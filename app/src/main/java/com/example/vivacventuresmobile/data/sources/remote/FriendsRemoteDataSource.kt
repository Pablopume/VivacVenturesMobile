package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.data.model.toFriend
import com.example.vivacventuresmobile.data.model.toFriendRequest
import com.example.vivacventuresmobile.data.model.toFriendRequestResponse
import com.example.vivacventuresmobile.data.model.toVivacPlace
import com.example.vivacventuresmobile.domain.modelo.Friend
import com.example.vivacventuresmobile.domain.modelo.FriendRequest
import com.example.vivacventuresmobile.domain.modelo.Report
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.example.vivacventuresmobile.utils.NetworkResult
import javax.inject.Inject

class FriendsRemoteDataSource @Inject constructor(
    private val friendsService: FriendsService
) {
    suspend fun getAmigo(username: String): NetworkResult<Friend> {
        try {
            val response = friendsService.getAmigos(username)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body.toFriend())
                }
            } else {
                return NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }

    suspend fun getFriends(username: String): NetworkResult<List<FriendRequest>> {
        try {
            val response = friendsService.getFriends(username)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body.map { it.toFriendRequest() })
                }
            } else {
                return NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }

    suspend fun sendFriendRequest(friendRequest: FriendRequest): NetworkResult<Unit> {
        return try {
            val response = friendsService.sendFriendRequest(friendRequest.toFriendRequestResponse())
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun acceptFriendRequest(friendRequest: FriendRequest): NetworkResult<Unit> {
        return try {
            val response = friendsService.acceptFriendRequest(friendRequest.toFriendRequestResponse())
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun rejectFriendRequest(friendRequest: FriendRequest): NetworkResult<Unit> {
        return try {
            val response = friendsService.rejectFriendRequest(friendRequest.id)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun deleteFriend(friendRequest: FriendRequest): NetworkResult<Unit> {
        return try {
            val response = friendsService.deleteFriend(friendRequest.id)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }
}