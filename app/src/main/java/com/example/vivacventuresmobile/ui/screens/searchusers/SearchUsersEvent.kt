package com.example.vivacventuresmobile.ui.screens.searchusers

sealed class SearchUsersEvent {
    class SaveUsername(val username: String) : SearchUsersEvent()
    class OnSearchChange(val search: String) : SearchUsersEvent()
    object DoSearch : SearchUsersEvent()
    object ErrorVisto : SearchUsersEvent()
}