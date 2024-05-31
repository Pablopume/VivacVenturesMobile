package com.example.vivacventuresmobile.data.sources.remote

import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.data.model.FavouritesVivacPlaceResponse
import com.example.vivacventuresmobile.data.model.VivacPlaceResponse
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface VivacPlacesService {
    @GET(Constantes.VIVACPLACES)
    suspend fun getVivacPlaces(): Response<List<VivacPlaceResponse>>

    @GET(Constantes.VIVACPLACES_ID)
    suspend fun getVivacPlace(@Path(Constantes.ID) id: Int): Response<VivacPlaceResponse>

    @GET(Constantes.VIVACPLACES_USER)
    suspend fun getVivacPlacesByUsername(@Path(Constantes.USERNAME) username: String): Response<List<FavouritesVivacPlaceResponse>>

    @GET(Constantes.VIVACPLACESMY)
    suspend fun getVivacPlacesWithFavourites(): Response<List<FavouritesVivacPlaceResponse>>

    @GET(Constantes.VIVACPLACES_TYPE)
    suspend fun getVivacPlaceByType(@Path(Constantes.TYPE) type: String): Response<List<FavouritesVivacPlaceResponse>>

    @POST(Constantes.VIVACPLACE)
    @Headers(Constantes.CONTENT_TYPE)
    suspend fun saveVivacPlace(@Body vivacPlace: VivacPlaceResponse): Response<VivacPlaceResponse>

    @PUT(Constantes.VIVACPLACE)
    @Headers(Constantes.CONTENT_TYPE)
    suspend fun updateVivacPlace(@Body vivacPlace: VivacPlaceResponse): Response<Boolean>

    @GET(Constantes.NEARBY)
    suspend fun getNearbyPlaces(@Query(Constantes.LATITUDE) latitude: Double, @Query(Constantes.LONGITUDE) longitude: Double): Response<List<FavouritesVivacPlaceResponse>>

    @DELETE(Constantes.DELETE)
    suspend fun deleteVivacPlace(@Path(Constantes.ID) id: Int): Response<Unit>
}