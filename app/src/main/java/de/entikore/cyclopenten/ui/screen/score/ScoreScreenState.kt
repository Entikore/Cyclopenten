package de.entikore.cyclopenten.ui.screen.score

import de.entikore.cyclopenten.data.local.entity.Highscore
import de.entikore.cyclopenten.ui.theme.ColorTheme

data class ScoreScreenState(
    val scoreboard: List<Highscore>,
    val showNameField: Boolean,
    val gameEndMessage: String,
    val scoreMessage: String,
    val score: Int,
    val hardMode: Boolean,
    val colorTheme: ColorTheme,
)
