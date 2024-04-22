package com.example.vivacventuresmobile.ui.screens.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.domain.usecases.ForgotPasswordUseCase
import com.example.vivacventuresmobile.domain.usecases.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel() {
    private val _uiState: MutableStateFlow<ForgotPasswordState> by lazy {
        MutableStateFlow(ForgotPasswordState())
    }
    val uiState: MutableStateFlow<ForgotPasswordState> = _uiState


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
        _uiState.update { it.copy(emailsend = true) }
        if (_uiState.value.correoElectronico.isNotEmpty()) {
            forgotPasswordUseCase.invoke(_uiState.value.correoElectronico)
            viewModelScope.launch {

            }
        } else {
            _uiState.update { it.copy(error = "Please enter an email") }
        }
    }

    private fun changePassword() {
        _uiState.update { it.copy(passwordchanged = true) }
    }


}