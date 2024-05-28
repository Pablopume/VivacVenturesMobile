package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.data.model.ListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ListsService {
    @GET("/lists")
    suspend fun getLists(@Query("username") username: String): Response<List<ListResponse>>

    @GET("/list/{id}")
    suspend fun getList(@Path("id") id: Int): Response<ListResponse>

    @GET("/lists/shared")
    suspend fun getListSharedWith(@Query("id") id: Int): Response<List<String>>

    @POST("/list")
    suspend fun saveList(@Body listResponse: ListResponse): Response<Unit>

    @POST("/list/share")
    suspend fun shareList(@Query("id") id: Int, @Query("username") username: String): Response<Unit>

    @DELETE("/list/delete/shared")
    suspend fun deleteSharedList(@Query("id") id: Int, @Query("username") username: String): Response<Unit>

    @DELETE("/list/delete")
    suspend fun deleteList(@Path("id") id: Int): Response<Unit>

    @POST("/list/favorite")
    suspend fun addFavoriteToList(@Query("id") id: Int, @Query("vivacId") vivacId: Int): Response<Unit>

    @DELETE("/list/favorite/delete")
    suspend fun removeFavoriteFromList(@Query("id") id: Int, @Query("vivacId") vivacId: Int): Response<Unit>
}