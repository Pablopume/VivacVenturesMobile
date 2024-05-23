package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.domain.modelo.Report
import com.example.vivacventuresmobile.utils.NetworkResult
import javax.inject.Inject

class ReportsRemoteDataSource @Inject constructor(
    private val reportsService: ReportsService
) {
    suspend fun saveReport(report: Report): NetworkResult<Unit> {
        return try {
            val response = reportsService.saveReport(report)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error(response.message())
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "An error occurred")
        }
    }
}