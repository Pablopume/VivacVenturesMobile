package com.example.vivacventuresmobile.ui.screens.login

data class LoginState (
    val user: String? = null,
    val password: String? = null,
    val correoElectronico: String? = null,
    val error: String? = null,
    val loginSuccess: Boolean = false,
    val isLoading: Boolean = false,
)