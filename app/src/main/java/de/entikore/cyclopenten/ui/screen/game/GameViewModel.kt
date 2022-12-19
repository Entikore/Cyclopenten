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
import de.entikore.cyclopenten.domain.usecases.SaveSaveGameUseCase
import de.entikore.cyclopenten.domain.usecases.SoundEffectPreferenceUseCase
import de.entikore.cyclopenten.util.Constants
import de.entikore.cyclopenten.util.Constants.SCORE_INCREASE_EASY_DIFF
import de.entikore.cyclopenten.util.Constants.SCORE_INCREASE_HARD_DIFF
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GameViewModel @Inject constructor(
    private val getChemicalElementsUseCase: GetChemicalElementsUseCase,
    private val getSaveGameUseCase: GetSaveGameUseCase,
    private val saveSaveGameUseCase: SaveSaveGameUseCase,
    private val deleteSaveGameUseCase: DeleteSaveGameUseCase,
    private val soundEffectPreferenceUseCase: SoundEffectPreferenceUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var allElements = mutableListOf<ChemicalElement>()

    private val argument = savedStateHandle.get<Boolean>(Constants.DIFFICULTY) ?: false

    private val _gameState = MutableStateFlow(GameScreenState())
    val gameState: StateFlow<GameScreenState> = _gameState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        _gameState.value
    )

    private val _showLoading = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading

    private val _soundEffect = MutableStateFlow(false)
    val soundEffect: StateFlow<Boolean> = _soundEffect

    init {
        viewModelScope.launch {
            val elementsResult = getChemicalElementsUseCase()
            if (elementsResult is Result.Success && elementsResult.data.isNotEmpty()) {
                allElements.addAll(elementsResult.data.shuffled())
                val element = allElements[0]
                _gameState.update { state ->
                    state.setupNewElement(element)
                    state.setDifficulty(hardMode = argument)
                    state
                }
            }
            loadSaveGame()
            soundEffectPreferenceUseCase.invoke().collect {
                _soundEffect.value = it.soundEffectOn
            }
        }
    }

    private fun loadSaveGame() {
        viewModelScope.launch {
            getSaveGameUseCase().collect { saveGame ->
                if (saveGame is Result.Success && saveGame.data != null) {
                    allElements.retainAll(saveGame.data.remainingQuestions)
                    _gameState.update {
                        it.copy(
                            lives = saveGame.data.lives,
                            lostLives = saveGame.data.lostLives,
                            score = saveGame.data.score,
                            hardDifficulty = saveGame.data.difficulty
                        )
                    }
                    updateGameState(saveGame.data.currentElement)
                }
            }
        }
    }

    private fun updateGameState(element: ChemicalElement) {
        _gameState.update {
            it.setupNewElement(element)
            it
        }
        _showLoading.value = false
    }

    fun resetGameState() {
        _gameState.value = GameScreenState()
    }

    fun evaluateAnswer(guess: String) {
        viewModelScope.launch {
            if (guess != _gameState.value.element) {
                wrongAnswer()
            } else {
                _gameState.update { it.copy(hidden = false) }
                _showLoading.value = true
                allElements.removeIf { it.name == guess }
                delay(1800)
                correctAnswer(
                    if (_gameState.value.hardDifficulty) SCORE_INCREASE_HARD_DIFF
                    else SCORE_INCREASE_EASY_DIFF
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
                gameOver = remainingLives < 1
            )
        }
    }

    private fun saveGame() {
        val saveGame = SaveGame(
            lives = _gameState.value.lives,
            lostLives = _gameState.value.lostLives,
            score = _gameState.value.score,
            remainingQuestions = allElements,
            currentElement = allElements[0],
            difficulty = _gameState.value.hardDifficulty
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
