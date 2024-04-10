package com.example.vivacventuresmobile.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apollo_davidroldan.utils.NetworkResult
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlacesUseCase
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
        )
        getVivacPlaces()
    }

    fun handleEvent(event: MapEvent) {
        when (event) {
            MapEvent.ErrorVisto -> _uiState.value = _uiState.value.copy(error = null)
            is MapEvent.HandleLocationUpdate -> TODO()
            MapEvent.LocationOff -> TODO()
            is MapEvent.OnMapLongClick -> TODO()
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

            is MapEvent.UpdateCameraPosition -> {
                val cameraPosition = CameraPositionState(
                    position = CameraPosition.fromLatLngZoom(
                        event.latLng,
                        10f
                    )
                )
//                val cameraPosition = CameraUpdateFactory.newLatLngZoom(LatLng(event.latLng.latitude, event.latLng.longitude), 10f)
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
}