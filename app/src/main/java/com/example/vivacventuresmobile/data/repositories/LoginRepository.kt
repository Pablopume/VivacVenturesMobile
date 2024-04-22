package com.example.vivacventuresmobile.data.repositories

import com.example.vivacventuresmobile.data.model.LoginToken
import com.example.vivacventuresmobile.data.sources.remote.LoginRemoteDataSource
import com.example.vivacventuresmobile.domain.modelo.Credentials
import com.example.vivacventuresmobile.utils.NetworkResult
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class LoginRepository @Inject constructor(
    private val remoteDataSource: LoginRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun register(credentials: Credentials): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.register(credentials)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun login(credentials: Credentials): Flow<NetworkResult<LoginToken>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.login(credentials)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun forgotPassword(email: String): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.forgotPassword(email)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun resetPassword(email: String, password: String, tempPassword: String): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.resetPassword(email, password, tempPassword)
            emit(result)
        }.flowOn(dispatcher)
    }



}