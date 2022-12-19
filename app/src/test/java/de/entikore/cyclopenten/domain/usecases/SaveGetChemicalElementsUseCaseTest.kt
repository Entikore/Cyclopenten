package de.entikore.cyclopenten.domain.usecases

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import de.entikore.cyclopenten.GoodUnitTestData
import de.entikore.cyclopenten.MainCoroutineRule
import de.entikore.cyclopenten.data.FakeRepository
import de.entikore.cyclopenten.data.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@SmallTest
class SaveGetChemicalElementsUseCaseTest {

    private lateinit var saveSaveGameUseCase: SaveSaveGameUseCase

    private lateinit var repository: FakeRepository

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        repository = FakeRepository()
        saveSaveGameUseCase = SaveSaveGameUseCase(repository)
    }

    @Test
    fun `save a save game`() = runTest {
        var saveGame = (repository.getSaveGame().first() as Result.Success).data
        assertThat(saveGame).isNull()

        saveSaveGameUseCase(GoodUnitTestData.testSaveGame)

        saveGame = (repository.getSaveGame().first() as Result.Success).data
        assertThat(saveGame).isNotNull()
        assertThat(GoodUnitTestData.testSaveGame).isEqualTo(saveGame)
    }
}
