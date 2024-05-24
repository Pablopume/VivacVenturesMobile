package com.example.vivacventuresmobile.ui.screens.myfriends

import com.example.vivacventuresmobile.domain.modelo.Friend
import com.example.vivacventuresmobile.domain.modelo.VivacPlaceList

data class MyFriendsState(
    val friends: List<Friend> = emptyList(),
    val username: String = "",
    val error: String? = null,
    val loading: Boolean = false,
)