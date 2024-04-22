package com.example.vivacventuresmobile.data.sources.remote

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.vivacventuresmobile.common.Constantes
import dagger.hilt.android.qualifiers.ApplicationContext
import com.example.vivacventuresmobile.data.sources.remote.di.NetworkModule.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject



class TokenManager @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        private val refreshtoken = stringPreferencesKey(Constantes.REFRESHTOKEN)
        private val accessToken = stringPreferencesKey(Constantes.ACCESS_TOKEN)
    }

    fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[refreshtoken]
        }
    }

    suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[refreshtoken] = token
        }
    }

    fun getAccessToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[accessToken]
        }
    }

    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[accessToken] = token
        }
    }
}