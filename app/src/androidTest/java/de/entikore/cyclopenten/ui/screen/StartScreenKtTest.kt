package de.entikore.cyclopenten.ui.screen

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import de.entikore.cyclopenten.R
import org.junit.Rule
import org.junit.Test

class StartScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun hasText(@StringRes resId: Int) =
        hasText(composeTestRule.activity.getString(resId))

    @Test
    fun startScreenWithoutSaveGame() {
        composeTestRule.setContent {
            StartScreen(
                onNewGameClicked = { },
                onContinueClicked = { },
                onScoreClicked = { },
                onSettingsClicked = { },
                continueEnabled = false,
                saveGameDifficulty = false
            )
        }
        assertButtonNodeIsDisplayedEnabledAndHasAction(R.string.btn_new_game)
        assertButtonNodeIsDisplayedDisabledAndHasAction(R.string.btn_continue)
        assertButtonNodeIsDisplayedEnabledAndHasAction(R.string.btn_highscore)
        assertButtonNodeIsDisplayedEnabledAndHasAction(R.string.btn_settings)
    }

    @Test
    fun startScreenWithSaveGame() {
        composeTestRule.setContent {
            StartScreen(
                onNewGameClicked = { },
                onContinueClicked = { },
                onScoreClicked = { },
                onSettingsClicked = { },
                continueEnabled = true,
                saveGameDifficulty = false
            )
        }
        assertButtonNodeIsDisplayedEnabledAndHasAction(R.string.btn_new_game)
        assertButtonNodeIsDisplayedEnabledAndHasAction(R.string.btn_continue)
        assertButtonNodeIsDisplayedEnabledAndHasAction(R.string.btn_highscore)
        assertButtonNodeIsDisplayedEnabledAndHasAction(R.string.btn_settings)
    }

    private fun assertButtonNodeIsDisplayedEnabledAndHasAction(stringId: Int) {
        composeTestRule.onNode(hasText(stringId))
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
    }

    private fun assertButtonNodeIsDisplayedDisabledAndHasAction(stringId: Int) {
        composeTestRule.onNode(hasText(stringId))
            .assertIsDisplayed()
            .assertIsNotEnabled()
            .assertHasClickAction()
    }
}
