package com.example.vivacventuresmobile.data.repositories

import com.example.vivacventuresmobile.data.sources.remote.FavouritesRemoteDataSource
import com.example.vivacventuresmobile.data.sources.remote.ListsRemoteDataSource
import com.example.vivacventuresmobile.domain.modelo.ListFavs
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.example.vivacventuresmobile.domain.modelo.VivacPlaceList
import com.example.vivacventuresmobile.utils.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ListsRepository @Inject constructor(
    private val remoteDataSource: ListsRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun getLists(username: String): Flow<NetworkResult<List<ListFavs>>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.getLists(username)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun getListSharedWith(id: Int): Flow<NetworkResult<List<String>>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.getListSharedWith(id)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun saveList(listFavs: ListFavs): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.saveList(listFavs)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun shareList(id: Int, username: String): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.shareList(id, username)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun deleteList(id: Int): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.deleteList(id)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun deleteListSharedWith(id: Int, username: String): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.deleteSharedList(id, username)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun addFavoriteToList(listId: Int, vivacId: Int): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.addFavoriteToList(listId, vivacId)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun removeFavoriteFromList(listId: Int, vivacId: Int): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.removeFavoriteFromList(listId, vivacId)
            emit(result)
        }.flowOn(dispatcher)
    }
}