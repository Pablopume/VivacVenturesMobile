package com.example.vivacventuresmobile.ui.screens.myfriends

import com.example.vivacventuresmobile.domain.modelo.VivacPlaceList

data class MyFriendsState(
    val vivacPlaces: List<VivacPlaceList> = emptyList(),
    val username: String = "",
    val error: String? = null,
    val loading: Boolean = false,
)