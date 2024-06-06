package com.example.vivacventuresmobile.data.repositories

import com.example.vivacventuresmobile.data.sources.remote.FavouritesRemoteDataSource
import com.example.vivacventuresmobile.domain.modelo.VivacPlaceList
import com.example.vivacventuresmobile.utils.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FavouritesRepository @Inject constructor(
    private val remoteDataSource: FavouritesRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun saveFavourite(username: String, vivacId: Int): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.saveFavourite(username, vivacId)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun getFavourites(username: String): Flow<NetworkResult<List<VivacPlaceList>>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.getFavourites(username)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun deleteFavourite(username: String, vivacId: Int): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.deleteFavourite(username, vivacId)
            emit(result)
        }.flowOn(dispatcher)
    }
}