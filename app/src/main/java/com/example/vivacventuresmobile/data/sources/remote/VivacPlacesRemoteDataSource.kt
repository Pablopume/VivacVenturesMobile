package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.utils.NetworkResult
import com.example.vivacventuresmobile.data.model.toVivacPlace
import com.example.vivacventuresmobile.data.model.toVivacPlaceResponse
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class VivacPlacesRemoteDataSource @Inject constructor(
    private val vivacPlacesService: VivacPlacesService
) {
    suspend fun getVivacPlaces(): NetworkResult<List<VivacPlace>> {
        try {
            val response = vivacPlacesService.getVivacPlaces()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body.map { it.toVivacPlace() })
                }
            } else {
                return NetworkResult.Error("Error")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }

    suspend fun getVivacPlace(id: Int): NetworkResult<VivacPlace> {
        try {
            val response = vivacPlacesService.getVivacPlace(id)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body.toVivacPlace())
                }
            } else {
                return NetworkResult.Error("Error")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }

    suspend fun getVivacPlaceByType(type: String): NetworkResult<List<VivacPlace>> {
        try {
            val response = vivacPlacesService.getVivacPlaceByType(type)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body.map { it.toVivacPlace() })
                }
            } else {
                return NetworkResult.Error("Error")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }

    suspend fun addVivacPlace(vivacPlace: VivacPlace): NetworkResult<VivacPlace> {
        try {
            val response = vivacPlacesService.saveVivacPlace(vivacPlace)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body.toVivacPlace())
                }
            } else {
                return NetworkResult.Error("Error")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }

    suspend fun getNearbyPlaces(latLong: LatLng): NetworkResult<List<VivacPlace>> {
        try {
            val response = vivacPlacesService.getNearbyPlaces(latLong.latitude, latLong.longitude)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body.map { it.toVivacPlace() })
                }
            } else {
                return NetworkResult.Error("Error")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }
}