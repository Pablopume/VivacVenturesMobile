package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.BuildConfig
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.data.model.toFriend
import com.example.vivacventuresmobile.data.model.toFriendRequest
import com.example.vivacventuresmobile.data.model.toFriendRequestResponse
import com.example.vivacventuresmobile.domain.modelo.Friend
import com.example.vivacventuresmobile.domain.modelo.FriendRequest
import com.example.vivacventuresmobile.utils.NetworkResult
import com.example.vivacventuresmobile.utils.StringProvider
import javax.inject.Inject

class FriendsRemoteDataSource @Inject constructor(
    private val friendsService: FriendsService,
    private val stringProvider: StringProvider,
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
                return if (BuildConfig.FLAVOR == Constantes.DEVELOPMENT) {
                    NetworkResult.Error("${response.code()} ${response.errorBody()}")
                } else {
                    NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
                }
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
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
                return if (BuildConfig.FLAVOR == Constantes.DEVELOPMENT) {
                    NetworkResult.Error("${response.code()} ${response.errorBody()}")
                } else {
                    NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
                }
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
    }

    suspend fun sendFriendRequest(friendRequest: FriendRequest): NetworkResult<Unit> {
        return try {
            val response = friendsService.sendFriendRequest(friendRequest.toFriendRequestResponse())
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                return if (BuildConfig.FLAVOR == Constantes.DEVELOPMENT) {
                    NetworkResult.Error("${response.code()} ${response.errorBody()}")
                } else {
                    NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
                }
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun acceptFriendRequest(friendRequest: FriendRequest): NetworkResult<Unit> {
        return try {
            val response =
                friendsService.acceptFriendRequest(friendRequest.toFriendRequestResponse())
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                return if (BuildConfig.FLAVOR == Constantes.DEVELOPMENT) {
                    NetworkResult.Error("${response.code()} ${response.errorBody()}")
                } else {
                    NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
                }
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
                return if (BuildConfig.FLAVOR == Constantes.DEVELOPMENT) {
                    NetworkResult.Error("${response.code()} ${response.errorBody()}")
                } else {
                    NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
                }
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
                return if (BuildConfig.FLAVOR == Constantes.DEVELOPMENT) {
                    NetworkResult.Error("${response.code()} ${response.errorBody()}")
                } else {
                    NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
                }
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }
}