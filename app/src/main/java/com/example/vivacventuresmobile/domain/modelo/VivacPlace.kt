package com.example.vivacventuresmobile.domain.modelo

import java.time.LocalDate

data class VivacPlace(
    val id: Int,
    val name: String,
    val description: String?,
    val lat: Double,
    val lon: Double,
    val username: String?,
    val capacity: Int,
    val date: LocalDate?,
    val valorations: List<Valoration>? = null,
    val type: String?,
)
