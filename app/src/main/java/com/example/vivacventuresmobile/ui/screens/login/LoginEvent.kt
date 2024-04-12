package com.example.vivacventuresmobile.ui.screens.login

sealed class LoginEvent {
    class OnLoginEvent() : LoginEvent()
    class OnRegisterEvent() : LoginEvent()
    object ErrorVisto : LoginEvent()
    class NameChanged(val name: String) : LoginEvent()
    class PasswordChange(val password: String) : LoginEvent()
//    class saveSettingsEvent(dataStore: DataStore<AppSettings>) : LoginEvent()
    class EmailChanged(val email: String) : LoginEvent()
}