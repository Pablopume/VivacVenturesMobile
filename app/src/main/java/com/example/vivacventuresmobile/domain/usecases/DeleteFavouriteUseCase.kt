package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.ListsRepository
import javax.inject.Inject

class DeleteFavouriteUseCase @Inject constructor(private var repository: ListsRepository) {
    suspend operator fun invoke(listId: Int, vivacId: Int) =
        repository.removeFavoriteFromList(listId, vivacId)
}