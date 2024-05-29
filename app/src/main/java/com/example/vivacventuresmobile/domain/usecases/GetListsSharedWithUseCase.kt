package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.ListsRepository
import javax.inject.Inject

class GetListsSharedWithUseCase @Inject constructor(private var repository: ListsRepository) {

    suspend operator fun invoke(listId: Int) =
        repository.getListSharedWith(listId)
}