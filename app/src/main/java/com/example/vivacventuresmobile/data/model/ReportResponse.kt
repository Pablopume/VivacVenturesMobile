package com.example.vivacventuresmobile.data.model

import com.example.vivacventuresmobile.domain.modelo.Report
import com.google.gson.annotations.SerializedName

data class ReportResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("vivacPlaceId")
    val vivacPlaceId: Int,
    @SerializedName("description")
    val description: String,
)

fun Report.toReportResponse(): ReportResponse = ReportResponse(id, username, vivacPlaceId, description)