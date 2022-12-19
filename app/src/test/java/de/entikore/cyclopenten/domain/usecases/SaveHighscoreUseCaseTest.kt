package de.entikore.cyclopenten.domain.usecases

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import de.entikore.cyclopenten.GoodUnitTestData
import de.entikore.cyclopenten.data.FakeRepository
import de.entikore.cyclopenten.data.Result
import de.entikore.cyclopenten.data.local.entity.NameScoreAndDifficulty
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@SmallTest
class SaveHighscoreUseCaseTest {
    private lateinit var repository: FakeRepository
    private lateinit var saveHighscoreUseCase: SaveHighscoreUseCase

    @Before
    fun setup() {
        repository = FakeRepository()
        saveHighscoreUseCase = SaveHighscoreUseCase(repository)
    }

    @Test
    fun `save new highscore`() = runTest {
        val numberOfEntries = 20
        val entries = GoodUnitTestData.getScoreboardEntries(numberOfEntries)
        entries.forEach { repository.addScoreboardEntries(it) }
        var scoreboard = (repository.getScoreboard().first() as Result.Success).data
        assertThat(scoreboard.size).isEqualTo(numberOfEntries)

        val expectedEntry = NameScoreAndDifficulty("TestUser", 45, false)
        assertThat(
            scoreboard.firstOrNull {
                it.name == expectedEntry.name &&
                    it.score == expectedEntry.score &&
                    it.hardMode == expectedEntry.hardMode
            }
        ).isNull()
        saveHighscoreUseCase(expectedEntry)

        scoreboard = (repository.getScoreboard().first() as Result.Success).data
        assertThat(scoreboard).isNotEmpty()
        assertThat(scoreboard.size).isEqualTo(10)
        assertThat(
            scoreboard.firstOrNull {
                it.name == expectedEntry.name &&
                    it.score == expectedEntry.score &&
                    it.hardMode == expectedEntry.hardMode
            }
        ).isNotNull()
    }
}
