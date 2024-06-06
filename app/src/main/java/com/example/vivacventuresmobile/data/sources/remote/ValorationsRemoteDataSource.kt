package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.BuildConfig
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.domain.modelo.Valoration
import com.example.vivacventuresmobile.utils.NetworkResult
import com.example.vivacventuresmobile.utils.StringProvider
import javax.inject.Inject

class ValorationsRemoteDataSource @Inject constructor(
    private val valorationsService: ValorationsService,
    private val stringProvider: StringProvider,
) {
    suspend fun addValoration(valoration: Valoration): NetworkResult<Unit> {
        return try {
            val response = valorationsService.addValoration(valoration)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                if (response.code() == 400) {
                    NetworkResult.Error(stringProvider.getString(R.string.already_reviewed))
                } else {
                    return if (BuildConfig.FLAVOR == Constantes.DEVELOPMENT) {
                        NetworkResult.Error("${response.code()} ${response.errorBody()}")
                    } else {
                        NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
                    }
                }
            }
        } catch (e: Exception) {
            NetworkResult.Error(stringProvider.getString(R.string.error_occurred))
        }
    }

    suspend fun deleteValoration(id: Int): NetworkResult<Unit> {
        return try {
            val response = valorationsService.deleteValoration(id)
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