package de.entikore.cyclopenten.domain.usecases

import de.entikore.cyclopenten.data.ChemicalElementRepository
import de.entikore.cyclopenten.data.local.entity.SaveGame
import de.entikore.cyclopenten.domain.usecases.base.SuspendBaseUseCaseWithParams
import javax.inject.Inject

class SaveSaveGameBaseUseCase
@Inject
constructor(private val repository: ChemicalElementRepository) :
    SuspendBaseUseCaseWithParams<SaveGame, Unit> {
    override suspend fun invoke(params: SaveGame) {
        repository.saveGame(params)
    }
}
