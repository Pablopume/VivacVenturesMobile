package com.example.vivacventuresmobile.ui.screens.listplaces

import com.example.vivacventuresmobile.domain.modelo.VivacPlaceList

data class ListPlacesState(
    val vivacPlaces: List<VivacPlaceList> = emptyList(),
    val username: String = "",
    val lastCall: Int = 0,
    val error: String? = null,
    val loading: Boolean = false,
)