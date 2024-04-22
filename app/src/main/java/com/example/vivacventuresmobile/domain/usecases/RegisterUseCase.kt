package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.LoginRepository
import com.example.vivacventuresmobile.domain.modelo.Credentials
import javax.inject.Inject


class RegisterUseCase @Inject constructor(var repository: LoginRepository){
    operator fun invoke(credentials: Credentials) = repository.register(credentials)
}