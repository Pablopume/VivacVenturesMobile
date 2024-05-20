package com.example.vivacventuresmobile.domain.modelo

import java.time.LocalDate

data class Valoration(
    val id: Int = 0,
    val user: String = "",
    val vivacPlaceId: Int = 0,
    val score: Int = 0,
    val review: String = "",
    val date: LocalDate = LocalDate.MIN,
)
