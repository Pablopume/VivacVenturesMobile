package com.example.vivacventuresmobile.ui.screens.myplaces

sealed class MyFriendsEvent {
    class GetVivacPlaces() : MyFriendsEvent()
    class SaveUsername(val username: String) : MyFriendsEvent()
    object ErrorVisto : MyFriendsEvent()
}