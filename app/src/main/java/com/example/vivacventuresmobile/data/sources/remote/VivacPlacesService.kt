package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.data.model.VivacPlaceResponse
import retrofit2.Response
import retrofit2.http.GET

interface VivacPlacesService {
    @GET("/vivacplaces")
    suspend fun getVivacPlaces(): Response<List<VivacPlaceResponse>>
}