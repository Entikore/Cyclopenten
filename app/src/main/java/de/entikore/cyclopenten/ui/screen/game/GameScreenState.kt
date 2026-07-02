package de.entikore.cyclopenten.ui.screen.game

import de.entikore.cyclopenten.data.local.entity.ChemicalElement
import de.entikore.cyclopenten.ui.theme.ColorTheme

data class GameScreenState(
    val lives: Int = 0,
    val lostLives: Int = 0,
    val score: Int = 0,
    val atomicNumber: Int = 0,
    val element: String = "",
    val symbol: String = "",
    val answerOptions: List<String> = listOf("", "", "", ""),
    val category: String = "",
    val colorTheme: ColorTheme = ColorTheme(""),
    val hidden: Boolean = true,
    val gameOver: Boolean = false,
    val hardDifficulty: Boolean = false
) {
    fun copyWithDifficulty(hardMode: Boolean): GameScreenState {
        return copy(
            hardDifficulty = hardMode,
            lives = if (hardMode) 4 else 3
        )
    }

    fun copyWithNewElement(newElement: ChemicalElement): GameScreenState {
        return copy(
            atomicNumber = newElement.atomicNumber,
            element = newElement.name,
            category = newElement.category,
            colorTheme = ColorTheme(newElement.category),
            symbol = newElement.symbol,
            answerOptions = newElement.choices,
            hidden = true
        )
    }

    fun hiddenElementName() = if (hidden) "??????" else element
    fun won() = gameOver && lives > 0
}
