package com.example.vivacventuresmobile.ui.screens.detalleplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apollo_davidroldan.utils.NetworkResult
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlaceUseCase
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetallePlaceViewModel @Inject constructor(
    private val getVivacPlaceUseCase: GetVivacPlaceUseCase
): ViewModel(){
    private val _uiState: MutableStateFlow<DetallePlaceState> by lazy {
        MutableStateFlow(DetallePlaceState())
    }

    val uiState: MutableStateFlow<DetallePlaceState> = _uiState

    init {
        _uiState.value = DetallePlaceState(
            error = null,
            loading = false
        )
    }

    fun handleEvent(event: DetallePlaceEvent) {
        when (event) {
            DetallePlaceEvent.ErrorVisto -> _uiState.value = _uiState.value.copy(error = null)
            is DetallePlaceEvent.GetDetalle -> getVivacPlace(event.id)
        }
    }

    private fun getVivacPlace(id: Int) {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            getVivacPlaceUseCase(id)
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
                                        vivacPlace = places,
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