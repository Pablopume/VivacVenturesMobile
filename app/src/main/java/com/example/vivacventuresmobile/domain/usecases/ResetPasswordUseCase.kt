package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.LoginRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(private val repository: LoginRepository){
    operator fun invoke(email: String, password: String, tempPassword: String) = repository.resetPassword(email, password, tempPassword)
}