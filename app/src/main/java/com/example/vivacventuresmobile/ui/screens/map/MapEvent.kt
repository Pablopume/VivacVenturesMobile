package com.example.vivacventuresmobile.ui.screens.map

import com.google.android.gms.maps.model.LatLng

sealed class MapEvent {
    object ToggleDarkMap : MapEvent()
    data class OnMapLongClick(val latLng: LatLng) : MapEvent()
    data class HandleLocationUpdate(val latLng: LatLng): MapEvent()
    data class UpdateCameraPosition(val latLng: LatLng): MapEvent()
    object LocationOff: MapEvent()
    object ErrorVisto : MapEvent()
}