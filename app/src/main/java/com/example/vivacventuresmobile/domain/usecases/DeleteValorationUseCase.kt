package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.ValorationsRepository
import javax.inject.Inject

class DeleteValorationUseCase @Inject constructor(private var repository: ValorationsRepository) {
    suspend operator fun invoke(id: Int) =
        repository.deleteValoration(id)
}