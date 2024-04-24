package com.example.vivacventuresmobile.data.model

import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.google.gson.annotations.SerializedName
import java.time.LocalDate


data class VivacPlaceResponse(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("latitude")
    val latitude: Double = 0.0,
    @SerializedName("longitude")
    val longitude: Double = 0.0,
    @SerializedName("username")
    val username: String = "",
    @SerializedName("capacity")
    val capacity: Int = 0,
    @SerializedName("date")
    val date: LocalDate = LocalDate.now(),
    @SerializedName("valorations")
    val valorations: List<ValorationResponse> = emptyList(),
    @SerializedName("type")
    val type: String = "",
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("images")
    val images: List<String> = emptyList(),
)

fun VivacPlaceResponse.toVivacPlace(): VivacPlace = VivacPlace(
    id,
    name,
    description,
    latitude,
    longitude,
    username,
    capacity,
    date,
    valorations.map { it.toValoration() },
    type,
    price,
    images
)

fun VivacPlace.toVivacPlaceResponse(): VivacPlaceResponse = VivacPlaceResponse(
    id,
    name,
    description,
    lat,
    lon,
    username,
    capacity,
    date,
    valorations.map { it.toValorationResponse() },
    type,
    price,
    images
)