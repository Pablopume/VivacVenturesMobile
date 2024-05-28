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
    @SerializedName("favoritos")
    val favoritos: List<VivacPlaceResponse> = emptyList(),
)

fun ListResponse.toListFavs(): ListFavs {
    return ListFavs(
        id = id,
        name = name,
        username = username,
        favoritos = favoritos.map { it.toVivacPlace() },
    )
}

fun ListFavs.toListResponse(): ListResponse {
    return ListResponse(
        id = id,
        name = name,
        username = username,
        favoritos = favoritos.map { it.toVivacPlaceResponse() },
    )
}
