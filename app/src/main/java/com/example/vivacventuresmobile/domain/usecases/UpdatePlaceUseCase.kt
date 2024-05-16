package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.VivacPlacesRepository
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import javax.inject.Inject

class UpdatePlaceUseCase @Inject constructor(private var repository: VivacPlacesRepository){
    operator fun invoke(place: VivacPlace) = repository.updateVivacPlace(place)
}