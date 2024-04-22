package com.example.vivacventuresmobile.ui.screens.forgotpassword

data class ForgotPasswordState (
    val temppassword: String = "",
    val password: String = "",
    val correoElectronico: String = "",
    val error: String? = null,
    val passwordchanged: Boolean = false,
    val emailsend: Boolean = false,
    val loading: Boolean = false,
)