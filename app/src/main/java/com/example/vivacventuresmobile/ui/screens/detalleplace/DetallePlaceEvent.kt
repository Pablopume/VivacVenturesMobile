package com.example.vivacventuresmobile.ui.screens.detalleplace

sealed class DetallePlaceEvent {
    class GetDetalle(val id: Int) : DetallePlaceEvent()
    object ErrorVisto : DetallePlaceEvent()
}