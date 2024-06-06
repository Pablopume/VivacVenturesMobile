package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.ListsRepository
import javax.inject.Inject

class ShareListUseCase @Inject constructor(private var repository: ListsRepository) {

    operator fun invoke(listId: Int, username: String) =
        repository.shareList(listId, username)
}