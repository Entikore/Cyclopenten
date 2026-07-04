package de.entikore.cyclopenten.ui.screen.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.entikore.cyclopenten.data.Result
import de.entikore.cyclopenten.data.local.entity.ChemicalElement
import de.entikore.cyclopenten.data.local.entity.SaveGame
import de.entikore.cyclopenten.domain.usecases.DeleteSaveGameUseCase
import de.entikore.cyclopenten.domain.usecases.GetChemicalElementsUseCase
import de.entikore.cyclopenten.domain.usecases.GetSaveGameUseCase
import de.entikore.cyclopenten.domain.usecases.SaveSaveGameBaseUseCase
import de.entikore.cyclopenten.domain.usecases.SoundEffectPreferenceUseCase
import de.entikore.cyclopenten.util.Constants
import de.entikore.cyclopenten.util.Constants.SCORE_INCREASE_EASY_DIFF
import de.entikore.cyclopenten.util.Constants.SCORE_INCREASE_HARD_DIFF
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class GameViewModel
@Inject
constructor(
    private val getChemicalElementsUseCase: GetChemicalElementsUseCase,
    private val getSaveGameUseCase: GetSaveGameUseCase,
    private val saveSaveGameUseCase: SaveSaveGameBaseUseCase,
    private val deleteSaveGameUseCase: DeleteSaveGameUseCase,
    private val soundEffectPreferenceUseCase: SoundEffectPreferenceUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val allElements = mutableListOf<ChemicalElement>()

    private val argument = savedStateHandle.get<Boolean>(Constants.DIFFICULTY) ?: false

    private val _gameState = MutableStateFlow(GameScreenState())
    val gameState: StateFlow<GameScreenState> =
        _gameState.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            _gameState.value,
        )

    private val _showLoading = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading.asStateFlow()

    private val _soundEffect = MutableStateFlow(false)
    val soundEffect: StateFlow<Boolean> = _soundEffect.asStateFlow()

    init {
        viewModelScope.launch {
            // Collect sound preferences concurrently
            launch {
                soundEffectPreferenceUseCase.invoke().collect {
                    _soundEffect.value = it.soundEffectOn
                }
            }

            // Sequentially load elements and check for any existing save game
            val elementsResult = getChemicalElementsUseCase()
            if (elementsResult is Result.Success && elementsResult.data.isNotEmpty()) {
                val shuffledElements = elementsResult.data.shuffled()
                val saveGameResult = getSaveGameUseCase().first()

                if (saveGameResult is Result.Success && saveGameResult.data != null) {
                    val saveGame = saveGameResult.data
                    allElements.addAll(saveGame.remainingQuestions)
                    _gameState.update {
                        it.copy(
                            lives = saveGame.lives,
                            lostLives = saveGame.lostLives,
                            score = saveGame.score,
                            hardDifficulty = saveGame.difficulty,
                            hidden = true,
                        ).copyWithNewElement(saveGame.currentElement)
                    }
                } else {
                    allElements.addAll(shuffledElements)
                    val element = allElements[0]
                    _gameState.update { state ->
                        state
                            .copyWithNewElement(element)
                            .copyWithDifficulty(hardMode = argument)
                    }
                }
            }
        }
    }

    private fun updateGameState(element: ChemicalElement) {
        _gameState.update {
            it.copyWithNewElement(element)
        }
        _showLoading.value = false
    }

    fun resetGameState() {
        _gameState.value = GameScreenState()
    }

    fun evaluateAnswer(guess: String) {
        // Prevent double answer submission/clicks while loading or if answer is already revealed
        if (!_gameState.value.hidden || _showLoading.value) return

        viewModelScope.launch {
            if (guess != _gameState.value.element) {
                wrongAnswer()
            } else {
                _gameState.update { it.copy(hidden = false) }
                _showLoading.value = true
                allElements.removeIf { it.name == guess }
                delay(1800.milliseconds)
                correctAnswer(
                    if (_gameState.value.hardDifficulty) {
                        SCORE_INCREASE_HARD_DIFF
                    } else {
                        SCORE_INCREASE_EASY_DIFF
                    },
                )
            }
            if (!_gameState.value.gameOver) {
                saveGame()
            } else {
                deleteSaveGame()
            }
        }
    }

    private fun correctAnswer(addedScore: Int) {
        if (allElements.isEmpty()) {
            _gameState.update { it.copy(score = it.score + addedScore, gameOver = true) }
        } else {
            _gameState.update { it.copy(score = it.score + addedScore) }
            updateGameState(allElements[0])
        }
    }

    private fun wrongAnswer() {
        val remainingLives = _gameState.value.lives - 1
        _gameState.update {
            it.copy(
                lives = remainingLives,
                lostLives = it.lostLives + 1,
                gameOver = remainingLives < 1,
            )
        }
    }

    private fun saveGame() {
        val saveGame =
            SaveGame(
                lives = _gameState.value.lives,
                lostLives = _gameState.value.lostLives,
                score = _gameState.value.score,
                remainingQuestions = allElements,
                currentElement = allElements[0],
                difficulty = _gameState.value.hardDifficulty,
            )
        viewModelScope.launch {
            saveSaveGameUseCase.invoke(saveGame)
        }
    }

    private fun deleteSaveGame() {
        viewModelScope.launch {
            deleteSaveGameUseCase()
        }
    }
}
