package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.data.model.FavouritesVivacPlaceResponse
import com.example.vivacventuresmobile.data.model.VivacPlaceResponse
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface VivacPlacesService {
    @GET("/vivacplaces")
    suspend fun getVivacPlaces(): Response<List<VivacPlaceResponse>>

    @GET("/vivacplaces/id/{id}")
    suspend fun getVivacPlace(@Path("id") id: Int): Response<VivacPlaceResponse>

    @GET("/vivacplaces/user/{username}")
    suspend fun getVivacPlacesByUsername(@Path("username") username: String): Response<List<FavouritesVivacPlaceResponse>>

    @GET("/vivacplaces")
    suspend fun getVivacPlacesWithFavourites(): Response<List<FavouritesVivacPlaceResponse>>

    @GET("/vivacplaces/type/{type}")
    suspend fun getVivacPlaceByType(@Path("type") type: String): Response<List<FavouritesVivacPlaceResponse>>

    @GET("/vivacplaces/id/{id}")
    suspend fun getVivacPlaceById(@Path("id") id: Int): Response<VivacPlaceResponse>

    @POST("/vivacplace")
    @Headers("Content-Type: application/json")
    suspend fun saveVivacPlace(@Body vivacPlace: VivacPlaceResponse): Response<VivacPlaceResponse>

    @PUT("/vivacplace")
    @Headers("Content-Type: application/json")
    suspend fun updateVivacPlace(@Body vivacPlace: VivacPlaceResponse): Response<Boolean>

    @GET("/nearby")
    suspend fun getNearbyPlaces(@Query("latitude") latitude: Double, @Query("longitude") longitude: Double): Response<List<FavouritesVivacPlaceResponse>>

    @DELETE("/delete/{id}")
    suspend fun deleteVivacPlace(@Path("id") id: Int): Response<Unit>
}