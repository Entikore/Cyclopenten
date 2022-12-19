package de.entikore.cyclopenten

import de.entikore.cyclopenten.data.local.entity.ChemicalElement
import de.entikore.cyclopenten.data.local.entity.SaveGame
import de.entikore.cyclopenten.util.Constants.CAT_ALKALI_METALS
import de.entikore.cyclopenten.util.Constants.CAT_NOBLE_GASES
import de.entikore.cyclopenten.util.Constants.CAT_NON_METALS

object FakeTestData {
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

    val saveGame = SaveGame(
        lives = 2,
        lostLives = 1,
        score = 30,
        remainingQuestions = testListOfChemicalElements,
        currentElement = testChemicalElement3,
        difficulty = false
    )
}
