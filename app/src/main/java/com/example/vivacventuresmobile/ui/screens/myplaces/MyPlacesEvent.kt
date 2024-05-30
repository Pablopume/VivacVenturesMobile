package com.example.vivacventuresmobile.ui.screens.myplaces

sealed class MyPlacesEvent {
    class GetVivacPlaces() : MyPlacesEvent()
    class DeleteVivacPlace(val id: Int) : MyPlacesEvent()
    class SaveUsername(val username: String) : MyPlacesEvent()
    object ErrorVisto : MyPlacesEvent()
}