package de.entikore.cyclopenten.domain.usecases

import de.entikore.cyclopenten.data.ChemicalElementRepository
import de.entikore.cyclopenten.data.Result
import de.entikore.cyclopenten.data.local.entity.Highscore
import de.entikore.cyclopenten.domain.usecases.base.BaseUseCaseWithOutParams
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetScoreboardUseCase @Inject constructor(private val repository: ChemicalElementRepository) :
    BaseUseCaseWithOutParams<Flow<Result<List<Highscore>>>> {

    override suspend fun invoke(): Flow<Result<List<Highscore>>> = repository.getScoreboard()
}
