package de.entikore.cyclopenten.ui.screen.settings

import de.entikore.cyclopenten.data.UserPreferences
import de.entikore.cyclopenten.ui.theme.ColorTheme

data class SettingsScreenState(
    val userPreferences: UserPreferences,
    val colorTheme: ColorTheme
)
