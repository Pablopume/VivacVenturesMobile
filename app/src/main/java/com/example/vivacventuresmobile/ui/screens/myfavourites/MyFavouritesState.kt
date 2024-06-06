package com.example.vivacventuresmobile.ui.screens.myfavourites

import com.example.vivacventuresmobile.domain.modelo.ListFavs

data class MyFavouritesState(
    val list: ListFavs = ListFavs(),
    val listId: Int = 0,
    val username: String = "",
    val sharedWith: List<String> = emptyList(),
    val friends: List<String> = emptyList(),
    val listDeleted: Boolean = false,
    val firstTime: Boolean = true,
    val error: String? = null,
    val loading: Boolean = false,
)