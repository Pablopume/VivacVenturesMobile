package com.example.vivacventuresmobile.data.preferences

import kotlinx.serialization.Serializable
@Serializable
data class AppPreferences(
    val username : String = "",
    val password : String = "",
)