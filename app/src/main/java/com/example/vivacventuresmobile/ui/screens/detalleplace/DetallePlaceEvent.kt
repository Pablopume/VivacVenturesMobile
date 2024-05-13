package com.example.vivacventuresmobile.ui.screens.detalleplace

sealed class DetallePlaceEvent {
    class GetDetalle(val id: Int) : DetallePlaceEvent()
    class SaveUsernameAndId(val username: String, val vivacId: Int) : DetallePlaceEvent()
    class AddFavourite() : DetallePlaceEvent()
    class DeleteFavourite() : DetallePlaceEvent()
    object ErrorVisto : DetallePlaceEvent()
}