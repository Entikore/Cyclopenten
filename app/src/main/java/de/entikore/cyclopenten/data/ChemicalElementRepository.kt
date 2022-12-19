package de.entikore.cyclopenten.data

import de.entikore.cyclopenten.data.local.entity.ChemicalElement
import de.entikore.cyclopenten.data.local.entity.Highscore
import de.entikore.cyclopenten.data.local.entity.NameScoreAndDifficulty
import de.entikore.cyclopenten.data.local.entity.SaveGame
import kotlinx.coroutines.flow.Flow

interface ChemicalElementRepository {
    suspend fun getElements(): Result<List<ChemicalElement>>
    suspend fun insertHighscore(nameScoreAndDifficulty: NameScoreAndDifficulty)
    suspend fun deleteScoreboard()
    suspend fun deleteOldHighscore()
    fun getScoreboard(): Flow<Result<List<Highscore>>>
    fun getSaveGame(): Flow<Result<SaveGame?>>
    suspend fun saveGame(saveGame: SaveGame)
    suspend fun deleteSaveGame()
}
