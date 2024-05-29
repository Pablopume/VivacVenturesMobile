package com.example.vivacventuresmobile.ui.screens.mylists

sealed class MyListsEvent {
    class GetLists() : MyListsEvent()
    class CreateList() : MyListsEvent()
    class OnNameChanged(val name: String) : MyListsEvent()
    class SaveUsername(val username: String) : MyListsEvent()
    object ErrorVisto : MyListsEvent()
}