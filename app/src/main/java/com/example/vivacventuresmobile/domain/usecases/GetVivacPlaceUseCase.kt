package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.VivacPlacesRepository
import javax.inject.Inject

class GetVivacPlaceUseCase @Inject constructor(private val repository: VivacPlacesRepository){
    operator fun invoke(id: Int, username: String) = repository.getVivacPlace(id, username)
}