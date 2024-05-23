package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.domain.modelo.Report
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ReportsService {
    @POST("/report")
    @Headers("Content-Type: application/json")
    suspend fun saveReport(@Body report: Report): Response<Unit>
}