package com.example.vivacventuresmobile.data.model

import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.google.gson.annotations.SerializedName
import java.time.LocalDate


data class VivacPlaceResponse (
    @SerializedName("id")
    val id : Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("username")
    val username: String?,
    @SerializedName("capacity")
    val capacity : Int,
    @SerializedName("date")
    val date: LocalDate?,
    @SerializedName("valorations")
    val valorations: List<ValorationResponse>? = null,
    @SerializedName("type")
    val type: String
)

fun VivacPlaceResponse.toVivacPlace() : VivacPlace = VivacPlace(id, name, description, latitude, longitude, username, capacity, date, valorations?.map { it.toValoration() }, type)