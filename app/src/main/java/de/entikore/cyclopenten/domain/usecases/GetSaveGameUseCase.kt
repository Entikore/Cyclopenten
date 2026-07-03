package de.entikore.cyclopenten.domain.usecases

import de.entikore.cyclopenten.data.ChemicalElementRepository
import de.entikore.cyclopenten.data.Result
import de.entikore.cyclopenten.data.local.entity.SaveGame
import de.entikore.cyclopenten.domain.usecases.base.BaseUseCaseWithOutParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSaveGameUseCase
@Inject
constructor(private val repository: ChemicalElementRepository) :
    BaseUseCaseWithOutParams<Flow<Result<SaveGame?>>> {
    override fun invoke(): Flow<Result<SaveGame?>> = repository.getSaveGame()
}
