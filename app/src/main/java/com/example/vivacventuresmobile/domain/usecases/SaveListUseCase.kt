package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.ListsRepository
import com.example.vivacventuresmobile.domain.modelo.ListFavs
import javax.inject.Inject

class SaveListUseCase @Inject constructor(private var repository: ListsRepository) {

    suspend operator fun invoke(listFavs: ListFavs) =
        repository.saveList(listFavs)
}