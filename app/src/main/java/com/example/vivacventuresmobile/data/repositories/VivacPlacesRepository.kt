package com.example.vivacventuresmobile.data.repositories

import com.example.apollo_davidroldan.utils.NetworkResult
import com.example.vivacventuresmobile.data.sources.remote.VivacPlacesRemoteDataSource
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VivacPlacesRepository @Inject constructor(
    private val remoteDataSource: VivacPlacesRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun getVivacPlaces(): Flow<NetworkResult<List<VivacPlace>>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.getVivacPlaces()
            emit(result)
        }.flowOn(dispatcher)
    }

}