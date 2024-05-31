package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.domain.modelo.Valoration
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ValorationsService {

    @POST(Constantes.VALORATION)
    @Headers(Constantes.CONTENT_TYPE)
    suspend fun addValoration(@Body valoration: Valoration): Response<Unit>

    @DELETE(Constantes.VALORATION_ID)
    suspend fun deleteValoration(@Path(Constantes.ID) int: Int): Response<Unit>
}