package de.entikore.cyclopenten.domain.usecases

import de.entikore.cyclopenten.data.ChemicalElementRepository
import de.entikore.cyclopenten.data.local.entity.NameScoreAndDifficulty
import de.entikore.cyclopenten.domain.usecases.base.BaseUseCaseWithParams
import javax.inject.Inject

class SaveHighscoreUseCase @Inject constructor(private val repository: ChemicalElementRepository) :
    BaseUseCaseWithParams<NameScoreAndDifficulty, Unit> {

    override suspend fun invoke(params: NameScoreAndDifficulty) {
        repository.insertHighscore(params)
        repository.deleteOldHighscore()
    }
}
