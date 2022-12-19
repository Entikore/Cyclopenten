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
class DeleteGetScoreboardUseCaseTest {
    private lateinit var deleteScoreboardUseCase: DeleteScoreboardUseCase
    private lateinit var repository: FakeRepository

    @Before
    fun setup() {
        repository = FakeRepository()
        deleteScoreboardUseCase = DeleteScoreboardUseCase(repository)
    }

    @Test
    fun `delete scoreboard`() = runTest {
        repository.addScoreboardEntries(
            GoodUnitTestData.testScore1,
            GoodUnitTestData.testScore2,
            GoodUnitTestData.testScore3
        )
        assertThat((repository.getScoreboard().first() as Result.Success).data).isNotEmpty()

        deleteScoreboardUseCase()

        assertThat((repository.getScoreboard().first() as Result.Success).data).isEmpty()
    }
}
