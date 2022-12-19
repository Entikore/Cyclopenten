package de.entikore.cyclopenten.domain.usecases

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import de.entikore.cyclopenten.GoodUnitTestData
import de.entikore.cyclopenten.data.FakeRepository
import de.entikore.cyclopenten.data.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@SmallTest
class GetSaveGetChemicalElementsUseCaseTest {

    private lateinit var getSaveGameUseCase: GetSaveGameUseCase
    private lateinit var repository: FakeRepository

    @Before
    fun setup() {
        repository = FakeRepository()
        getSaveGameUseCase = GetSaveGameUseCase(repository)
    }

    @Test
    fun `get a save game`() = runTest {
        var saveGame = (getSaveGameUseCase.invoke().first() as Result.Success).data
        assertThat(saveGame).isNull()
        repository.saveGame(GoodUnitTestData.testSaveGame)
        saveGame = (getSaveGameUseCase.invoke().first() as Result.Success).data
        assertThat(saveGame).isNotNull()
        assertThat(saveGame).isEqualTo(GoodUnitTestData.testSaveGame)
    }
}
