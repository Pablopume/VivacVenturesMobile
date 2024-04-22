package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.common.Constantes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.getAccessToken().first()
        }
        val request = chain.request().newBuilder().header(Constantes.AUTHORIZATION, Constantes.BEARER+"$token").build()
        return chain.proceed(request)
    }
}