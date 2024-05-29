package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.ListsRepository
import javax.inject.Inject

class GetListByUserAndVivacPlaceUseCase @Inject constructor(private var repository: ListsRepository) {

    suspend operator fun invoke(vivacId: Int, username: String) =
        repository.getListsByUserAndVivacPlace(username, vivacId)
}