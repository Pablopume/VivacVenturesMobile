package com.example.vivacventuresmobile.ui.screens.myplaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.domain.usecases.DeleteVivacPlaceUseCase
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlacesByUsernameUseCase
import com.example.vivacventuresmobile.utils.NetworkResult
import com.example.vivacventuresmobile.utils.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPlacesViewModel @Inject constructor(
    private val getVivacPlacesUseCase: GetVivacPlacesByUsernameUseCase,
    private val deletePlaceUseCase: DeleteVivacPlaceUseCase,
    private val stringProvider: StringProvider
) : ViewModel() {
    private val _uiState: MutableStateFlow<MyPlacesState> by lazy {
        MutableStateFlow(MyPlacesState())
    }

    val uiState: StateFlow<MyPlacesState> = _uiState

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
            is MyPlacesEvent.DeleteVivacPlace -> deletePlace(event.id)
        }
    }

    private fun deletePlace(id: Int) {
        viewModelScope.launch {
            deletePlaceUseCase(id)
                .catch { cause ->
                    _uiState.update {
                        it.copy(
                            error = cause.message,
                            loading = false
                        )
                    }
                }
                .collect { result ->
                    when (result) {
                        is NetworkResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    error = result.message,
                                    loading = false
                                )
                            }
                            getVivacPlaces()
                        }

                        is NetworkResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    loading = false,
                                    error = stringProvider.getString(R.string.place_deleted),
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
                    }
                }
        }

    }

    private fun getVivacPlaces() {
        if (_uiState.value.username.isEmpty()) return
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
                                    loading = false,
                                    firstTime = false
                                )
                            }
                        }

                        is NetworkResult.Success -> {
                            result.data?.let { places ->
                                _uiState.update {
                                    it.copy(
                                        vivacPlaces = places,
                                        loading = false,
                                        firstTime = false
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