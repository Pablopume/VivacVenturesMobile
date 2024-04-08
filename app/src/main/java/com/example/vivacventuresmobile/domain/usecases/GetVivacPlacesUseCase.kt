package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.VivacPlacesRepository
import javax.inject.Inject

class GetVivacPlacesUseCase @Inject constructor(private val repository: VivacPlacesRepository){
    operator fun invoke() = repository.getVivacPlaces()
}