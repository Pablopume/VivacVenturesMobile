package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.ListsRepository
import javax.inject.Inject

class DeleteListSharedWithUseCase @Inject constructor(private var repository: ListsRepository) {

    operator fun invoke(vivacId: Int, username: String) =
        repository.deleteListSharedWith(vivacId, username)
}