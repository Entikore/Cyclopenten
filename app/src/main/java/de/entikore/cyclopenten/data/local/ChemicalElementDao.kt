package de.entikore.cyclopenten.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.entikore.cyclopenten.data.local.entity.ChemicalElement
import de.entikore.cyclopenten.data.local.entity.Highscore
import de.entikore.cyclopenten.data.local.entity.NameScoreAndDifficulty
import de.entikore.cyclopenten.data.local.entity.SaveGame
import kotlinx.coroutines.flow.Flow

@Dao
interface ChemicalElementDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(chemicalElement: ChemicalElement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chemicalElements: List<ChemicalElement>)

    @Query("SELECT * FROM chemical_elements")
    suspend fun getAllElements(): List<ChemicalElement>

    @Insert(entity = Highscore::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHighscore(nameScoreAndDifficulty: NameScoreAndDifficulty)

    @Query("SELECT * FROM highscore ORDER BY score DESC LIMIT :limit")
    fun getAllHighscores(limit: Int): Flow<List<Highscore>>

    @Query(
        "DELETE FROM highscore WHERE id IN (SELECT id FROM highscore ORDER BY score DESC" +
            " LIMIT 1 OFFSET 10)"
    )

    fun deleteOldHighscore()

    @Query("DELETE FROM highscore")
    fun deleteScoreboard()

    @Query("SELECT * FROM save_game WHERE id = :id")
    fun hasSaveGame(id: Int): Flow<SaveGame?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGame(saveGame: SaveGame)

    @Query("DELETE FROM save_game")
    fun deleteSaveGame()
}
