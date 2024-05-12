package com.example.vivacventuresmobile.domain.modelo


data class VivacPlaceList(
    val id: Int = 0,
    val name: String = "",
    val type: String = "",
    val valorations: Double = 0.0,
    val images: String = "",
    val favorite: Boolean = false
)
