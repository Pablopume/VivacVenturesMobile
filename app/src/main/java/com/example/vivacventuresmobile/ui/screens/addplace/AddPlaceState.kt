package com.example.vivacventuresmobile.ui.screens.addplace

import com.example.vivacventuresmobile.domain.modelo.VivacPlace

data class AddPlaceState (
    val loading: Boolean = false,
    val error: String? = null,
    val addPlaceDone: Boolean = false,
    val place: VivacPlace = VivacPlace()
)