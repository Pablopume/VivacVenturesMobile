package com.example.vivacventuresmobile.ui.screens.map

import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties

data class MapState(
    val vivacPlaces: List<VivacPlace> = emptyList(),
    val properties: MapProperties = MapProperties(),
    val error: String? = null,
    val loading: Boolean = false,
    val isLocationEnabled: Boolean = false,
    val currentLocation: LatLng? = null,
//    val cameraPositionState: CameraPositionState = CameraPositionState(),
    val currentLatLng: LatLng = LatLng(0.toDouble(), 0.toDouble()),
    val isDarkMap: Boolean = false,
)