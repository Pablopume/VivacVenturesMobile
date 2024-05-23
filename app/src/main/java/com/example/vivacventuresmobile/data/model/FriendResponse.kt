package com.example.vivacventuresmobile.data.model

import com.example.vivacventuresmobile.domain.modelo.Friend
import com.google.gson.annotations.SerializedName

data class FriendResponse(
    @SerializedName("username")
    val username: String = "",
    @SerializedName("count")
    val count: String = ""
)

fun FriendResponse.toFriend(): Friend = Friend(username, count)