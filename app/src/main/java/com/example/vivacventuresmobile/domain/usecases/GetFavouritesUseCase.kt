package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.FavouritesRepository
import javax.inject.Inject

class GetFavouritesUseCase @Inject constructor(private val repository: FavouritesRepository) {
    operator fun invoke(username: String) = repository.getFavourites(username)
}