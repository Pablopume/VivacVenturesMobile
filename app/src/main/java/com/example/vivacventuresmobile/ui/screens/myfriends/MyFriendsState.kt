package com.example.vivacventuresmobile.ui.screens.myfriends

import com.example.vivacventuresmobile.domain.modelo.FriendRequest

data class MyFriendsState(
    val friends: List<FriendRequest> = emptyList(),
    val pendingFriends: List<FriendRequest> = emptyList(),
    val username: String = "",
    val error: String? = null,
    val loading: Boolean = false,
)