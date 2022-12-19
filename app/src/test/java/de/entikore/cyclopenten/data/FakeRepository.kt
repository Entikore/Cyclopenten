package de.entikore.cyclopenten.data

import androidx.annotation.VisibleForTesting
import de.entikore.cyclopenten.data.local.entity.ChemicalElement
import de.entikore.cyclopenten.data.local.entity.Highscore
import de.entikore.cyclopenten.data.local.entity.NameScoreAndDifficulty
import de.entikore.cyclopenten.data.local.entity.SaveGame
import kotlin.random.Random
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeRepository : ChemicalElementRepository {

    companion object {
        const val EXPECTED_EXCEPTION = "Test Exception"
    }

    private var shouldReturnError = false

    private val _elements = MutableStateFlow(mutableListOf<ChemicalElement>())
    val elements: StateFlow<List<ChemicalElement>> = _elements.asStateFlow()

    private val observableElements: Flow<Result<List<ChemicalElement>>> = elements.map {
        if (shouldReturnError) {
            Result.Error(Exception())
        } else {
            Result.Success(it)
        }
    }

    private val _saveGame = MutableStateFlow<SaveGame?>(null)
    val saveGame: StateFlow<SaveGame?> = _saveGame

    private val observableSaveGame: Flow<Result<SaveGame?>> = saveGame.map {
        if (shouldReturnError) {
            Result.Error(Exception())
        } else {
            Result.Success(it)
        }
    }

    private val _scoreboard = MutableStateFlow(mutableListOf<Highscore>())
    val scoreboard: StateFlow<List<Highscore>> = _scoreboard

    private val observableScoreboard: Flow<Result<List<Highscore>>> = scoreboard.map {
        if (shouldReturnError) {
            Result.Error(Exception())
        } else {
            Result.Success(it)
        }
    }

    override suspend fun getElements(): Result<List<ChemicalElement>> {
        if (shouldReturnError) {
            return Result.Error(Exception(EXPECTED_EXCEPTION))
        }
        return observableElements.first()
    }

    override suspend fun insertHighscore(nameScoreAndDifficulty: NameScoreAndDifficulty) {
        if (shouldReturnError) {
            throw Exception(EXPECTED_EXCEPTION)
        }
        val newScore = Highscore(
            Random.nextInt(),
            nameScoreAndDifficulty.name,
            nameScoreAndDifficulty.score,
            nameScoreAndDifficulty.hardMode
        )
        _scoreboard.update {
            it.add(newScore)
            it.sortByDescending { entry -> entry.score }
            it
        }
    }

    override suspend fun deleteScoreboard() {
        _scoreboard.value = mutableListOf()
    }

    override suspend fun deleteOldHighscore() {
        val trimmed: MutableList<Highscore> = _scoreboard.value.subList(0, 10)
        _scoreboard.value = trimmed
    }

    override fun getScoreboard(): Flow<Result<List<Highscore>>> {
        return observableScoreboard
    }

    override fun getSaveGame(): Flow<Result<SaveGame?>> {
        return observableSaveGame
    }

    override suspend fun saveGame(saveGame: SaveGame) {
        if (shouldReturnError) {
            throw Exception(EXPECTED_EXCEPTION)
        }
        _saveGame.value = saveGame
    }

    override suspend fun deleteSaveGame() {
        if (shouldReturnError) {
            throw Exception(EXPECTED_EXCEPTION)
        }
        _saveGame.value = null
    }

    @VisibleForTesting
    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    @VisibleForTesting
    fun addElements(vararg elements: ChemicalElement) {
        _elements.update { oldElements ->
            oldElements.addAll(elements)
            oldElements
        }
    }

    @VisibleForTesting
    fun addScoreboardEntries(vararg entries: Highscore) {
        _scoreboard.update {
            it.addAll(entries)
            it.sortByDescending { entry -> entry.score }
            it
        }
    }
}
