package com.example.vivacventuresmobile.ui.screens.register

data class RegisterState (
    val user: String = "",
    val password: String = "",
    val correoElectronico: String = "",
    val error: String? = null,
    val registered: Boolean = false,
    val loading: Boolean = false,
)