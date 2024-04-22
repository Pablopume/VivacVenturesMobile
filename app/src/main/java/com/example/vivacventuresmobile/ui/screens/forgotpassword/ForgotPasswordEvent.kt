package com.example.vivacventuresmobile.ui.screens.forgotpassword

sealed class ForgotPasswordEvent {
    class ForgotPassword() : ForgotPasswordEvent()
    class SendEmail() : ForgotPasswordEvent()
    class OnTempPasswordChange(val temppassword: String) : ForgotPasswordEvent()
    class OnPasswordChange(val password: String) : ForgotPasswordEvent()
    class OnEmailChange(val email: String) : ForgotPasswordEvent()
    object ErrorVisto : ForgotPasswordEvent()
}
