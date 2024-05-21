package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.ValorationsRepository
import com.example.vivacventuresmobile.domain.modelo.Valoration
import javax.inject.Inject

class AddValorationUseCase @Inject constructor(private var repository: ValorationsRepository) {
    operator fun invoke(valoration: Valoration) = repository.addValoration(valoration)
}