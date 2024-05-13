package com.example.vivacventuresmobile.data.repositories

import com.example.vivacventuresmobile.utils.NetworkResult
import com.example.vivacventuresmobile.data.sources.remote.VivacPlacesRemoteDataSource
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.example.vivacventuresmobile.domain.modelo.VivacPlaceList
import com.google.android.gms.maps.model.LatLng
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

    fun getVivacPlacesWithFavourites(username: String): Flow<NetworkResult<List<VivacPlaceList>>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.getVivacPlacesWithFavourites(username)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun getVivacPlace(id: Int, username: String): Flow<NetworkResult<VivacPlace>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.getVivacPlace(id, username)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun getVivacPlacesByUsername(username: String): Flow<NetworkResult<List<VivacPlaceList>>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.getVivacPlacesByUsername(username)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun getVivacPlaceByType(type: String, username: String): Flow<NetworkResult<List<VivacPlaceList>>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.getVivacPlaceByType(type, username)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun getNearbyPlaces(latLng: LatLng, username: String): Flow<NetworkResult<List<VivacPlaceList>>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.getNearbyPlaces(latLng, username)
            emit(result)
        }.flowOn(dispatcher)
    }

    fun addVivacPlace(vivacPlace: VivacPlace): Flow<NetworkResult<VivacPlace>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = remoteDataSource.addVivacPlace(vivacPlace)
            emit(result)
        }.flowOn(dispatcher)
    }

}