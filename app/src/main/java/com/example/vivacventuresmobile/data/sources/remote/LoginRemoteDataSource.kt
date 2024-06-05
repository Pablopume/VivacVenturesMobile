package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.BuildConfig
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.data.model.LoginToken
import com.example.vivacventuresmobile.domain.modelo.Credentials
import com.example.vivacventuresmobile.utils.NetworkResult
import com.example.vivacventuresmobile.utils.StringProvider
import javax.inject.Inject


class LoginRemoteDataSource @Inject constructor(
    private val loginService: LoginService,
    private val stringProvider: StringProvider,
) {
    suspend fun register(credentials: Credentials): NetworkResult<Unit> {
        return try {
            val response = loginService.register(credentials)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                if (response.code() == 409) {
                    NetworkResult.Error(Constantes.USER_EXISTS)
                } else {
                    return if (BuildConfig.FLAVOR == Constantes.DEVELOPMENT) {
                        NetworkResult.Error("${response.code()} ${response.errorBody()}")
                    } else {
                        NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
                    }
                }
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun login(credentials: Credentials): NetworkResult<LoginToken> {
        return try {
            val response = loginService.login(credentials.username, credentials.password)
            if (response.isSuccessful) {
                val loginTokens = response.body()
                if (loginTokens != null) {
                    NetworkResult.Success(loginTokens)
                } else {
                    NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
                }
            } else {
                if (response.code() == 403) {
                    NetworkResult.Error(Constantes.USERORPASS_INCORRECT)
                } else if (response.code() == 401) {
                    NetworkResult.Error(Constantes.VERIFY_EMAIL)
                } else {
                    return if (BuildConfig.FLAVOR == Constantes.DEVELOPMENT) {
                        NetworkResult.Error("${response.code()} ${response.errorBody()}")
                    } else {
                        NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
                    }
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
                if (response.code() == 404) {
                    NetworkResult.Error(stringProvider.getString(R.string.no_account_for_that_mail))
                } else {
                    return if (BuildConfig.FLAVOR == Constantes.DEVELOPMENT) {
                        NetworkResult.Error("${response.code()} ${response.errorBody()}")
                    } else {
                        NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
                    }
                }
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun resetPassword(
        email: String,
        newPassword: String,
        temporalPassword: String
    ): NetworkResult<Unit> {
        return try {
            val response = loginService.resetPassword(email, newPassword, temporalPassword)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {

                if (response.code() == 403) {
                    NetworkResult.Error(stringProvider.getString(R.string.incorrect_temporal_password))
                } else {
                    return if (BuildConfig.FLAVOR == Constantes.DEVELOPMENT) {
                        NetworkResult.Error("${response.code()} ${response.errorBody()}")
                    } else {
                        NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
                    }
                }
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }
}

