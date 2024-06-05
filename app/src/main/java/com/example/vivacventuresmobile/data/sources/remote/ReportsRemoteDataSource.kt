package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.BuildConfig
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.domain.modelo.Report
import com.example.vivacventuresmobile.utils.NetworkResult
import com.example.vivacventuresmobile.utils.StringProvider
import javax.inject.Inject

class ReportsRemoteDataSource @Inject constructor(
    private val reportsService: ReportsService,
    private val stringProvider: StringProvider,
) {
    suspend fun saveReport(report: Report): NetworkResult<Unit> {
        return try {
            val response = reportsService.saveReport(report)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                return if (BuildConfig.FLAVOR == Constantes.DEVELOPMENT) {
                    NetworkResult.Error("${response.code()} ${response.errorBody()}")
                } else {
                    NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
                }
            }
        } catch (e: Exception) {
            NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
        }
    }
}