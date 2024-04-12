package com.example.vivacventuresmobile.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.ui.screens.login.LoginEvent
import com.example.vivacventuresmobile.ui.screens.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel  @Inject constructor(
//    private val loginUseCase: LoginUseCase,
//    private val registerUseCase: RegisterUseCase
) : ViewModel()  {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(
                correoElectronico = "",
                user = "",
                password = "",
                error = null,
                loginSuccess = false,
                isLoading = false,
            ) }
        }
    }

    fun handleEvent(loginEvent: LoginEvent) {
        when(loginEvent) {
            is LoginEvent.OnLoginEvent -> doLogin()
            is LoginEvent.OnRegisterEvent -> doRegister()
            LoginEvent.ErrorVisto -> viewModelScope.launch { _state.update { it.copy(error = null) } }
            is LoginEvent.EmailChanged -> _state.value = _state.value.copy(correoElectronico = loginEvent.email)
            is LoginEvent.NameChanged -> _state.update { it.copy(user = loginEvent.name) }
            is LoginEvent.PasswordChange -> _state.update { it.copy(password = loginEvent.password) }
        }
    }



    fun doLogin() {
        viewModelScope.launch {
               if (state.value.user == "" || state.value.password == "") {
                    _state.value = _state.value.copy(error = "CAMPOS INCOMPLETOS", isLoading = false)
                } else {
//                    val result = loginUseCase(state.value.user!!, state.value.password!!)
                   _state.value = _state.value.copy(isLoading = false, loginSuccess = true)
               }
        }
    }

    private fun doRegister() {
//        viewModelScope.launch {
//            if (state.value.correoElectronico == "" || state.value.user == ""|| state.value.password == "") {
//                _state.value = _state.value.copy(error = Constants.CAMPOS_INCOMPLETOS, isLoading = false)
//            } else {
//                val result = registerUseCase(state.value.user!!, state.value.password!!,state.value.correoElectronico!!)
//                when (result) {
//                    is NetworkResult.Success -> {
//                        _state.value = _state.value.copy(error = Constants.USUARIOREGISTER, isLoading = false)
//                    }
//                    is NetworkResult.Error -> {
//                        _state.value = _state.value.copy(error = result.message, isLoading = false)
//                    }
//                }
//            }
//        }
    }

}