package de.entikore.cyclopenten.domain.usecases

import de.entikore.cyclopenten.data.ChemicalElementRepository
import de.entikore.cyclopenten.domain.usecases.base.BaseUseCaseWithOutParams
import javax.inject.Inject

class DeleteSaveGameUseCase @Inject constructor(private val repository: ChemicalElementRepository) :
    BaseUseCaseWithOutParams<Unit> {

    override suspend fun invoke() = repository.deleteSaveGame()
}
