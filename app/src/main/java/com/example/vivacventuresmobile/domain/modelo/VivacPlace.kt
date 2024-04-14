package com.example.vivacventuresmobile.domain.modelo

import java.time.LocalDate

data class VivacPlace(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val username: String = "",
    val capacity: Int = 0,
    val date: LocalDate = LocalDate.now(),
    val valorations: List<Valoration> = emptyList(),
    val type: String = "",
)
