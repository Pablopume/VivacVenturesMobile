package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.utils.NetworkResult
import com.example.vivacventuresmobile.data.model.toVivacPlace
import com.example.vivacventuresmobile.data.model.toVivacPlaceList
import com.example.vivacventuresmobile.data.model.toVivacPlaceResponse
import com.example.vivacventuresmobile.domain.modelo.Valoration
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.example.vivacventuresmobile.domain.modelo.VivacPlaceList
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class ValorationsRemoteDataSource @Inject constructor(
    private val valorationsService: ValorationsService
) {
    suspend fun addValoration(valoration: Valoration): NetworkResult<Unit> {
        return try {
            val response = valorationsService.addValoration(valoration)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("${response.errorBody()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun deleteValoration(id: Int): NetworkResult<Unit> {
        return try {
            val response = valorationsService.deleteValoration(id)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("${response.errorBody()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "An error occurred")
        }
    }
}