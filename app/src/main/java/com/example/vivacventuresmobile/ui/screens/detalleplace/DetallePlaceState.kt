package com.example.vivacventuresmobile.ui.screens.detalleplace

import com.example.vivacventuresmobile.domain.modelo.ListFavs
import com.example.vivacventuresmobile.domain.modelo.VivacPlace

data class DetallePlaceState(
    val vivacPlace: VivacPlace? = null,
    val username: String = "",
    val deleted: Boolean = false,
    val score: Int = 5,
    val reviewValoration: String = "",
    val descriptionReport: String = "",
    val listsUser: List<ListFavs> = emptyList(),
    val listsVivacPlace: List<ListFavs> = emptyList(),
    val error: String? = null,
    val loading: Boolean = false,
)