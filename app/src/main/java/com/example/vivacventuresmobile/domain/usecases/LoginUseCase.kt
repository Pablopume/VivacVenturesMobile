package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.model.LoginToken
import com.example.vivacventuresmobile.data.repositories.LoginRepository
import com.example.vivacventuresmobile.data.sources.remote.TokenManager
import com.example.vivacventuresmobile.domain.modelo.Credentials
import com.example.vivacventuresmobile.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    var repository: LoginRepository,
    private val tokenManager: TokenManager,
){
    suspend operator fun invoke(credentials : Credentials): Flow<NetworkResult<LoginToken>> {
        val loginResult = repository.login(credentials)

        loginResult.collect{ result ->
            if (result is NetworkResult.Success){
                result.data?.accessToken?.let { tokenManager.saveAccessToken(it) }
                result.data?.refreshToken?.let { tokenManager.saveRefreshToken(it) }
            }
        }

        return loginResult
    }
}
