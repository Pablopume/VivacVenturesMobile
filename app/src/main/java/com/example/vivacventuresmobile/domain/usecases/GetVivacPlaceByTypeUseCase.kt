package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.VivacPlacesRepository
import javax.inject.Inject

class GetVivacPlaceByTypeUseCase @Inject constructor(private val repository: VivacPlacesRepository){
    operator fun invoke(type: String) = repository.getVivacPlaceByType(type)
}