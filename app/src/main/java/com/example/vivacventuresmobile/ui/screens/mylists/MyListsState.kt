package com.example.vivacventuresmobile.ui.screens.mylists

import com.example.vivacventuresmobile.domain.modelo.ListFavs

data class MyListsState(
    val list: List<ListFavs> = emptyList(),
    val username: String = "",
    val nameList: String = "",
    val error: String? = null,
    val loading: Boolean = false,
)