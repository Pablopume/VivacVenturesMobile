package com.example.vivacventuresmobile.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.domain.modelo.Credentials
import com.example.vivacventuresmobile.domain.usecases.RegisterUseCase
import com.example.vivacventuresmobile.ui.common.ConstantesPantallas
import com.example.vivacventuresmobile.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<RegisterState> by lazy {
        MutableStateFlow(RegisterState())
    }
    val uiState: MutableStateFlow<RegisterState> = _uiState


    init {
        _uiState.value = RegisterState(
            error = null,
        )
    }

    fun handleEvent(event: RegisterEvent) {
        when (event) {

            is RegisterEvent.Register -> {
                register()
            }

            is RegisterEvent.OnUserNameChange -> _uiState.update {
                it.copy(user = event.username)
            }

            is RegisterEvent.OnPasswordChange -> _uiState.update {
                it.copy(password = event.password)
            }

            is RegisterEvent.OnEmailChange -> _uiState.update {
                it.copy(correoElectronico = event.email)
            }

            RegisterEvent.ErrorVisto -> _uiState.update { it.copy(error = null) }
        }
    }


    private fun register() {
        if (_uiState.value.user.isEmpty() || _uiState.value.password.isEmpty() || _uiState.value.user.isBlank() || _uiState.value.password.isBlank()) {
            _uiState.update { it.copy(error = ConstantesPantallas.USER_OR_PASS_EMPTY) }
        } else {
            viewModelScope.launch {
                registerUseCase(
                    Credentials(
                        _uiState.value.correoElectronico,
                        _uiState.value.user,
                        _uiState.value.password
                    )
                )
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

                            is NetworkResult.Loading -> _uiState.update { it.copy(loading = true) }
                            is NetworkResult.Success -> _uiState.update {
                                it.copy(
                                    error = ConstantesPantallas.USER_REGISTRADO,
                                    loading = false,
                                    registered = true
                                )
                            }

                        }
                    }
            }
        }

    }
}