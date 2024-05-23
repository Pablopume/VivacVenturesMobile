package com.example.vivacventuresmobile.data.repositories

import com.example.vivacventuresmobile.data.sources.remote.ReportsRemoteDataSource
import com.example.vivacventuresmobile.domain.modelo.Report
import com.example.vivacventuresmobile.utils.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ReportsRepository @Inject constructor(
    private val remoteDataSource: ReportsRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun saveReport(report: Report): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.saveReport(report)
            emit(result)
        }.flowOn(dispatcher)
    }
}