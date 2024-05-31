package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.data.model.ListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ListsService {
    @GET(Constantes.LISTS)
    suspend fun getLists(@Query(Constantes.USERNAME) username: String): Response<List<ListResponse>>

    @GET(Constantes.LIST)
    suspend fun getList(@Path(Constantes.ID) id: Int): Response<ListResponse>

    @GET(Constantes.LIST_USER_VIVAC)
    suspend fun getListsByUserAndVivacPlace(@Query(Constantes.USERNAME) username: String, @Query(Constantes.VIVACPLACEID) id: Int): Response<List<ListResponse>>

    @GET(Constantes.LIST_SHARED)
    suspend fun getWhoIsListShareWith(@Query(Constantes.ID) id: Int): Response<List<String>>

    @POST(Constantes.LIST_SAVE)
    suspend fun saveList(@Body listResponse: ListResponse): Response<Unit>

    @POST(Constantes.LIST_SHARE)
    suspend fun shareList(@Query(Constantes.ID) id: Int, @Query(Constantes.USERNAME) username: String): Response<Unit>

    @DELETE(Constantes.LIST_DELETE_SHARED)
    suspend fun deleteSharedList(@Query(Constantes.ID) id: Int, @Query(Constantes.USERNAME) username: String): Response<Unit>

    @DELETE(Constantes.LIST_DELETE)
    suspend fun deleteList(@Query(Constantes.ID) id: Int): Response<Unit>

    @POST(Constantes.LIST_FAVORITE)
    suspend fun addFavoriteToList(@Query(Constantes.ID) id: Int, @Query(Constantes.VIVACID) vivacId: Int): Response<Unit>

    @DELETE(Constantes.LIST_FAVORITE_DELETE)
    suspend fun removeFavoriteFromList(@Query(Constantes.ID) id: Int, @Query(Constantes.VIVACID) vivacId: Int): Response<Unit>
}