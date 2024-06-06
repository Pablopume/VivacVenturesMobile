package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.ListsRepository
import javax.inject.Inject

class GetListUseCase @Inject constructor(private var repository: ListsRepository) {

    operator fun invoke(listId: Int) =
        repository.getList(listId)
}