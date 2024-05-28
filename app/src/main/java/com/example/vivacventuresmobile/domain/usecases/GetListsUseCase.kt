package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.ListsRepository
import javax.inject.Inject

class GetListsUseCase @Inject constructor(private var repository: ListsRepository) {

    suspend operator fun invoke(username: String) =
        repository.getLists(username)
}