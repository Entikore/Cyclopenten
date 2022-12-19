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
class GetScoreboardUseCaseTest {

    private lateinit var getScoreboardUseCase: GetScoreboardUseCase

    private lateinit var repository: FakeRepository

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        repository = FakeRepository()
        getScoreboardUseCase = GetScoreboardUseCase(repository)
    }

    @Test
    fun `get scoreboard`() = runTest {
        var scoreboard = (getScoreboardUseCase().first() as Result.Success).data
        assertThat(scoreboard).isEmpty()
        repository.addScoreboardEntries(GoodUnitTestData.testScore1, GoodUnitTestData.testScore2)
        scoreboard = (getScoreboardUseCase().first() as Result.Success).data
        assertThat(scoreboard).isNotEmpty()
        assertThat(scoreboard.size).isEqualTo(2)
    }
}
