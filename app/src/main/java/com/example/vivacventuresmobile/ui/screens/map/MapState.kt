package com.example.vivacventuresmobile.ui.screens.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties

data class MapState(
//    val sleepingPlaces: List<SleepingPlace> = emptyList(),
    val properties: MapProperties = MapProperties(),
    val error: String? = null,
    val loading: Boolean = false,
    val isLocationEnabled: Boolean = false,
    val currentLocation: LatLng? = null,
    val cameraPositionState: CameraPositionState = CameraPositionState(),
    val isFalloutMap: Boolean = false,
)