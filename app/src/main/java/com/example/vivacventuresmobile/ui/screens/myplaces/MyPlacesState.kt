package com.example.vivacventuresmobile.ui.screens.myplaces

import com.example.vivacventuresmobile.domain.modelo.VivacPlaceList

data class MyPlacesState(
    val vivacPlaces: List<VivacPlaceList> = emptyList(),
    val username: String = "",
    val error: String? = null,
    val loading: Boolean = false,
)