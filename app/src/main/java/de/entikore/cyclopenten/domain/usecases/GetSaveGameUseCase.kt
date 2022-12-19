package de.entikore.cyclopenten.domain.usecases

import de.entikore.cyclopenten.data.ChemicalElementRepository
import de.entikore.cyclopenten.data.Result
import de.entikore.cyclopenten.data.local.entity.SaveGame
import de.entikore.cyclopenten.domain.usecases.base.BaseUseCaseWithOutParams
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetSaveGameUseCase @Inject constructor(private val repository: ChemicalElementRepository) :
    BaseUseCaseWithOutParams<Flow<Result<SaveGame?>>> {

    override suspend fun invoke(): Flow<Result<SaveGame?>> = repository.getSaveGame()
}
