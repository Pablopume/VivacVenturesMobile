package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.FavouritesRepository
import javax.inject.Inject

class AddFavouriteUseCase @Inject constructor(private var repository: FavouritesRepository) {

    suspend operator fun invoke(username: String, vivacId: Int) =
        repository.saveFavourite(username, vivacId)
}