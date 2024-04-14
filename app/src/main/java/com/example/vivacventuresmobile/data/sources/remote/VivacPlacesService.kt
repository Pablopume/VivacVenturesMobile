package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.data.model.VivacPlaceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VivacPlacesService {
    @GET("/vivacplaces")
    suspend fun getVivacPlaces(): Response<List<VivacPlaceResponse>>

    @GET("/vivacplaces/{id}")
    suspend fun getVivacPlace(@Path("id") id: Int): Response<VivacPlaceResponse>

    @GET("/{type}")
    suspend fun getVivacPlaceByType(@Path("type") type: String): Response<List<VivacPlaceResponse>>

    @POST("/vivacplace")
    suspend fun postVivacPlace(@Body vivacPlaceResponse: VivacPlaceResponse): Response<VivacPlaceResponse>


//      @GET("/vivacplaces/type?type={type}")
//    suspend fun getVivacPlaceByType(@Path("type") type: String): Response<List<VivacPlaceResponse>>
}