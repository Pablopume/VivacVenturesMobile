package com.example.vivacventuresmobile.data.repositories

import com.example.vivacventuresmobile.data.sources.remote.ValorationsRemoteDataSource
import com.example.vivacventuresmobile.domain.modelo.Valoration
import com.example.vivacventuresmobile.utils.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ValorationsRepository @Inject constructor(
    private val remoteDataSource: ValorationsRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun addValoration(valoration: Valoration): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.addValoration(valoration)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun deleteValoration(id: Int): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.deleteValoration(id)
            emit(result)
        }.flowOn(dispatcher)
    }
}