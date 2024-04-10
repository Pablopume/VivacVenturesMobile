package com.example.vivacventuresmobile.ui.screens.listplaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apollo_davidroldan.utils.NetworkResult
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListPlacesViewModel @Inject constructor(
    private val getVivacPlacesUseCase: GetVivacPlacesUseCase
): ViewModel(){
    private val _uiState: MutableStateFlow<ListPlacesState> by lazy {
        MutableStateFlow(ListPlacesState())
    }

    val uiState: MutableStateFlow<ListPlacesState> = _uiState

    init {
        _uiState.value = ListPlacesState(
            error = null,
            loading = false
        )
        getVivacPlaces()
    }

    fun handleEvent(event: ListPlacesEvent) {
        when (event) {
            ListPlacesEvent.ErrorVisto -> _uiState.value = _uiState.value.copy(error = null)
            is ListPlacesEvent.GetVivacPlaces -> getVivacPlaces()
        }
    }

    private fun getVivacPlaces() {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            getVivacPlacesUseCase()
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