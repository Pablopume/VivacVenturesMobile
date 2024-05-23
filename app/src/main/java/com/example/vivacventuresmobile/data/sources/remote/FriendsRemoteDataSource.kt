package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.data.model.toFriend
import com.example.vivacventuresmobile.data.model.toVivacPlace
import com.example.vivacventuresmobile.domain.modelo.Friend
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
                return NetworkResult.Error("Error")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }
}