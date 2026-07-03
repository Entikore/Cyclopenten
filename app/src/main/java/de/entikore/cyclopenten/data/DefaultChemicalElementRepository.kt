package de.entikore.cyclopenten.data

import android.database.sqlite.SQLiteException
import de.entikore.cyclopenten.data.local.ChemicalElementDao
import de.entikore.cyclopenten.data.local.entity.ChemicalElement
import de.entikore.cyclopenten.data.local.entity.Highscore
import de.entikore.cyclopenten.data.local.entity.NameScoreAndDifficulty
import de.entikore.cyclopenten.data.local.entity.SaveGame
import de.entikore.cyclopenten.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DefaultChemicalElementRepository
@Inject
constructor(
    private val dao: ChemicalElementDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ChemicalElementRepository {
    override suspend fun getElements(): Result<List<ChemicalElement>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(dao.getAllElements())
        } catch (e: SQLiteException) {
            Timber.e(e, "Error fetching elements from database.")
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

    override fun getScoreboard(): Flow<Result<List<Highscore>>> = dao.getAllHighscores(HIGHSCORE_LIMIT)
        .map<List<Highscore>, Result<List<Highscore>>> { Result.Success(it) }
        .catch { emit(Result.Error(Exception(it))) }

    override fun getSaveGame(): Flow<Result<SaveGame?>> = dao.hasSaveGame(SaveGame.SAVE_GAME_ID)
        .map<SaveGame?, Result<SaveGame?>> { Result.Success(it) }
        .catch { emit(Result.Error(Exception(it))) }

    override suspend fun saveGame(saveGame: SaveGame) {
        withContext(ioDispatcher) {
            dao.saveGame(saveGame)
        }
    }

    override suspend fun deleteSaveGame() = withContext(ioDispatcher) { dao.deleteSaveGame() }

    companion object {
        const val HIGHSCORE_LIMIT = 10
    }
}
