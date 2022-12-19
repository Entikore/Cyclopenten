package de.entikore.cyclopenten.data

import de.entikore.cyclopenten.data.local.ChemicalElementDao
import de.entikore.cyclopenten.data.local.entity.ChemicalElement
import de.entikore.cyclopenten.data.local.entity.Highscore
import de.entikore.cyclopenten.data.local.entity.NameScoreAndDifficulty
import de.entikore.cyclopenten.data.local.entity.SaveGame
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

class DefaultChemicalElementRepository @Inject constructor(
    private val dao: ChemicalElementDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    ChemicalElementRepository {

    override suspend fun getElements(): Result<List<ChemicalElement>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(dao.getAllElements())
        } catch (e: Exception) {
            Timber.e("Error fetching elements from database.", e)
            Result.Error(e)
        }
    }

    override suspend fun insertHighscore(nameScoreAndDifficulty: NameScoreAndDifficulty) {
        withContext(ioDispatcher) {
            dao.insertHighscore(nameScoreAndDifficulty)
        }
    }

    override suspend fun deleteScoreboard() {
        withContext(ioDispatcher) {
            dao.deleteScoreboard()
        }
    }

    override suspend fun deleteOldHighscore() = withContext(ioDispatcher) {
        dao.deleteOldHighscore()
    }

    override fun getScoreboard(): Flow<Result<List<Highscore>>> {
        return dao.getAllHighscores(10).map {
            Result.Success(it)
        }
    }

    override fun getSaveGame(): Flow<Result<SaveGame?>> {
        return dao.hasSaveGame(SaveGame.SAVE_GAME_ID).map {
            Result.Success(it)
        }
    }

    override suspend fun saveGame(saveGame: SaveGame) {
        withContext(ioDispatcher) {
            dao.saveGame(saveGame)
        }
    }

    override suspend fun deleteSaveGame() = withContext(ioDispatcher) { dao.deleteSaveGame() }
}
