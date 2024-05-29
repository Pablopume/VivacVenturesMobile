package com.example.vivacventuresmobile.data.model

import com.example.vivacventuresmobile.domain.modelo.VivacPlaceList
import com.google.gson.annotations.SerializedName


data class FavouritesVivacPlaceResponse(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("valorations")
    val valorations: Double = 0.0,
    @SerializedName("images")
    val images: String = "",
    @SerializedName("favorite")
    val favorite: Boolean = false
)

fun FavouritesVivacPlaceResponse.toVivacPlaceList(): VivacPlaceList = VivacPlaceList(
    id,
    name,
    type,
    valorations,
    images,
    favorite
)

fun VivacPlaceList.toFavouritesVivacPlaceResponse(): FavouritesVivacPlaceResponse = FavouritesVivacPlaceResponse(
    id,
    name,
    type,
    valorations,
    images,
    favorite
)