package com.example.vivacventuresmobile.ui.screens.login

sealed class LoginEvent {
    class OnLoginEvent() : LoginEvent()
    object ErrorVisto : LoginEvent()
    class NameChanged(val name: String) : LoginEvent()
    class PasswordChange(val password: String) : LoginEvent()
}