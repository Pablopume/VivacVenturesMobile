package com.example.vivacventuresmobile.data.repositories

import com.example.vivacventuresmobile.data.sources.remote.FriendsRemoteDataSource
import com.example.vivacventuresmobile.data.sources.remote.ReportsRemoteDataSource
import com.example.vivacventuresmobile.domain.modelo.Friend
import com.example.vivacventuresmobile.domain.modelo.Report
import com.example.vivacventuresmobile.utils.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FriendsRepository @Inject constructor(
    private val remoteDataSource: FriendsRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun getAmigo(username: String): Flow<NetworkResult<Friend>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.getAmigo(username)
            emit(result)
        }.flowOn(dispatcher)
    }
}