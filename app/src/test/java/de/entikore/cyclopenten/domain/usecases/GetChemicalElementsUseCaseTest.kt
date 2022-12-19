package de.entikore.cyclopenten.domain.usecases

import androidx.test.filters.SmallTest
import de.entikore.cyclopenten.GoodUnitTestData
import de.entikore.cyclopenten.MainCoroutineRule
import de.entikore.cyclopenten.data.FakeRepository
import de.entikore.cyclopenten.data.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@SmallTest
class GetChemicalElementsUseCaseTest {

    private lateinit var getChemicalElementsUseCase: GetChemicalElementsUseCase

    private lateinit var chemicalRepository: FakeRepository

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        chemicalRepository = FakeRepository()
        getChemicalElementsUseCase = GetChemicalElementsUseCase(chemicalRepository)
    }

    @Test
    fun `empty chemical elements list`() = runTest {
        val tasks = getChemicalElementsUseCase()
        assertTrue(tasks is Result.Success)
        val resultData = (tasks as Result.Success).data
        assertTrue(resultData.isEmpty())
    }

    @Test
    fun `chemical elements list success`() = runTest {
        chemicalRepository.addElements(
            GoodUnitTestData.testChemicalElement1,
            GoodUnitTestData.testChemicalElement2,
            GoodUnitTestData.testChemicalElement3
        )

        val tasks = getChemicalElementsUseCase()
        assertTrue(tasks is Result.Success)
        val resultData = (tasks as Result.Success).data
        assertEquals(3, resultData.size)
        assertTrue(
            resultData.containsAll(
                listOf(
                    GoodUnitTestData.testChemicalElement1,
                    GoodUnitTestData.testChemicalElement2,
                    GoodUnitTestData.testChemicalElement3
                )
            )
        )
    }

    @Test
    fun `chemical elements list error`() = runTest {
        chemicalRepository.setReturnError(true)
        val tasks = getChemicalElementsUseCase()
        assertTrue(tasks is Result.Error)
        val exception = (tasks as Result.Error).exception
        assertEquals(FakeRepository.EXPECTED_EXCEPTION, exception.message)
    }
}
