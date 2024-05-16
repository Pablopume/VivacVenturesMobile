package com.example.vivacventuresmobile.ui.screens.addplace

import android.net.Uri
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import java.time.LocalDate

sealed class AddPlaceEvent {
    class AddPlace() : AddPlaceEvent()
    object ErrorVisto : AddPlaceEvent()
    class OnNameChange(val placeName: String) : AddPlaceEvent()
    class OnDescriptionChange(val placeDescription: String) : AddPlaceEvent()
    class OnPicturesChange(val pictures: List<Uri>) : AddPlaceEvent()
    class OnTypeChange(val type: String) : AddPlaceEvent()
    class OnDateChange(val date: LocalDate) : AddPlaceEvent()
    class OnCapacityChange(val capacity: Int) : AddPlaceEvent()
    class OnPriceChange(val price: String) : AddPlaceEvent()
    class AddUsername(val userName: String) : AddPlaceEvent()
    class AddUri(val pictures: List<Uri>) : AddPlaceEvent()
    class DeleteUri(val num: Int) : AddPlaceEvent()
    class DetailsCompleted() : AddPlaceEvent()
    class Vuelta() : AddPlaceEvent()
    class UpdatePlace() : AddPlaceEvent()
    class ChangeExists(val boolean: String) : AddPlaceEvent()
}