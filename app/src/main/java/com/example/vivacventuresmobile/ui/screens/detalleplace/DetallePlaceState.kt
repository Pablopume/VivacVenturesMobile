package com.example.vivacventuresmobile.ui.screens.detalleplace

import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties

data class DetallePlaceState(
    val vivacPlace: VivacPlace? = null,
    val error: String? = null,
    val loading: Boolean = false,
)