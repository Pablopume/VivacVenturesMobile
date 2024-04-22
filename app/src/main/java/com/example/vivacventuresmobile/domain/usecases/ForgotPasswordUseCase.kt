package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.LoginRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(private val repository: LoginRepository){
    operator fun invoke(email: String) = repository.forgotPassword(email)
}