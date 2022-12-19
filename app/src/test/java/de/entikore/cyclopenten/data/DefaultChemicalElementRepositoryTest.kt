package de.entikore.cyclopenten.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import de.entikore.cyclopenten.GoodUnitTestData
import de.entikore.cyclopenten.MainCoroutineRule
import de.entikore.cyclopenten.data.local.ChemicalElementDatabase
import de.entikore.cyclopenten.data.local.entity.Highscore
import de.entikore.cyclopenten.data.local.entity.NameScoreAndDifficulty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(RobolectricTestRunner::class)
class DefaultChemicalElementRepositoryTest {

    // Class under test
    private lateinit var chemicalRepository: DefaultChemicalElementRepository
    private lateinit var database: ChemicalElementDatabase

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createRepository() {
        // build dependencies for class under test
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ChemicalElementDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        // Get a reference to the class under test
        chemicalRepository = DefaultChemicalElementRepository(
            database.chemicalElementDao(), Dispatchers.Main
        )
    }

    @Test
    fun `getElements empty repository`() = runTest {
        assertThat(chemicalRepository.getElements() is Result.Success).isTrue()
        assertThat((chemicalRepository.getElements() as Result.Success).data).isEmpty()
    }

    @Test
    fun `getElements non empty repository`() = runTest {
        database.chemicalElementDao().insert(GoodUnitTestData.testChemicalElement1)

        val resultSuccess = chemicalRepository.getElements() as Result.Success

        assertThat(resultSuccess.data.size).isEqualTo(1)
        assertThat(resultSuccess.data[0]).isEqualTo(GoodUnitTestData.testChemicalElement1)
    }

    @Test
    fun `getElements multiple elements in repository`() = runTest {
        database.chemicalElementDao().insertAll(GoodUnitTestData.testListOfChemicalElements)

        val resultSuccess = chemicalRepository.getElements() as Result.Success

        assertThat(resultSuccess.data.size)
            .isEqualTo(GoodUnitTestData.testListOfChemicalElements.size)
        assertThat(resultSuccess.data).isEqualTo(GoodUnitTestData.testListOfChemicalElements)
    }

    @Test
    fun `insertHighscore in repository`() = runTest {
        val scoreBoard: Result<List<Highscore>> = chemicalRepository.getScoreboard().first()
        assertThat(scoreBoard is Result.Success).isTrue()
        assertThat((scoreBoard as Result.Success).data).isEmpty()

        chemicalRepository.insertHighscore(GoodUnitTestData.testNameScoreAndDifficulty)

        val scoreBoardAfterInsert: Result<List<Highscore>> =
            chemicalRepository.getScoreboard().first()
        assertThat(scoreBoardAfterInsert is Result.Success).isTrue()
        val data = (scoreBoardAfterInsert as Result.Success).data
        assertThat(data).isNotEmpty()
        assertThat(data[0].name).isEqualTo(GoodUnitTestData.testNameScoreAndDifficulty.name)
        assertThat(data[0].score).isEqualTo(GoodUnitTestData.testNameScoreAndDifficulty.score)
        assertThat(data[0].hardMode).isEqualTo(GoodUnitTestData.testNameScoreAndDifficulty.hardMode)
    }

    @Test
    fun `deleteScoreboard from repository`() = runTest {
        chemicalRepository.insertHighscore(GoodUnitTestData.testNameScoreAndDifficulty)

        val scoreBoard: Result<List<Highscore>> = chemicalRepository.getScoreboard().first()
        assertThat(scoreBoard is Result.Success).isTrue()
        assertThat((scoreBoard as Result.Success).data).isNotEmpty()

        chemicalRepository.deleteScoreboard()

        val scoreBoardAfterDelete: Result<List<Highscore>> =
            chemicalRepository.getScoreboard().first()
        assertThat(scoreBoardAfterDelete is Result.Success).isTrue()
        assertThat((scoreBoardAfterDelete as Result.Success).data).isEmpty()
    }

    @Test
    fun `deleteOldHighscore to just have 10 entries`() = runTest {
        for (i in 1..11) {
            database.chemicalElementDao()
                .insertHighscore(NameScoreAndDifficulty("Tester$i", i, false))
        }
        val scoreBoard: List<Highscore> =
            database.chemicalElementDao().getAllHighscores(100).first()
        assertThat(scoreBoard.size).isGreaterThan(10)

        chemicalRepository.deleteOldHighscore()
        advanceUntilIdle()
        val scoreBoardAfterDelete: List<Highscore> =
            database.chemicalElementDao().getAllHighscores(100).first()
        assertThat(scoreBoardAfterDelete.size).isEqualTo(10)
    }

    @Test
    fun `getScoreboard from repository`() = runTest {
        var result = chemicalRepository.getScoreboard().first()
        assertThat(result is Result.Success).isTrue()
        assertThat((result as Result.Success).data).isEmpty()

        for (i in 1..10) {
            database.chemicalElementDao()
                .insertHighscore(NameScoreAndDifficulty("Tester$i", i, false))
        }
        val scoreBoard: List<Highscore> =
            database.chemicalElementDao().getAllHighscores(100).first()
        assertThat(scoreBoard.size).isEqualTo(10)

        result = chemicalRepository.getScoreboard().first()
        assertThat(result is Result.Success).isTrue()
        assertThat((result as Result.Success).data).isNotEmpty()
        assertThat(result.data.size).isEqualTo(10)
    }

    @Test
    fun `getSaveGame from repository`() = runTest {
        var result = chemicalRepository.getSaveGame().first()
        assertThat(result is Result.Success).isTrue()
        assertThat((result as Result.Success).data).isNull()

        database.chemicalElementDao().saveGame(GoodUnitTestData.testSaveGame)

        result = chemicalRepository.getSaveGame().first()
        assertThat(result is Result.Success).isTrue()
        assertThat((result as Result.Success).data).isNotNull()
        assertThat(result.data).isEqualTo(GoodUnitTestData.testSaveGame)
    }

    @Test
    fun `saveGame to repository`() = runTest {
        var result = chemicalRepository.getSaveGame().first()
        assertThat(result is Result.Success).isTrue()
        assertThat((result as Result.Success).data).isNull()

        chemicalRepository.saveGame(GoodUnitTestData.testSaveGame)

        result = chemicalRepository.getSaveGame().first()
        assertThat(result is Result.Success).isTrue()
        assertThat((result as Result.Success).data).isNotNull()
        assertThat(result.data).isEqualTo(GoodUnitTestData.testSaveGame)
    }

    @Test
    fun `deleteSaveGame from repository`() = runTest {
        database.chemicalElementDao().saveGame(GoodUnitTestData.testSaveGame)
        var result = chemicalRepository.getSaveGame().first()
        assertThat(result is Result.Success).isTrue()
        assertThat((result as Result.Success).data).isNotNull()

        chemicalRepository.deleteSaveGame()

        result = chemicalRepository.getSaveGame().first()
        assertThat(result is Result.Success).isTrue()
        assertThat((result as Result.Success).data).isNull()
    }
}
