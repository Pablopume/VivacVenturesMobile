package com.example.vivacventuresmobile.ui.screens.searchusers

import com.example.vivacventuresmobile.domain.modelo.Friend

data class SearchUsersState(
    val friend: Friend = Friend(),
    val search: String = "",
    val username: String = "",
    val error: String? = null,
    val loading: Boolean = false,
)