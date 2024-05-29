package com.example.vivacventuresmobile.data.model

import com.example.vivacventuresmobile.domain.modelo.ListFavs
import com.google.gson.annotations.SerializedName

data class ListResponse(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("count")
    val username: String = "",
    @SerializedName("vivacPlaces")
    val vivacPlaces: List<FavouritesVivacPlaceResponse> = emptyList(),
)

fun ListResponse.toListFavs(): ListFavs {
    return ListFavs(
        id = id,
        name = name,
        username = username,
        favoritos = vivacPlaces.map { it.toVivacPlaceList() },
    )
}

fun ListFavs.toListResponse(): ListResponse {
    return ListResponse(
        id = id,
        name = name,
        username = username,
        vivacPlaces = favoritos.map { it.toFavouritesVivacPlaceResponse() },
    )
}
