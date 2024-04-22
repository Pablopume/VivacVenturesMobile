package com.example.vivacventuresmobile.ui.screens.login

data class LoginState (
    val user: String = "",
    val password: String = "",
    val error: String? = null,
    val loginSuccess: Boolean = false,
    val isLoading: Boolean = false,
)