package com.example.vivacventuresmobile.domain.modelo

data class FriendRequest (
    val id: Int = 0,
    val requester: String = "",
    val requested: String = "",
    val status: Boolean = false,
)