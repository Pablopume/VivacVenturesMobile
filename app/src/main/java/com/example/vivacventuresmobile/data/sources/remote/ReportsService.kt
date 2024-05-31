package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.domain.modelo.Report
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ReportsService {
    @POST(Constantes.REPORT)
    @Headers(Constantes.CONTENT_TYPE)
    suspend fun saveReport(@Body report: Report): Response<Unit>
}