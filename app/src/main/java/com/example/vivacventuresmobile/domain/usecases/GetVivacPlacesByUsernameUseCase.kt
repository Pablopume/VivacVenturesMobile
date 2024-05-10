package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.VivacPlacesRepository
import javax.inject.Inject

class GetVivacPlacesByUsernameUseCase @Inject constructor(private val repository: VivacPlacesRepository){
    operator fun invoke(username: String) = repository.getVivacPlacesByUsername(username)
}