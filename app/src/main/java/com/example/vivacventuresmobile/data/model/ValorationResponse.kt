package com.example.vivacventuresmobile.data.model

import com.example.vivacventuresmobile.domain.modelo.Valoration
import com.google.gson.annotations.SerializedName

data class ValorationResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("user")
    val user: String,
    @SerializedName("vivacPlace")
    val vivacPlaceId: Int,
    @SerializedName("score")
    val score: Int,
    @SerializedName("review")
    val review: String,
)
fun ValorationResponse.toValoration() : Valoration = Valoration(id, user, vivacPlaceId, score, review)
