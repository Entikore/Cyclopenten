package de.entikore.cyclopenten.domain.usecases

import de.entikore.cyclopenten.data.ChemicalElementRepository
import de.entikore.cyclopenten.data.Result
import de.entikore.cyclopenten.data.local.entity.ChemicalElement
import de.entikore.cyclopenten.domain.usecases.base.BaseUseCaseWithOutParams
import javax.inject.Inject

class GetChemicalElementsUseCase @Inject constructor(
    private val repository: ChemicalElementRepository
) :
    BaseUseCaseWithOutParams<Result<List<ChemicalElement>>> {

    override suspend fun invoke(): Result<List<ChemicalElement>> = repository.getElements()
}
