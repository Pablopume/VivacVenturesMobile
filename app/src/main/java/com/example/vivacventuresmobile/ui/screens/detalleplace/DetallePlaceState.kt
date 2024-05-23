package com.example.vivacventuresmobile.ui.screens.detalleplace

import com.example.vivacventuresmobile.domain.modelo.VivacPlace

data class DetallePlaceState(
    val vivacPlace: VivacPlace? = null,
    val username: String = "",
    val deleted: Boolean = false,
    val score: Int = 0,
    val reviewValoration: String = "",
    val descriptionReport: String = "",
    val error: String? = null,
    val loading: Boolean = false,
)