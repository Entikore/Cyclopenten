package de.entikore.cyclopenten.ui.screen.game

import androidx.lifecycle.SavedStateHandle
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import de.entikore.cyclopenten.GoodUnitTestData
import de.entikore.cyclopenten.MainCoroutineRule
import de.entikore.cyclopenten.data.FakeRepository
import de.entikore.cyclopenten.data.Result
import de.entikore.cyclopenten.domain.usecases.DeleteSaveGameUseCase
import de.entikore.cyclopenten.domain.usecases.GetChemicalElementsUseCase
import de.entikore.cyclopenten.domain.usecases.GetSaveGameUseCase
import de.entikore.cyclopenten.domain.usecases.SaveSaveGameUseCase
import de.entikore.cyclopenten.domain.usecases.SoundEffectPreferenceUseCase
import de.entikore.cyclopenten.ui.theme.ColorTheme
import de.entikore.cyclopenten.util.Constants.SCORE_INCREASE_EASY_DIFF
import de.entikore.cyclopenten.util.Constants.SCORE_INCREASE_HARD_DIFF
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(MockitoJUnitRunner::class)
class GameViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: FakeRepository
    private lateinit var getChemicalElementsUseCaseTest: GetChemicalElementsUseCase
    private lateinit var getSaveGameUseCase: GetSaveGameUseCase
    private lateinit var saveSaveGameUseCase: SaveSaveGameUseCase
    private lateinit var deleteSaveGameUseCase: DeleteSaveGameUseCase
    private lateinit var soundEffectPreferenceUseCase: SoundEffectPreferenceUseCase
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var viewModel: GameViewModel

    @Before
    fun setUp() {
        repository = FakeRepository()

        repository.addElements(
            GoodUnitTestData.testChemicalElement1
        )

        getChemicalElementsUseCaseTest = GetChemicalElementsUseCase(repository)
        getSaveGameUseCase = GetSaveGameUseCase(repository)
        saveSaveGameUseCase = SaveSaveGameUseCase(repository)
        deleteSaveGameUseCase = DeleteSaveGameUseCase(repository)
        soundEffectPreferenceUseCase = mock(SoundEffectPreferenceUseCase::class.java)
        savedStateHandle = mock(SavedStateHandle::class.java)

        viewModel = GameViewModel(
            getChemicalElementsUseCaseTest,
            getSaveGameUseCase,
            saveSaveGameUseCase,
            deleteSaveGameUseCase,
            soundEffectPreferenceUseCase,
            savedStateHandle
        )
    }

    @Test
    fun `check game state is build correct`() = runTest {
        //  get first chemical element from use case
        val firstElement =
            (getChemicalElementsUseCaseTest() as Result.Success).data[0]

        // build expected game state based on the first chemical element
        val expectedGameState = GameScreenState(
            element = firstElement.name,
            colorTheme = ColorTheme(firstElement.category),
            atomicNumber = firstElement.atomicNumber,
            category = firstElement.category,
            symbol = firstElement.symbol,
            answerOptions = firstElement.choices
        )
        expectedGameState.setDifficulty(false)

        assertThat(viewModel.gameState.value).isEqualTo(expectedGameState)
    }

    @Test
    fun resetGameState() = runTest {
        //  get first chemical element from use case
        val firstElement =
            (getChemicalElementsUseCaseTest() as de.entikore.cyclopenten.data.Result.Success)
                .data[0]
        // build expected game state based on the first chemical element
        val gameStateBeforeReset = GameScreenState(
            element = firstElement.name,
            colorTheme = ColorTheme(firstElement.category),
            atomicNumber = firstElement.atomicNumber,
            category = firstElement.category,
            symbol = firstElement.symbol,
            answerOptions = firstElement.choices
        )
        gameStateBeforeReset.setDifficulty(false)
        // check current game state is as expected
        assertThat(viewModel.gameState.value).isEqualTo(gameStateBeforeReset)

        viewModel.resetGameState()

        val defaultGameScreenState = GameScreenState()
        assertThat(viewModel.gameState.value).isEqualTo(defaultGameScreenState)
    }

    @Test
    fun `evaluate correct answer difficulty easy`() = runTest {
        val currentGameState = viewModel.gameState.value

        val rightAnswer = currentGameState.element
        val currentScore = currentGameState.score
        val difficulty = currentGameState.hardDifficulty
        // difficulty is easy, score should be incremented by 10
        assertThat(difficulty).isFalse()
        assertThat(currentScore).isEqualTo(0)

        viewModel.evaluateAnswer(rightAnswer)

        advanceUntilIdle()

        assertThat(viewModel.gameState.value.score).isEqualTo(SCORE_INCREASE_EASY_DIFF)
    }

    @Test
    fun `evaluate correct answer difficulty hard`() = runTest {
        viewModel.gameState.value.setDifficulty(hardMode = true)
        val currentGameState = viewModel.gameState.value

        val rightAnswer = currentGameState.element
        val currentScore = currentGameState.score
        val difficulty = currentGameState.hardDifficulty
        // difficulty is hard, score should be incremented by 10
        assertThat(difficulty).isTrue()
        assertThat(currentScore).isEqualTo(0)

        viewModel.evaluateAnswer(rightAnswer)

        advanceUntilIdle()

        assertThat(viewModel.gameState.value.score).isEqualTo(SCORE_INCREASE_HARD_DIFF)
    }

    @Test
    fun `evaluate wrong answer difficulty easy`() = runTest {
        val currentGameState = viewModel.gameState.value

        val rightAnswer = currentGameState.element
        val currentScore = currentGameState.score
        val currentLives = currentGameState.lives
        val currentLostLives = currentGameState.lostLives
        val difficulty = currentGameState.hardDifficulty
        // difficulty is easy, score should be incremented by 10
        assertThat(difficulty).isFalse()
        assertThat(currentScore).isEqualTo(0)
        assertThat(currentLives).isEqualTo(3)
        assertThat(currentLostLives).isEqualTo(0)

        viewModel.evaluateAnswer("wrong Answer")

        advanceUntilIdle()

        assertThat(viewModel.gameState.value.element).isEqualTo(rightAnswer)
        assertThat(viewModel.gameState.value.score).isEqualTo(0)
        assertThat(viewModel.gameState.value.lives).isEqualTo(2)
        assertThat(viewModel.gameState.value.lostLives).isEqualTo(1)
    }
}
