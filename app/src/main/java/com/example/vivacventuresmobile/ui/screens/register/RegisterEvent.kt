package com.example.vivacventuresmobile.ui.screens.register

sealed class RegisterEvent {
    class Register() : RegisterEvent()
    class OnUserNameChange(val username: String) : RegisterEvent()
    class OnPasswordChange(val password: String) : RegisterEvent()
    class OnEmailChange(val email: String) : RegisterEvent()
    object ErrorVisto : RegisterEvent()
}
