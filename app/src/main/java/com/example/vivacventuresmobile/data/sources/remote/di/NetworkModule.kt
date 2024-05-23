package com.example.vivacventuresmobile.data.sources.remote.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.data.sources.remote.AuthAuthenticator
import com.example.vivacventuresmobile.data.sources.remote.AuthInterceptor
import com.example.vivacventuresmobile.data.sources.remote.FavouritesService
import com.example.vivacventuresmobile.data.sources.remote.LoginService
import com.example.vivacventuresmobile.data.sources.remote.ReportsService
import com.example.vivacventuresmobile.data.sources.remote.ValorationsService
import com.example.vivacventuresmobile.data.sources.remote.VivacPlacesService
import com.example.vivacventuresmobile.utils.Constants.BASE_URL
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .authenticator(authAuthenticator)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterMoshiFactory(): MoshiConverterFactory {
        val moshi = Moshi.Builder()
            .add(LocalDateAdapter())
            .build()
        return MoshiConverterFactory.create(moshi)
    }

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constantes.DATA_STORE)

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideLoginService(retrofit: Retrofit): LoginService =
        retrofit.create(LoginService::class.java)

    @Singleton
    @Provides
    fun provideVivacPlacesService(retrofit: Retrofit): VivacPlacesService =
        retrofit.create(VivacPlacesService::class.java)

    @Singleton
    @Provides
    fun provideFavouritesService(retrofit: Retrofit): FavouritesService =
        retrofit.create(FavouritesService::class.java)

    @Singleton
    @Provides
    fun provideValorationService(retrofit: Retrofit): ValorationsService =
        retrofit.create(ValorationsService::class.java)

    @Singleton
    @Provides
    fun provideReportService(retrofit: Retrofit): ReportsService =
        retrofit.create(ReportsService::class.java)


    @Singleton
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

}


class LocalDateAdapter {
    @ToJson
    fun toJson(value: LocalDate): String {
        return value.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @FromJson
    fun fromJson(value: String): LocalDate {
        return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
    }
}
