package com.example.vivacventuresmobile.ui.screens.myplaces

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.domain.modelo.AutocompleteResult
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlaceByTypeUseCase
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlaceNearbyUseCase
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlacesByUsernameUseCase
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlacesUseCase
import com.example.vivacventuresmobile.utils.NetworkResult
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPlacesViewModel @Inject constructor(
    private val getVivacPlacesUseCase: GetVivacPlacesByUsernameUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MyPlacesState> by lazy {
        MutableStateFlow(MyPlacesState())
    }

    val uiState: MutableStateFlow<MyPlacesState> = _uiState

    init {
        _uiState.value = MyPlacesState(
            error = null,
            loading = false
        )
    }

    fun handleEvent(event: MyPlacesEvent) {
        when (event) {
            MyPlacesEvent.ErrorVisto -> _uiState.value = _uiState.value.copy(error = null)
            is MyPlacesEvent.SaveUsername -> {
                _uiState.value = _uiState.value.copy(username = event.username)
                getVivacPlaces()
            }
            is MyPlacesEvent.GetVivacPlaces -> getVivacPlaces()
        }
    }

    private fun getVivacPlaces() {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            getVivacPlacesUseCase(_uiState.value.username)
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