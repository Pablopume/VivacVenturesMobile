package com.example.vivacventuresmobile.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apollo_davidroldan.utils.NetworkResult
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlacesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapViewModel @Inject constructor(
    private val getVivacPlacesRepository: GetVivacPlacesUseCase
) : ViewModel() {
    private val _uiState: MutableStateFlow<MapState> by lazy {
        MutableStateFlow(MapState())
    }
    val uiState: MutableStateFlow<MapState> = _uiState

    init {
        _uiState.value = MapState(
            error = null,
            loading = false
        )
        getVivacPlaces()
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

    fun handleEvent(event: MapEvent) {
        when (event) {
            MapEvent.ErrorVisto -> _uiState.value = _uiState.value.copy(error = null)
            is MapEvent.HandleLocationUpdate -> TODO()
            MapEvent.LocationOff -> TODO()
            is MapEvent.OnMapLongClick -> TODO()
            MapEvent.ToggleFalloutMap -> TODO()
        }
    }
}