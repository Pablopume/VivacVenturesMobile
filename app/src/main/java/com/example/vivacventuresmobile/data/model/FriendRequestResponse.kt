package com.example.vivacventuresmobile.data.model

import com.example.vivacventuresmobile.domain.modelo.Friend
import com.example.vivacventuresmobile.domain.modelo.FriendRequest
import com.google.gson.annotations.SerializedName

data class FriendRequestResponse(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("requester")
    val requester: String = "",
    @SerializedName("requested")
    val requested: String = "",
    @SerializedName("status")
    val status: Boolean = false,
)

fun FriendRequestResponse.toFriendRequest(): FriendRequest {
    return FriendRequest(
        id = id,
        requester = requester,
        requested = requested,
        status = status,
    )
}

fun FriendRequest.toFriendRequestResponse(): FriendRequestResponse {
    return FriendRequestResponse(
        id = id,
        requester = requester,
        requested = requested,
        status = status,
    )
}

