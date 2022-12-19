package de.entikore.cyclopenten.ui.screen.settings

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import de.entikore.cyclopenten.data.UserPreferences
import de.entikore.cyclopenten.ui.theme.randomTheme
import de.entikore.cyclopenten.util.Semantics
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SettingsScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var screenState: SettingsScreenState

    @Test
    fun userInteractionOptionsAvailable() {
        composeTestRule.setContent {
            SettingScreen(
                screenState = SettingsScreenState(
                    UserPreferences(
                        musicOn = false,
                        soundEffectOn = false
                    ),
                    colorTheme = randomTheme()
                ),
                updateMusicSetting = {},
                updateSoundEffectSetting = {},
                clearScoreboard = {}
            )
        }
        composeTestRule.onNode(hasTestTag(Semantics.SWITCH_SETTINGS_MUSIC))
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
        composeTestRule.onNode(hasTestTag(Semantics.SWITCH_SETTINGS_SOUND))
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
        composeTestRule.onNode(hasTestTag(Semantics.BTN_SETTINGS_DELETE))
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
    }

    @Test
    fun musicSliderTest() {
        screenState = SettingsScreenState(
            UserPreferences(musicOn = false, soundEffectOn = false),
            colorTheme = randomTheme()
        )
        composeTestRule.setContent {
            SettingScreen(
                screenState = screenState,
                updateMusicSetting = {
                    screenState = SettingsScreenState(
                        UserPreferences(musicOn = it, soundEffectOn = false),
                        colorTheme = randomTheme()
                    )
                },
                updateSoundEffectSetting = {},
                clearScoreboard = {}
            )
        }
        assertFalse(screenState.userPreferences.musicOn)
        composeTestRule.onNode(hasTestTag(Semantics.SWITCH_SETTINGS_MUSIC))
            .assertIsOff().performClick()
        assertTrue(screenState.userPreferences.musicOn)
    }

    @Test
    fun soundEffectSlider() {
        var screenState = SettingsScreenState(
            UserPreferences(musicOn = false, soundEffectOn = false),
            colorTheme = randomTheme()
        )
        composeTestRule.setContent {
            SettingScreen(
                screenState = screenState,
                updateMusicSetting = {
                },
                updateSoundEffectSetting = {
                    screenState = SettingsScreenState(
                        UserPreferences(musicOn = false, soundEffectOn = it),
                        colorTheme = randomTheme()
                    )
                },
                clearScoreboard = {}
            )
        }
        assertFalse(screenState.userPreferences.soundEffectOn)
        composeTestRule.onNode(hasTestTag(Semantics.SWITCH_SETTINGS_SOUND))
            .assertIsOff().performClick()
        assertTrue(screenState.userPreferences.soundEffectOn)
    }

    @Test
    fun deleteScoreboardButton() {
        composeTestRule.setContent {
            SettingScreen(
                screenState = SettingsScreenState(
                    UserPreferences(musicOn = false, soundEffectOn = false),
                    colorTheme = randomTheme()
                ),
                updateMusicSetting = {},
                updateSoundEffectSetting = {},
                clearScoreboard = {}
            )
        }
        composeTestRule.onNode(hasTestTag(Semantics.ALERT_SETTINGS)).assertDoesNotExist()
        composeTestRule.onNode(hasTestTag(Semantics.BTN_SETTINGS_DELETE))
            .performClick()
        composeTestRule.onNode(hasTestTag(Semantics.ALERT_SETTINGS)).assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun dismissAlertDialog() {
        composeTestRule.setContent {
            SettingScreen(
                screenState = SettingsScreenState(
                    UserPreferences(musicOn = false, soundEffectOn = false),
                    colorTheme = randomTheme()
                ),
                updateMusicSetting = {},
                updateSoundEffectSetting = {},
                clearScoreboard = {}
            )
        }
        composeTestRule.onNode(hasTestTag(Semantics.ALERT_SETTINGS)).assertDoesNotExist()
        composeTestRule.onNode(hasTestTag(Semantics.BTN_ALERT_DISMISS)).assertDoesNotExist()
        composeTestRule.onNode(hasTestTag(Semantics.BTN_ALERT_CONFIRM)).assertDoesNotExist()
        composeTestRule.onNode(hasTestTag(Semantics.BTN_SETTINGS_DELETE))
            .performClick()
        composeTestRule.onNode(hasTestTag(Semantics.ALERT_SETTINGS)).assertExists()
            .assertIsDisplayed()
        composeTestRule.onNode(hasTestTag(Semantics.BTN_ALERT_DISMISS)).assertExists()
            .assertIsDisplayed().performClick()
        composeTestRule.onNode(hasTestTag(Semantics.ALERT_SETTINGS)).assertDoesNotExist()
        composeTestRule.onNode(hasTestTag(Semantics.BTN_ALERT_DISMISS)).assertDoesNotExist()
    }

    @Test
    fun confirmAlertDialog() {
        composeTestRule.setContent {
            SettingScreen(
                screenState = SettingsScreenState(
                    UserPreferences(musicOn = false, soundEffectOn = false),
                    colorTheme = randomTheme()
                ),
                updateMusicSetting = {},
                updateSoundEffectSetting = {},
                clearScoreboard = {}
            )
        }
        composeTestRule.onNode(hasTestTag(Semantics.ALERT_SETTINGS)).assertDoesNotExist()
        composeTestRule.onNode(hasTestTag(Semantics.BTN_ALERT_DISMISS)).assertDoesNotExist()
        composeTestRule.onNode(hasTestTag(Semantics.BTN_ALERT_CONFIRM)).assertDoesNotExist()
        composeTestRule.onNode(hasTestTag(Semantics.BTN_SETTINGS_DELETE))
            .performClick()
        composeTestRule.onNode(hasTestTag(Semantics.ALERT_SETTINGS)).assertExists()
            .assertIsDisplayed()
        composeTestRule.onNode(hasTestTag(Semantics.BTN_ALERT_CONFIRM)).assertExists()
            .assertIsDisplayed().performClick()
        composeTestRule.onNode(hasTestTag(Semantics.ALERT_SETTINGS)).assertDoesNotExist()
        composeTestRule.onNode(hasTestTag(Semantics.BTN_ALERT_CONFIRM)).assertDoesNotExist()
    }
}
