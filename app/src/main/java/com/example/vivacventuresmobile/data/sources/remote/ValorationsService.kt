package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.domain.modelo.Valoration
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ValorationsService {

    @POST("/valoration")
    @Headers("Content-Type: application/json")
    suspend fun addValoration(@Body valoration: Valoration): Response<Unit>

    @DELETE("/valoration/{id}")
    suspend fun deleteValoration(@Query("id") int: Int): Response<Unit>
}