package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.VivacPlacesRepository
import javax.inject.Inject

class DeleteVivacPlaceUseCase @Inject constructor(private var repository: VivacPlacesRepository) {

    suspend operator fun invoke(vivacId: Int) =
        repository.deleteVivacPlace(vivacId)
}