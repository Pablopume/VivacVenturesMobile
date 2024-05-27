package com.example.vivacventuresmobile.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.domain.modelo.Credentials
import com.example.vivacventuresmobile.domain.usecases.LoginUseCase
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
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val stringProvider: StringProvider,
) : ViewModel() {

    private val _uiState: MutableStateFlow<LoginState> by lazy {
        MutableStateFlow(LoginState())
    }

    val uiState: StateFlow<LoginState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    user = "",
                    password = "",
                    error = null,
                    loginSuccess = false,
                    isLoading = false,
                )
            }
        }
    }

    fun handleEvent(loginEvent: LoginEvent) {
        when (loginEvent) {
            is LoginEvent.OnLoginEvent -> doLogin()
            LoginEvent.ErrorVisto -> viewModelScope.launch { _uiState.update { it.copy(error = null) } }

            is LoginEvent.NameChanged -> _uiState.update { it.copy(user = loginEvent.name) }
            is LoginEvent.PasswordChange -> _uiState.update { it.copy(password = loginEvent.password) }
        }
    }


    private fun doLogin() {
        if (_uiState.value.user == "" || _uiState.value.password == "" || _uiState.value.user.isEmpty() || _uiState.value.password.isEmpty()) {
            _uiState.update {
                it.copy(
                    error = stringProvider.getString(R.string.user_or_pass_empty),
                    isLoading = false
                )
            }
        } else {
            viewModelScope.launch {
                loginUseCase(Credentials("", _uiState.value.user, _uiState.value.password))
                    .catch(action = { cause ->
                        _uiState.update { it.copy(error = cause.message, isLoading = false) }
                    })
                    .collect { result ->
                        when (result) {
                            is NetworkResult.Success -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        loginSuccess = true
                                    )
                                }
                            }

                            is NetworkResult.Loading -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = true
                                    )
                                }
                            }

                            is NetworkResult.Error -> {
                                _uiState.update {
                                    it.copy(
                                        error = result.message,
                                        isLoading = false
                                    )
                                }
                            }
                        }

                    }


            }

        }
    }


}