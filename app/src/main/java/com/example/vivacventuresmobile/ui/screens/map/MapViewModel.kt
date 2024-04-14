package com.example.vivacventuresmobile.ui.screens.map

import android.annotation.SuppressLint
import android.os.Looper
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlacesUseCase
import com.example.vivacventuresmobile.utils.NetworkResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getVivacPlacesRepository: GetVivacPlacesUseCase
) : ViewModel() {
    private val _uiState: MutableStateFlow<MapState> by lazy {
        MutableStateFlow(MapState())
    }
    val uiState: MutableStateFlow<MapState> = _uiState


    private val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
    )
    private lateinit var fusedLocationCLient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    init {
        val cameraPosition = CameraPositionState(
            position = CameraPosition.fromLatLngZoom(
                LatLng(40.42966863252524, -3.6797065289867783),
                5.5f
            )
        )
        _uiState.value = MapState(
            error = null,
            loading = false,
            cameraPositionState = cameraPosition,
            currentLocation = LatLng(0.toDouble(), 0.toDouble()),
        )
        getVivacPlaces()
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
                                cameraPositionState = CameraPositionState(
                                    position = CameraPosition.fromLatLngZoom(
                                        LatLng(location.latitude, location.longitude),
                                        10f
                                    )
                                )
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
            is MapEvent.SendError -> {
                _uiState.value = _uiState.value.copy(error = event.error)
            }

            is MapEvent.UpdateCameraPosition -> {
                val cameraPosition = CameraPositionState(
                    position = CameraPosition.fromLatLngZoom(
                        event.latLng,
                        10f
                    )
                )
                _uiState.value = _uiState.value.copy(
                    cameraPositionState = cameraPosition
                )
            }
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
                                it.copy(
                                    error = result.message,
                                    loading = false
                                )
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
        //get location
        startLocationUpdates()
        _uiState.value = _uiState.value.copy(
            error = "Location is on",
            isLocationEnabled = true,
//            cameraPositionState = CameraPositionState(
//                position = CameraPosition.fromLatLngZoom(
//                    _uiState.value.currentLocation!!,
//                    10f
//                )
//            )
        )

    }

    private fun locationOff() {
        _uiState.value = _uiState.value.copy(
            isLocationEnabled = false,
//            cameraPositionState = CameraPositionState(
//                position = CameraPosition.fromLatLngZoom(
//                    LatLng(40.42966863252524, -3.6797065289867783),
//                    5.5f
//                )),
            currentLocation = LatLng(0.toDouble(), 0.toDouble()),
            error = "Location is off"
        )

        //stop location updates
        locationCallback?.let {
            fusedLocationCLient.removeLocationUpdates(it)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationCallback?.let {
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 100
            )
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(3000)
                .setMaxUpdateDelayMillis(100)
                .build()

            fusedLocationCLient?.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }
}