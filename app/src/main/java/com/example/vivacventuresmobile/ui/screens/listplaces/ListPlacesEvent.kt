package com.example.vivacventuresmobile.ui.screens.listplaces

sealed class ListPlacesEvent {
    class GetVivacPlaces() : ListPlacesEvent()

    class GetVivacPlacesByType(val type: String) : ListPlacesEvent()
    object ErrorVisto : ListPlacesEvent()
}