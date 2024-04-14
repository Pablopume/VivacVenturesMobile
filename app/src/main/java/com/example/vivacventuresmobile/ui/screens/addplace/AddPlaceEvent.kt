package com.example.vivacventuresmobile.ui.screens.addplace

sealed class AddPlaceEvent {
    class AddPlace(): AddPlaceEvent()
    object ErrorVisto : AddPlaceEvent()
    class OnNameChange(val placeName: String) : AddPlaceEvent()
    class OnDescriptionChange(val placeDescription: String) : AddPlaceEvent()

}