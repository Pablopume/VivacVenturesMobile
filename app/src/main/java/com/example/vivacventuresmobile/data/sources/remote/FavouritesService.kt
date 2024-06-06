package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.data.model.FavouritesVivacPlaceResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FavouritesService {

    @POST(Constantes.FAVORITO)
    @Headers(Constantes.CONTENT_TYPE)
    suspend fun saveFavorito(
        @Query(Constantes.USERNAME) username: String,
        @Query(Constantes.VIVACID) vivacId: Int
    ): Response<Unit>

    @GET(Constantes.FAVORITOS)
    suspend fun getFavoritos(@Path(Constantes.USERNAME) username: String): Response<List<FavouritesVivacPlaceResponse>>

    @DELETE(Constantes.DELETE_FAVORITO)
    @Headers(Constantes.CONTENT_TYPE)
    suspend fun deleteFavorito(
        @Query(Constantes.USERNAME) username: String,
        @Query(Constantes.VIVACID) vivacId: Int
    ): Response<Unit>
}