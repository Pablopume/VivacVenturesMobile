package com.example.vivacventuresmobile.ui.screens.detalleplace

import com.example.vivacventuresmobile.ui.screens.myplaces.MyPlacesEvent

sealed class DetallePlaceEvent {
    class GetDetalle(val id: Int) : DetallePlaceEvent()
    class SaveUsername(val username: String) : DetallePlaceEvent()
    class AddFavourite() : DetallePlaceEvent()
    class DeleteFavourite() : DetallePlaceEvent()
    object ErrorVisto : DetallePlaceEvent()
}