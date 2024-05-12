package com.example.vivacventuresmobile.ui.screens.listplaces

import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.example.vivacventuresmobile.domain.modelo.VivacPlaceList
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties

data class ListPlacesState(
    val vivacPlaces: List<VivacPlaceList> = emptyList(),
    val username: String = "",
    val error: String? = null,
    val loading: Boolean = false,
)