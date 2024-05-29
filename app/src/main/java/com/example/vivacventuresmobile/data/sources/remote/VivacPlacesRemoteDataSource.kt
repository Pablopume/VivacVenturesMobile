package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.utils.NetworkResult
import com.example.vivacventuresmobile.data.model.toVivacPlace
import com.example.vivacventuresmobile.data.model.toVivacPlaceList
import com.example.vivacventuresmobile.data.model.toVivacPlaceResponse
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.example.vivacventuresmobile.domain.modelo.VivacPlaceList
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
                return NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }

    suspend fun getVivacPlacesWithFavourites(username: String): NetworkResult<List<VivacPlaceList>> {
        try {
            val response = vivacPlacesService.getVivacPlacesWithFavourites()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body.map { it.toVivacPlaceList() })
                }
            } else {
                return NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }

    suspend fun getVivacPlace(id: Int, username: String): NetworkResult<VivacPlace> {
        try {
            val response = vivacPlacesService.getVivacPlace(id)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body.toVivacPlace())
                }
            } else {
                return NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }

    suspend fun getVivacPlaceByType(type: String, username: String): NetworkResult<List<VivacPlaceList>> {
        try {
            val response = vivacPlacesService.getVivacPlaceByType(type)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body.map { it.toVivacPlaceList() })
                }
            } else {
                return NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }

    suspend fun addVivacPlace(vivacPlace: VivacPlace): NetworkResult<VivacPlace> {
        try {
            val response = vivacPlacesService.saveVivacPlace(vivacPlace.toVivacPlaceResponse())
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body.toVivacPlace())
                }
            } else {
                return NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }

    suspend fun getNearbyPlaces(latLong: LatLng, username: String): NetworkResult<List<VivacPlaceList>> {
        try {
            val response = vivacPlacesService.getNearbyPlaces(latLong.latitude, latLong.longitude)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body.map { it.toVivacPlaceList() })
                }
            } else {
                return NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }

    suspend fun getVivacPlacesByUsername(username: String): NetworkResult<List<VivacPlaceList>> {
        try {
            val response = vivacPlacesService.getVivacPlacesByUsername(username)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body.map { it.toVivacPlaceList() })
                }
            } else {
                return NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }
    suspend fun updateVivacPlace(vivacPlace: VivacPlace): NetworkResult<Boolean> {
        try {
            val response = vivacPlacesService.updateVivacPlace(vivacPlace.toVivacPlaceResponse())
            if (response.isSuccessful) {
                return NetworkResult.Success(true)
            } else {
                return NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }

    suspend fun deleteVivacPlace(id: Int): NetworkResult<Unit> {
        try {
            val response = vivacPlacesService.deleteVivacPlace(id)
            if (response.isSuccessful) {
                return NetworkResult.Success(Unit)
            } else {
                return NetworkResult.Error("${response.code()} ${response.errorBody()}")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
        return NetworkResult.Error("Error")
    }
}