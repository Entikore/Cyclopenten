package de.entikore.cyclopenten.ui.navigation

import androidx.annotation.StringRes
import de.entikore.cyclopenten.R

enum class CyclopentenScreen(@StringRes val title: Int) {
    Start(title = R.string.screen_start),
    Difficulty(title = R.string.screen_difficulty),
    Game(title = R.string.screen_game),
    Score(title = R.string.screen_score),
    Settings(title = R.string.screen_settings),
}
