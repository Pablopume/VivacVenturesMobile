package com.example.vivacventuresmobile.ui.screens.detalleplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.domain.usecases.AddFavouriteUseCase
import com.example.vivacventuresmobile.domain.usecases.AddValorationUseCase
import com.example.vivacventuresmobile.domain.usecases.DeleteFavouriteUseCase
import com.example.vivacventuresmobile.domain.usecases.DeleteValorationUseCase
import com.example.vivacventuresmobile.domain.usecases.DeleteVivacPlaceUseCase
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlaceUseCase
import com.example.vivacventuresmobile.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetallePlaceViewModel @Inject constructor(
    private val getVivacPlaceUseCase: GetVivacPlaceUseCase,
    private val addFavouriteUseCase: AddFavouriteUseCase,
    private val deleteFavouriteUseCase: DeleteFavouriteUseCase,
    private val deletePlaceUseCase: DeleteVivacPlaceUseCase,
    private val addValorationUseCase: AddValorationUseCase,
    private val deleteValorationUseCase: DeleteValorationUseCase
) : ViewModel() {
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
            is DetallePlaceEvent.AddFavourite -> addFavourite()
            is DetallePlaceEvent.DeleteFavourite -> deleteFavourite()
            is DetallePlaceEvent.SaveUsernameAndId -> {
                _uiState.value =
                    _uiState.value.copy(username = event.username)
                getVivacPlace(event.vivacId ?: 0)
            }

            is DetallePlaceEvent.DeletePlace -> deletePlace()
            is DetallePlaceEvent.AddValoration -> {

            }

            is DetallePlaceEvent.DeleteValoration -> {

            }
        }
    }

    private fun deleteValoration(id: Int){
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            deleteValorationUseCase(id)
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
                        }

                        is NetworkResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    error = "Valoration deleted",
                                    loading = false,
                                )
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

    private fun getVivacPlace(id: Int) {
        if (_uiState.value.username.isNotEmpty() && _uiState.value.vivacPlace?.id != 0) {
            _uiState.update { it.copy(loading = true) }
            viewModelScope.launch {
                getVivacPlaceUseCase(id, _uiState.value.username)
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

    private fun addFavourite() {
        viewModelScope.launch {
            addFavouriteUseCase(_uiState.value.username ?: "", _uiState.value.vivacPlace?.id ?: 0)
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
                        }

                        is NetworkResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    loading = false,
                                )
                            }
                            getVivacPlace(_uiState.value.vivacPlace?.id ?: 0)
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

    private fun deleteFavourite() {
        viewModelScope.launch {
            deleteFavouriteUseCase(
                _uiState.value.username ?: "",
                _uiState.value.vivacPlace?.id ?: 0
            )
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
                        }

                        is NetworkResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    loading = false,
                                )
                            }
                            getVivacPlace(_uiState.value.vivacPlace?.id ?: 0)
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

    private fun deletePlace() {
        if (_uiState.value.vivacPlace?.id != 0) {
            viewModelScope.launch {
                deletePlaceUseCase(_uiState.value.vivacPlace?.id ?: 0)
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
                            }

                            is NetworkResult.Success -> {
                                _uiState.update {
                                    it.copy(
                                        loading = false,
                                        error = "Place deleted",
                                        deleted = true
                                    )
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
}