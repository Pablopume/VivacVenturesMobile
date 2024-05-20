package com.example.vivacventuresmobile.data.model

import com.example.vivacventuresmobile.domain.modelo.Valoration
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class ValorationResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val user: String,
    @SerializedName("vivacPlaceId")
    val vivacPlaceId: Int,
    @SerializedName("score")
    val score: Int,
    @SerializedName("review")
    val review: String,
//    @SerializedName("date")
//    val date: LocalDate
)
fun ValorationResponse.toValoration() : Valoration = Valoration(id, user, vivacPlaceId, score, review, LocalDate.now())
fun Valoration.toValorationResponse() : ValorationResponse = ValorationResponse(id, user, vivacPlaceId, score, review)
