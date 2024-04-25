package com.example.vivacventuresmobile.ui.screens.addplace

import android.net.Uri
import com.example.vivacventuresmobile.domain.modelo.VivacPlace

data class AddPlaceState (
    val loading: Boolean = false,
    val error: String? = null,
    val addPlaceDone: Boolean = false,
    val place: VivacPlace = VivacPlace(),
    val uris : List<Uri> = emptyList()
)