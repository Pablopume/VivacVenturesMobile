package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.VivacPlacesRepository
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class GetVivacPlaceNearbyUseCase @Inject constructor(private val repository: VivacPlacesRepository){
    operator fun invoke(latLng: LatLng, username: String) = repository.getNearbyPlaces(latLng, username)
}