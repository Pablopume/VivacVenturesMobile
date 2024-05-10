package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.data.model.VivacPlaceResponse
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VivacPlacesService {
    @GET("/vivacplaces")
    suspend fun getVivacPlaces(): Response<List<VivacPlaceResponse>>

    @GET("/vivacplaces/{id}")
    suspend fun getVivacPlace(@Path("id") id: Int): Response<VivacPlaceResponse>

    @GET("/vivacplaces/user/{username}")
    suspend fun getVivacPlacesByUsername(@Path("username") username: String): Response<List<VivacPlaceResponse>>

    @GET("/{type}")
    suspend fun getVivacPlaceByType(@Path("type") type: String): Response<List<VivacPlaceResponse>>

    @POST("/vivacplace")
    @Headers("Content-Type: application/json")
    suspend fun saveVivacPlace(@Body vivacPlace: VivacPlace): Response<VivacPlaceResponse>

    @GET("/nearby")
    suspend fun getNearbyPlaces(@Query("latitude") latitude: Double, @Query("longitude") longitude: Double): Response<List<VivacPlaceResponse>>
}