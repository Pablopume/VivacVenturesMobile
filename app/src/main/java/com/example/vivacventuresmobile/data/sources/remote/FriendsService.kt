package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.data.model.FriendResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FriendsService {
    @GET("/amigo")
    suspend fun getAmigos(@Query("username") username: String): Response<FriendResponse>
}