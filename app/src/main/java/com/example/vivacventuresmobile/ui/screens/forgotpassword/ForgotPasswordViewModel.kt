package com.example.vivacventuresmobile.ui.screens.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.domain.usecases.ForgotPasswordUseCase
import com.example.vivacventuresmobile.domain.usecases.ResetPasswordUseCase
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
class ForgotPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val stringProvider: StringProvider,
) : ViewModel() {
    private val _uiState: MutableStateFlow<ForgotPasswordState> by lazy {
        MutableStateFlow(ForgotPasswordState())
    }
    val uiState: StateFlow<ForgotPasswordState> = _uiState


    init {
        _uiState.value = ForgotPasswordState(
            error = null,
        )
    }

    fun handleEvent(event: ForgotPasswordEvent) {
        when (event) {

            is ForgotPasswordEvent.ForgotPassword -> {
                changePassword()
            }

            is ForgotPasswordEvent.SendEmail -> {
                sendEmail()
            }

            is ForgotPasswordEvent.OnTempPasswordChange -> _uiState.update {
                it.copy(temppassword = event.temppassword)
            }

            is ForgotPasswordEvent.OnPasswordChange -> _uiState.update {
                it.copy(password = event.password)
            }

            is ForgotPasswordEvent.OnEmailChange -> _uiState.update {
                it.copy(correoElectronico = event.email)
            }

            ForgotPasswordEvent.ErrorVisto -> _uiState.update { it.copy(error = null) }
        }
    }

    private fun sendEmail() {
        if (_uiState.value.correoElectronico.isNotEmpty()) {
            viewModelScope.launch {
                forgotPasswordUseCase(_uiState.value.correoElectronico)
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
                            is NetworkResult.Success -> {
                                _uiState.update {
                                    it.copy(
                                        error = stringProvider.getString(R.string.email_send_with_temp_pass),
                                        loading = false,
                                        emailsend = true
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
        } else {
            _uiState.update { it.copy(error = stringProvider.getString(R.string.enter_an_email)) }
        }
    }

    private fun changePassword() {
        if (_uiState.value.correoElectronico.isNotEmpty() && _uiState.value.password.isNotEmpty() && _uiState.value.temppassword.isNotEmpty()) {
            viewModelScope.launch {
                resetPasswordUseCase(_uiState.value.correoElectronico, _uiState.value.password, _uiState.value.temppassword)
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
                            is NetworkResult.Success -> {
                                _uiState.update {
                                    it.copy(
                                        error = stringProvider.getString(R.string.pass_changed),
                                        loading = false,
                                        passwordchanged = true
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
        } else {
            _uiState.update { it.copy(error = stringProvider.getString(R.string.fill_all_fields)) }
        }

    }


}