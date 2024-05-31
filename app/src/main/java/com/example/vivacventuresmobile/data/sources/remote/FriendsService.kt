package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.common.Constantes
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
    @GET(Constantes.AMIGO)
    suspend fun getAmigos(@Query(Constantes.USERNAME) username: String): Response<FriendResponse>

    @GET(Constantes.FRIENDS)
    suspend fun getFriends(@Query(Constantes.USERNAME) username: String): Response<List<FriendRequestResponse>>

    @POST(Constantes.FRIENDS_SEND)
    suspend fun sendFriendRequest(@Body friendRequest: FriendRequestResponse): Response<Unit>

    @PUT(Constantes.FRIENDS_ACCEPT)
    suspend fun acceptFriendRequest(@Body friendRequest: FriendRequestResponse): Response<Unit>

    @DELETE(Constantes.FRIENDS_REJECT)
    suspend fun rejectFriendRequest(@Path(Constantes.ID) id: Int): Response<Unit>

    @DELETE(Constantes.FRIENDS_DELETE)
    suspend fun deleteFriend(@Path(Constantes.ID) id: Int): Response<Unit>
}