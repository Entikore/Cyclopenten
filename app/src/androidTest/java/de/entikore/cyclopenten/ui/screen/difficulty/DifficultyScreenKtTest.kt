package de.entikore.cyclopenten.ui.screen.difficulty

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import de.entikore.cyclopenten.util.Semantics.ALERT_DIFFICULTY
import de.entikore.cyclopenten.util.Semantics.BTN_DIFFICULTY_EASY
import de.entikore.cyclopenten.util.Semantics.BTN_DIFFICULTY_HARD
import de.entikore.cyclopenten.util.Semantics.BTN_DIFFICULTY_HELP
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DifficultyScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        composeTestRule.setContent {
            DifficultyScreen {}
        }
    }

    @Test
    fun difficultyScreenDisplay() {
        composeTestRule.onNode(hasTestTag(BTN_DIFFICULTY_EASY)).assertExists()
            .assertIsDisplayed().assertIsEnabled().assertHasClickAction()
        composeTestRule.onNode(hasTestTag(BTN_DIFFICULTY_HARD)).assertExists()
            .assertIsDisplayed().assertIsEnabled().assertHasClickAction()
        composeTestRule.onNode(hasTestTag(BTN_DIFFICULTY_HELP)).assertExists()
            .assertIsDisplayed().assertIsEnabled().assertHasClickAction()
        composeTestRule.onNode(hasTestTag(ALERT_DIFFICULTY)).assertDoesNotExist()
    }

    @Test
    fun difficultyScreenHelpButton() {
        composeTestRule.onNode(hasTestTag(ALERT_DIFFICULTY)).assertDoesNotExist()
        composeTestRule.onNode(hasTestTag(BTN_DIFFICULTY_HELP)).performClick()
        composeTestRule.onNode(hasTestTag(ALERT_DIFFICULTY)).assertExists()
            .assertIsDisplayed()
    }
}
