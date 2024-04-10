package com.example.vivacventuresmobile.ui.screens.listplaces

sealed class ListPlacesEvent {
    class GetVivacPlaces() : ListPlacesEvent()
    object ErrorVisto : ListPlacesEvent()
}