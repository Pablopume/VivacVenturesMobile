package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.data.model.LoginTokens
import com.example.vivacventuresmobile.domain.modelo.Credentials
import com.example.vivacventuresmobile.utils.NetworkResult
import javax.inject.Inject


class LoginRemoteDataSource @Inject constructor(
    private val loginService: LoginService,
) {
    suspend fun register(credentials: Credentials): NetworkResult<Unit> {
        return try {
            val response = loginService.register(credentials)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {

                    NetworkResult.Error("${response.code()} ${response.errorBody()}")

            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun login(credentials: Credentials): NetworkResult<LoginTokens> {
        return try {
            val response = loginService.login(credentials.username, credentials.password)
            if (response.isSuccessful) {
                val loginTokens = response.body()
                if (loginTokens != null) {
                    NetworkResult.Success(loginTokens)
                } else {
                    NetworkResult.Error(Constantes.ERROR_DESCONOCIDO)
                }
            } else {
                if (response.code() == 403) {
                    NetworkResult.Error(Constantes.USERORPASS_INCORRECT)
                } else if (response.code() == 401) {
                    NetworkResult.Error(Constantes.VERIFY_EMAIL)
                } else {
                    NetworkResult.Error("${response.code()} ${response.message()}")
                }
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun forgotPassword(email: String): NetworkResult<Unit> {
        return try {
            val response = loginService.forgotPassword(email)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun resetPassword(email: String, newPassword: String, temporalPassword: String): NetworkResult<Unit> {
        return try {
            val response = loginService.resetPassword(email, newPassword, temporalPassword)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }
}

