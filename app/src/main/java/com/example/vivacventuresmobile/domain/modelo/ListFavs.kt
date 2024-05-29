package com.example.vivacventuresmobile.domain.modelo

data class ListFavs (
    val id: Int = 0,
    val name: String = "",
    val username: String = "",
    val favoritos: List<VivacPlaceList> = emptyList(),
)