package com.example.vivacventuresmobile.ui.screens.map

import com.google.android.gms.maps.model.LatLng

sealed class MapEvent {
    object ToggleFalloutMap : MapEvent()
    data class OnMapLongClick(val latLng: LatLng) : MapEvent()
//    data class OnInfoWindowLongClick(val place: SleepingPlace) : MapEvent()
    data class HandleLocationUpdate(val latLng: LatLng): MapEvent()
    object LocationOff: MapEvent()
    object ErrorVisto : MapEvent()
}