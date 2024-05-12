package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.utils.NetworkResult
import com.example.vivacventuresmobile.data.model.toVivacPlace
import com.example.vivacventuresmobile.data.model.toVivacPlaceList
import com.example.vivacventuresmobile.data.model.toVivacPlaceResponse
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.example.vivacventuresmobile.domain.modelo.VivacPlaceList
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class FavouritesRemoteDataSource @Inject constructor(
    private val favouritesService: FavouritesService
) {

        suspend fun saveFavourite(username: String, vivacId: Int): NetworkResult<Unit> {
            return try {
                val response = favouritesService.saveFavorito(username, vivacId)
                if (response.isSuccessful) {
                    NetworkResult.Success(Unit)
                } else {
                    NetworkResult.Error(response.message())
                }
            } catch (e: Exception) {
                NetworkResult.Error(e.message ?: "An error occurred")
            }
        }

        suspend fun getFavourites(username: String): NetworkResult<List<VivacPlaceList>> {
            try {
                val response = favouritesService.getFavoritos(username)
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        return NetworkResult.Success(body.map { it.toVivacPlaceList() })
                    }
                } else {
                    return NetworkResult.Error(response.message())
                }
            } catch (e: Exception) {
                return NetworkResult.Error(e.message ?: "An error occurred")
            }
            return NetworkResult.Error("Error")
        }

        suspend fun deleteFavourite(username: String, vivacId: Int): NetworkResult<Unit> {
            return try {
                val response = favouritesService.deleteFavorito(username, vivacId)
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