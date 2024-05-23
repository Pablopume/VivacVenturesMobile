package com.example.vivacventuresmobile.ui.screens.detalleplace

sealed class DetallePlaceEvent {
    class GetDetalle(val id: Int) : DetallePlaceEvent()
    class SaveUsernameAndId(val username: String, val vivacId: Int) : DetallePlaceEvent()
    class DeleteValoration(val id: Int) : DetallePlaceEvent()
    class AddValoration() : DetallePlaceEvent()
    class AddReport() : DetallePlaceEvent()
    class AddFavourite() : DetallePlaceEvent()
    class DeleteFavourite() : DetallePlaceEvent()
    class DeletePlace() : DetallePlaceEvent()
    class OnScoreChange(val score: Int) : DetallePlaceEvent()
    class OnReviewValorationChange(val review: String) : DetallePlaceEvent()
    class OnDescriptionReportChange(val description: String) : DetallePlaceEvent()
    object ErrorVisto : DetallePlaceEvent()
}