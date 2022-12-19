package de.entikore.cyclopenten.ui.screen.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.entikore.cyclopenten.data.Result
import de.entikore.cyclopenten.data.local.entity.Highscore
import de.entikore.cyclopenten.data.local.entity.NameScoreAndDifficulty
import de.entikore.cyclopenten.domain.usecases.GetScoreboardUseCase
import de.entikore.cyclopenten.domain.usecases.SaveHighscoreUseCase
import de.entikore.cyclopenten.ui.theme.randomTheme
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ScoreViewModel @Inject constructor(
    private val getScoreboardUseCase: GetScoreboardUseCase,
    private val newHighScoreUseCaseSave: SaveHighscoreUseCase
) : ViewModel() {
    private val _scoreBoard = MutableStateFlow<List<Highscore>>(emptyList())
    val scoreBoard: StateFlow<List<Highscore>> = _scoreBoard

    private val _showNameField = MutableStateFlow(true)
    val showNameField: StateFlow<Boolean> = _showNameField

    val colorTheme = MutableStateFlow(randomTheme())

    init {
        viewModelScope.launch {
            getScoreboardUseCase.invoke().collect {
                if (it is Result.Success) {
                    _scoreBoard.value = it.data
                }
            }
        }
    }

    fun saveScore(name: String, score: Int, hardMode: Boolean) {
        if (_scoreBoard.value.size < 10 ||
            _scoreBoard.value.any { it.score < score }
        ) {
            viewModelScope.launch {
                newHighScoreUseCaseSave(NameScoreAndDifficulty(name, score, hardMode))
                getScoreboardUseCase.invoke().collect {
                    if (it is Result.Success) {
                        _scoreBoard.value = it.data
                    }
                }
            }
            _showNameField.value = false
        }
    }
}
