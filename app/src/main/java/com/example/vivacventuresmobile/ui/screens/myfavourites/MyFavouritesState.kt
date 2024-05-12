package com.example.vivacventuresmobile.ui.screens.myfavourites

import com.example.vivacventuresmobile.domain.modelo.VivacPlaceList

data class MyFavouritesState(
    val vivacPlaces: List<VivacPlaceList> = emptyList(),
    val username: String = "",
    val error: String? = null,
    val loading: Boolean = false,
)