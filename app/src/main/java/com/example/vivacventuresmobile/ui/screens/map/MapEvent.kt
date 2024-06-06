package com.example.vivacventuresmobile.ui.screens.map

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng

sealed class MapEvent {
    object ToggleDarkMap : MapEvent()
    data class OnMapLongClick(val latLng: LatLng) : MapEvent()
    data class UpdateCameraPosition(val latLng: LatLng) : MapEvent()
    data class StartLocationUpdates(val fusedLocationClient: FusedLocationProviderClient) :
        MapEvent()

    data class SendError(val error: String) : MapEvent()
    object GetAll : MapEvent()
    object LocationOn : MapEvent()
    object LocationOff : MapEvent()
    object ErrorVisto : MapEvent()

    class reLogin(val username: String, val password: String) : MapEvent()
}