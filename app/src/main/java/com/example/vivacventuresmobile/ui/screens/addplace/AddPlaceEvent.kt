package com.example.vivacventuresmobile.ui.screens.addplace

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate

sealed class AddPlaceEvent {
    class AddPlace() : AddPlaceEvent()
    object ErrorVisto : AddPlaceEvent()
    class OnNameChange(val placeName: String) : AddPlaceEvent()
    class OnDescriptionChange(val placeDescription: String) : AddPlaceEvent()
    class OnTypeChange(val type: String) : AddPlaceEvent()
    class OnDateChange(val date: LocalDate) : AddPlaceEvent()
    class OnCapacityChange(val capacity: Int) : AddPlaceEvent()
    class OnPriceChange(val price: String) : AddPlaceEvent()
    class OnLocationChange(val location: LatLng) : AddPlaceEvent()
    class AddUsername(val userName: String , val int : Int) : AddPlaceEvent()
    class AddUri(val pictures: List<Uri>) : AddPlaceEvent()
    class DeleteUri(val stringimage: String, val imagen : Boolean) : AddPlaceEvent()
    class DetailsCompleted() : AddPlaceEvent()
    class LocationCompleted() : AddPlaceEvent()
    class Vuelta() : AddPlaceEvent()
    class VueltaLocation() : AddPlaceEvent()
    class UpdatePlace() : AddPlaceEvent()
    class ChangeExists(val int: Int) : AddPlaceEvent()
}