package com.example.vivacventuresmobile.ui.screens.myfavourites

sealed class MyFavouritesEvent {
    class GetVivacPlaces() : MyFavouritesEvent()
    class DeleteList() : MyFavouritesEvent()
    class SaveListId(val listId: Int) : MyFavouritesEvent()
    class SaveUsername(val username: String) : MyFavouritesEvent()
    class ShareList(val username: String) : MyFavouritesEvent()
    class UnShareList(val username: String) : MyFavouritesEvent()
    object ErrorVisto : MyFavouritesEvent()
}