package com.example.vivacventuresmobile.ui.screens.listplaces

sealed class ListPlacesEvent {
    class GetVivacPlaces() : ListPlacesEvent()
    class GetVivacPlacesByType(val type: String) : ListPlacesEvent()
    class SearchPlaces(val query: String) : ListPlacesEvent()
    class SaveUsername(val username: String) : ListPlacesEvent()
    object ErrorVisto : ListPlacesEvent()
}