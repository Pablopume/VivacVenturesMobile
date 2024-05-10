package com.example.vivacventuresmobile.ui.screens.myfavourites

import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties

data class MyFavouritesState(
    val vivacPlaces: List<VivacPlace> = emptyList(),
    val username: String = "",
    val error: String? = null,
    val loading: Boolean = false,
)