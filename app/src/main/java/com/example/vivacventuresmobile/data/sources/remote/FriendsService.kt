package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.data.model.FriendRequestResponse
import com.example.vivacventuresmobile.data.model.FriendResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface FriendsService {
    @GET("/amigo")
    suspend fun getAmigos(@Query("username") username: String): Response<FriendResponse>

    @GET("/friends")
    suspend fun getFriends(@Query("username") username: String): Response<List<FriendRequestResponse>>

    @POST("/friends/send")
    suspend fun sendFriendRequest(@Body friendRequest: FriendRequestResponse): Response<Unit>

    @PUT("/friends/accept")
    suspend fun acceptFriendRequest(@Body friendRequest: FriendRequestResponse): Response<Unit>

    @DELETE("/friends/reject/{id}")
    suspend fun rejectFriendRequest(@Path("id") id: Int): Response<Unit>

    @DELETE("/friends/delete/{id}")
    suspend fun deleteFriend(@Path("id") id: Int): Response<Unit>
}