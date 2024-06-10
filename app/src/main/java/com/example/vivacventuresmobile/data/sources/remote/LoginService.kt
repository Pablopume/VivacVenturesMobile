package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.data.model.LoginToken
import com.example.vivacventuresmobile.domain.modelo.Credentials
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface LoginService {
    @POST(Constantes.REGISTER_PATH)
    suspend fun register(@Body credentials: Credentials): Response<Unit>

    @GET(Constantes.LOGIN_PATH)
    suspend fun login(@Query(Constantes.USERNAME) username: String, @Query(Constantes.PASSWORD)password: String): Response<LoginToken>

    @GET(Constantes.REFRESH_PATH)
    suspend fun refreshToken(@Query(Constantes.TOKEN) token: String): Response<LoginToken>

    @PUT(Constantes.FORGOTPASSWORDPATH)
    suspend fun forgotPassword(@Query(Constantes.EMAIL) email: String): Response<Unit>

    @PUT(Constantes.RESETPASS)
    suspend fun resetPassword(@Query(Constantes.EMAIL) email: String, @Query(Constantes.NEWPASSWORD) newPassword: String, @Query(Constantes.TEMPORALPASSWORD) temporalPassword: String): Response<Unit>
}