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
class DeleteSaveGetChemicalElementsUseCaseTest {

    private lateinit var deleteSaveGameUseCase: DeleteSaveGameUseCase
    private lateinit var repository: FakeRepository

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        repository = FakeRepository()

        deleteSaveGameUseCase = DeleteSaveGameUseCase(repository)
    }

    @Test
    fun `delete a save game`() = runTest {
        repository.saveGame(GoodUnitTestData.testSaveGame)

        val fetchedSaveGame = (repository.getSaveGame().first() as Result.Success).data

        assertThat(fetchedSaveGame).isNotNull()
        deleteSaveGameUseCase()

        val fetchedSaveGameAfter = (repository.getSaveGame().first() as Result.Success).data
        assertThat(fetchedSaveGameAfter).isNull()
    }
}
