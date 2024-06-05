package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.BuildConfig
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.utils.NetworkResult
import com.example.vivacventuresmobile.data.model.toVivacPlace
import com.example.vivacventuresmobile.data.model.toVivacPlaceList
import com.example.vivacventuresmobile.data.model.toVivacPlaceResponse
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.example.vivacventuresmobile.domain.modelo.VivacPlaceList
import com.example.vivacventuresmobile.utils.StringProvider
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class FavouritesRemoteDataSource @Inject constructor(
    private val favouritesService: FavouritesService,
    private val stringProvider: StringProvider,
) {

        suspend fun saveFavourite(username: String, vivacId: Int): NetworkResult<Unit> {
            return try {
                val response = favouritesService.saveFavorito(username, vivacId)
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
                NetworkResult.Error(e.message ?: stringProvider.getString(R.string.error_occurred))
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
                    return if (BuildConfig.FLAVOR == Constantes.DEVELOPMENT) {
                        NetworkResult.Error("${response.code()} ${response.errorBody()}")
                    } else {
                        NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
                    }
                }
            } catch (e: Exception) {
                return NetworkResult.Error(e.message ?: stringProvider.getString(R.string.error_occurred))
            }
            return NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
        }

        suspend fun deleteFavourite(username: String, vivacId: Int): NetworkResult<Unit> {
            return try {
                val response = favouritesService.deleteFavorito(username, vivacId)
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
                NetworkResult.Error(e.message ?: stringProvider.getString(R.string.error_occurred))
            }
        }
}