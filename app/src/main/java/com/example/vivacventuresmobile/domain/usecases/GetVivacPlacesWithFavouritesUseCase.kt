package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.VivacPlacesRepository
import javax.inject.Inject

class GetVivacPlacesWithFavouritesUseCase @Inject constructor(private val repository: VivacPlacesRepository){
    operator fun invoke(username: String) = repository.getVivacPlacesWithFavourites(username)
}