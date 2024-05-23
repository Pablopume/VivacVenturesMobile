package com.example.vivacventuresmobile.ui.screens.addLocation

sealed class AddLocationEvent {
    object ErrorVisto : AddLocationEvent()
    data class SendError(val error: String): AddLocationEvent()
}