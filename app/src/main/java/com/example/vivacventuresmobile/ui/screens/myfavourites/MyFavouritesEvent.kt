package com.example.vivacventuresmobile.ui.screens.myfavourites

sealed class MyFavouritesEvent {
    class GetVivacPlaces() : MyFavouritesEvent()
    class SaveUsername(val username: String) : MyFavouritesEvent()
    object ErrorVisto : MyFavouritesEvent()
}