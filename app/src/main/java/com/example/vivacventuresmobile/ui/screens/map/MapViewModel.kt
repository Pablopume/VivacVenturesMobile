package com.example.vivacventuresmobile.ui.screens.map

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MapViewModel @Inject constructor(

) : ViewModel(){
    private val _uiState: MutableStateFlow<MapState> by lazy {
        MutableStateFlow(MapState())
    }
    val uiState: MutableStateFlow<MapState> = _uiState

    init {
        _uiState.value = MapState(
            error = null,
            loading = false
        )
    }

    fun handleEvent(event: MapEvent) {
        when (event) {
            MapEvent.ErrorVisto -> _uiState.value = _uiState.value.copy(error = null)
            is MapEvent.HandleLocationUpdate -> TODO()
            MapEvent.LocationOff -> TODO()
            is MapEvent.OnMapLongClick -> TODO()
            MapEvent.ToggleFalloutMap -> TODO()
        }
    }
}