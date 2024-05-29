package com.example.vivacventuresmobile.ui.screens.map

import android.annotation.SuppressLint
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.domain.modelo.Credentials
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlacesUseCase
import com.example.vivacventuresmobile.domain.usecases.LoginUseCase
import com.example.vivacventuresmobile.utils.NetworkResult
import com.example.vivacventuresmobile.utils.StringProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.logging.Logger
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getVivacPlacesRepository: GetVivacPlacesUseCase,
    private val loginUseCase: LoginUseCase,
    private val stringProvider: StringProvider,
) : ViewModel() {

    private val _uiState: MutableStateFlow<MapState> by lazy {
        MutableStateFlow(MapState())
    }
    val uiState: StateFlow<MapState> = _uiState


    private val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
    )
    private lateinit var fusedLocationCLient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    init {
        _uiState.value = MapState(
            error = null,
            loading = false,
            currentLatLng = LatLng(40.42966863252524, -3.6797065289867783),
            currentLocation = LatLng(0.toDouble(), 0.toDouble()),
        )
    }

    fun handleEvent(event: MapEvent) {
        when (event) {
            MapEvent.ErrorVisto -> _uiState.value = _uiState.value.copy(error = null)
            MapEvent.LocationOn -> locationOn()
            MapEvent.LocationOff -> locationOff()
            is MapEvent.OnMapLongClick -> TODO()
            is MapEvent.StartLocationUpdates -> {
                fusedLocationCLient = event.fusedLocationClient
                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(p0: LocationResult) {
                        super.onLocationResult(p0)
                        for (location in p0.locations) {
                            _uiState.value = _uiState.value.copy(
                                currentLocation = LatLng(location.latitude, location.longitude),
                                currentLatLng = LatLng(location.latitude, location.longitude)
                            )
                        }
                    }
                }
            }

            MapEvent.ToggleDarkMap -> {
                _uiState.value = _uiState.value.copy(
                    properties = _uiState.value.properties.copy(
                        mapStyleOptions = if (_uiState.value.isDarkMap) {
                            null
                        } else MapStyleOptions(MapStyle.json),
                    ),
                    isDarkMap = !_uiState.value.isDarkMap
                )

            }

            MapEvent.GetAll -> {
                getVivacPlaces()
            }

            is MapEvent.SendError -> {
                _uiState.value = _uiState.value.copy(error = event.error)
            }

            is MapEvent.UpdateCameraPosition -> {
                _uiState.value = _uiState.value.copy(
                    currentLatLng = event.latLng
                )
            }

            is MapEvent.reLogin -> doReLogin(event.username, event.password)
        }
    }

    private fun getVivacPlaces() {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            getVivacPlacesRepository()
                .catch(action = { cause ->
                    _uiState.update {
                        it.copy(
                            error = cause.message,
                            loading = false
                        )
                    }
                })
                .collect { result ->
                    when (result) {
                        is NetworkResult.Error -> {
                            _uiState.update {
                                if (result.message == "Relogin") {
                                    it.copy(
                                        loading = false,
                                        relogin = true
                                    )
                                } else {
                                    it.copy(
                                        error = result.message,
                                        loading = false
                                    )
                                }
                            }
                        }

                        is NetworkResult.Success -> {
                            result.data?.let { places ->
                                _uiState.update {
                                    it.copy(
                                        vivacPlaces = places,
                                        loading = false
                                    )
                                }
                            }
                        }

                        is NetworkResult.Loading -> {
                            _uiState.update {
                                it.copy(
                                    loading = true
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun locationOn() {
        _uiState.value = _uiState.value.copy(
            error = stringProvider.getString(R.string.location_on),
            isLocationEnabled = true
        )
        startLocationUpdates()
    }

    private fun locationOff() {
        _uiState.value = _uiState.value.copy(
            isLocationEnabled = false,
            currentLocation = LatLng(0.toDouble(), 0.toDouble()),
            error = stringProvider.getString(R.string.location_off)
        )

        //stop location updates
        locationCallback?.let {
            fusedLocationCLient.removeLocationUpdates(it)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (_uiState.value.isLocationEnabled) {
            locationCallback.let {
                val locationRequest = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY, 100
                )
                    .setWaitForAccurateLocation(false)
                    .setMinUpdateIntervalMillis(3000)
                    .setMaxUpdateDelayMillis(100)
                    .build()

                fusedLocationCLient.requestLocationUpdates(
                    locationRequest,
                    it,
                    Looper.getMainLooper()
                )
            }
        } else {
            locationCallback?.let {
                fusedLocationCLient.removeLocationUpdates(it)
            }
        }
    }

    private fun doReLogin(username: String, password: String) {
        viewModelScope.launch {
            loginUseCase(Credentials("", username, password))
                .catch(action = { cause ->
                    _uiState.update { it.copy(error = cause.message, loading = false) }
                })
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    loading = false,
                                    relogin = false
                                )
                            }
                            getVivacPlaces()
                        }

                        is NetworkResult.Loading -> {
                            _uiState.update {
                                it.copy(
                                    loading = true
                                )
                            }
                        }

                        is NetworkResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    error = result.message,
                                    loading = false
                                )
                            }
                        }
                    }
                }
        }
    }
}

