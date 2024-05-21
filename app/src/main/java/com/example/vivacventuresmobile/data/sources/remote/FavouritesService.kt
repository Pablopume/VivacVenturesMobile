package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.data.model.FavouritesVivacPlaceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FavouritesService {

    @POST("/favorito")
    @Headers("Content-Type: application/json")
    suspend fun saveFavorito(@Query("username") username: String, @Query("vivacId") vivacId: Int): Response<Unit>

    @GET("/favoritos/{username}")
    suspend fun getFavoritos(@Path("username") username: String): Response<List<FavouritesVivacPlaceResponse>>

    @DELETE("/favorito/delete")
    @Headers("Content-Type: application/json")
    suspend fun deleteFavorito(@Query("username") username: String, @Query("vivacId") vivacId: Int): Response<Unit>
}