package com.example.vivacventuresmobile.ui.screens.myplaces

sealed class MyPlacesEvent {
    class GetVivacPlaces() : MyPlacesEvent()
    class SaveUsername(val username: String) : MyPlacesEvent()
    object ErrorVisto : MyPlacesEvent()
}