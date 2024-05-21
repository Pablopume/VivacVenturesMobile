package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.FavouritesRepository
import javax.inject.Inject

class DeleteFavouriteUseCase @Inject constructor(private var repository: FavouritesRepository) {
    suspend operator fun invoke(username: String, vivacId: Int) =
        repository.deleteFavourite(username, vivacId)
}