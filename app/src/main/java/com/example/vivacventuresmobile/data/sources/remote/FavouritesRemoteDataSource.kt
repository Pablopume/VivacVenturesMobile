package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.utils.NetworkResult
import com.example.vivacventuresmobile.data.model.toVivacPlace
import com.example.vivacventuresmobile.data.model.toVivacPlaceResponse
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
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

        suspend fun getFavourites(username: String): NetworkResult<List<VivacPlace>> {
            return try {
                val response = favouritesService.getFavoritos(username)
                if (response.isSuccessful) {
                    NetworkResult.Success(response.body()!!.map { it.toVivacPlace() })
                } else {
                    NetworkResult.Error(response.message())
                }
            } catch (e: Exception) {
                NetworkResult.Error(e.message ?: "An error occurred")
            }
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