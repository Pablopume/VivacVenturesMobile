package com.example.vivacventuresmobile.data.repositories

import com.example.vivacventuresmobile.data.sources.remote.FriendsRemoteDataSource
import com.example.vivacventuresmobile.domain.modelo.Friend
import com.example.vivacventuresmobile.domain.modelo.FriendRequest
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

    fun getFriends(username: String): Flow<NetworkResult<List<FriendRequest>>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.getFriends(username)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun sendFriendRequest(friendRequest: FriendRequest): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.sendFriendRequest(friendRequest)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun acceptFriendRequest(friendRequest: FriendRequest): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.acceptFriendRequest(friendRequest)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun rejectFriendRequest(friendRequest: FriendRequest): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.rejectFriendRequest(friendRequest)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun deleteFriend(friend: FriendRequest): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.deleteFriend(friend)
            emit(result)
        }.flowOn(dispatcher)
    }
}