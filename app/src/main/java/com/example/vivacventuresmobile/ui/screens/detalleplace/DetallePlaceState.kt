package com.example.vivacventuresmobile.ui.screens.detalleplace

import com.example.vivacventuresmobile.domain.modelo.VivacPlace

data class DetallePlaceState(
    val vivacPlace: VivacPlace? = null,
    val username: String = "",
    val error: String? = null,
    val loading: Boolean = false,
)