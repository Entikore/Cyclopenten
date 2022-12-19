package de.entikore.cyclopenten

import de.entikore.cyclopenten.data.local.entity.ChemicalElement
import de.entikore.cyclopenten.data.local.entity.Highscore
import de.entikore.cyclopenten.data.local.entity.NameScoreAndDifficulty
import de.entikore.cyclopenten.data.local.entity.SaveGame
import de.entikore.cyclopenten.util.Constants.CAT_ALKALI_METALS
import de.entikore.cyclopenten.util.Constants.CAT_NOBLE_GASES
import de.entikore.cyclopenten.util.Constants.CAT_NON_METALS
import kotlin.random.Random

object GoodUnitTestData {

    val testChemicalElement1 = ChemicalElement(
        atomicNumber = 1,
        iupacGroup = 1,
        period = 1,
        category = CAT_NON_METALS,
        symbol = "H",
        name = "Hydrogen",
        choices = listOf(
            "Hydrogen",
            "Hafnium",
            "Helium",
            "Holmium"
        )
    )

    val testChemicalElement2 = ChemicalElement(
        atomicNumber = 2,
        iupacGroup = 18,
        period = 1,
        category = CAT_NOBLE_GASES,
        symbol = "He",
        name = "Helium",
        choices = listOf(
            "Helium",
            "Helix",
            "Hefnium",
            "Hesitanium"
        )
    )

    val testChemicalElement3 = ChemicalElement(
        atomicNumber = 3,
        iupacGroup = 1,
        period = 2,
        category = CAT_ALKALI_METALS,
        symbol = "Li",
        name = "Lithium",
        choices = listOf(
            "Lithium",
            "Lawrencium",
            "Lithiotanite",
            "Libertius"
        )
    )

    val testListOfChemicalElements = listOf(testChemicalElement1, testChemicalElement2)

    val testSaveGame = SaveGame(
        lives = 2,
        lostLives = 1,
        score = 30,
        remainingQuestions = testListOfChemicalElements,
        currentElement = testChemicalElement3,
        difficulty = false
    )

    val testScore1 = Highscore(1, "Tester", 230, false)
    val testScore2 = Highscore(2, "Tester2", 0, true)
    val testScore3 = Highscore(3, "Tester3", 120, false)

    val testNameScoreAndDifficulty = NameScoreAndDifficulty("Tester", 20, false)
    val testScoreboard = listOf(testScore1, testScore2, testScore3)

    fun getScoreboardEntries(numberOfEntries: Int): MutableList<Highscore> {
        val scoreList = mutableListOf<Highscore>()
        for (i in 1..numberOfEntries) {
            scoreList.add(Highscore(i, "User$i", i, Random.nextBoolean()))
        }
        return scoreList
    }
}
