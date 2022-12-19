package de.entikore.cyclopenten.ui.screen.game

import de.entikore.cyclopenten.data.local.entity.ChemicalElement
import de.entikore.cyclopenten.ui.theme.ColorTheme

data class GameScreenState(
    var lives: Int = 0,
    var lostLives: Int = 0,
    var score: Int = 0,
    var atomicNumber: Int = 0,
    var element: String = "",
    var symbol: String = "",
    var answerOptions: List<String> = listOf("", "", "", ""),
    var category: String = "",
    var colorTheme: ColorTheme = ColorTheme(""),
    var hidden: Boolean = true,
    var gameOver: Boolean = false,
    var hardDifficulty: Boolean = false
) {
    fun setDifficulty(hardMode: Boolean) {
        hardDifficulty = hardMode
        lives = if (hardMode) 4 else 3
    }

    fun setupNewElement(newElement: ChemicalElement) {
        atomicNumber = newElement.atomicNumber
        element = newElement.name
        category = newElement.category
        colorTheme = ColorTheme(category)
        symbol = newElement.symbol
        answerOptions = newElement.choices
        hidden = true
    }

    fun hiddenElementName() = if (hidden) "??????" else element
    fun won() = gameOver && lives > 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GameScreenState) return false
        if (lives != other.lives ||
            lostLives != other.lostLives ||
            score != other.score ||
            atomicNumber != other.atomicNumber ||
            element != other.element ||
            symbol != other.symbol ||
            category != other.category ||
            hidden != other.hidden ||
            gameOver != other.gameOver ||
            hardDifficulty != other.hardDifficulty
        ) return false
        if (
            !(
                answerOptions.size == other.answerOptions.size &&
                    answerOptions.toSet() == other.answerOptions.toSet()
                )
        ) return false
        if (colorTheme != other.colorTheme) return false
        return true
    }

    override fun hashCode(): Int {
        var result = lives
        result = 31 * result + lostLives
        result = 31 * result + score
        result = 31 * result + atomicNumber
        result = 31 * result + element.hashCode()
        result = 31 * result + symbol.hashCode()
        result = 31 * result + answerOptions.hashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + colorTheme.hashCode()
        result = 31 * result + hidden.hashCode()
        result = 31 * result + gameOver.hashCode()
        result = 31 * result + hardDifficulty.hashCode()
        return result
    }
}
