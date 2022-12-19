package de.entikore.cyclopenten.ui.navigation

import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import de.entikore.cyclopenten.FakeTestData
import de.entikore.cyclopenten.MainActivity
import de.entikore.cyclopenten.R
import de.entikore.cyclopenten.data.local.ChemicalElementDatabase
import de.entikore.cyclopenten.util.Semantics.CD_DIFFICULTY_SCREEN
import de.entikore.cyclopenten.util.Semantics.CD_GAME_SCREEN
import de.entikore.cyclopenten.util.Semantics.CD_SCORE_SCREEN
import de.entikore.cyclopenten.util.Semantics.CD_SETTINGS_SCREEN
import de.entikore.cyclopenten.util.Semantics.CD_START_SCREEN
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    lateinit var navController: TestNavHostController

    @Inject
    lateinit var database: ChemicalElementDatabase

    private fun hasText(@StringRes resId: Int) = hasText(composeTestRule.activity.getString(resId))

    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(
                ComposeNavigator()
            )
            NavGraph(navController = navController)
        }
    }

    @Test
    fun navigateFromStartToNewGameEasy() {
        composeTestRule.apply {
            onNodeWithContentDescription(CD_START_SCREEN).assertIsDisplayed()
            onNode(hasText(R.string.btn_new_game)).assertIsDisplayed().performClick()

            onNodeWithContentDescription(CD_DIFFICULTY_SCREEN).assertIsDisplayed()
            onNode(hasText(R.string.btn_difficulty_easy)).assertIsDisplayed().performClick()

            onNodeWithContentDescription(CD_GAME_SCREEN).assertIsDisplayed()

            activityRule.scenario.onActivity { activity ->
                activity.onBackPressedDispatcher.onBackPressed()
            }
            onNodeWithContentDescription(CD_START_SCREEN).assertIsDisplayed()
        }
    }

    @Test
    fun navigateFromStartToNewGameHard() {
        composeTestRule.apply {
            onNodeWithContentDescription(CD_START_SCREEN).assertIsDisplayed()
            onNode(hasText(R.string.btn_new_game)).assertIsDisplayed().performClick()

            onNodeWithContentDescription(CD_DIFFICULTY_SCREEN).assertIsDisplayed()
            onNode(hasText(R.string.btn_difficulty_hard)).assertIsDisplayed().performClick()

            onNodeWithContentDescription(CD_GAME_SCREEN).assertIsDisplayed()

            activityRule.scenario.onActivity { activity ->
                activity.onBackPressedDispatcher.onBackPressed()
            }
            onNodeWithContentDescription(CD_START_SCREEN).assertIsDisplayed()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun navigateFromStartWithContinueEnabled() = runTest {
        // add save game to enable continue button
        database.chemicalElementDao().saveGame(FakeTestData.saveGame)

        composeTestRule.apply {
            onNodeWithContentDescription(CD_START_SCREEN).assertIsDisplayed()
            onNode(hasText(R.string.btn_continue)).assertIsDisplayed().assertIsEnabled()
                .performClick()

            onNodeWithContentDescription(CD_GAME_SCREEN).assertIsDisplayed()

            activityRule.scenario.onActivity { activity ->
                activity.onBackPressedDispatcher.onBackPressed()
            }
            onNodeWithContentDescription(CD_START_SCREEN).assertIsDisplayed()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun navigateFromStartWithContinueDisabled() = runTest {
        // delete save game to disable continue button
        database.chemicalElementDao().deleteSaveGame()

        composeTestRule.apply {
            onNodeWithContentDescription(CD_START_SCREEN).assertIsDisplayed()
            onNode(hasText(R.string.btn_continue)).assertIsDisplayed().assertIsNotEnabled()
        }
    }

    @Test
    fun navigateFromStartToScoreboard() {
        composeTestRule.apply {
            onNodeWithContentDescription(CD_START_SCREEN).assertIsDisplayed()
            onNode(hasText(R.string.btn_highscore)).assertIsDisplayed().performClick()

            onNodeWithContentDescription(CD_SCORE_SCREEN).assertIsDisplayed()

            activityRule.scenario.onActivity { activity ->
                activity.onBackPressedDispatcher.onBackPressed()
            }
            onNodeWithContentDescription(CD_START_SCREEN).assertIsDisplayed()
        }
    }

    @Test
    fun navigateFromStartToSettings() {
        composeTestRule.apply {
            onNodeWithContentDescription(CD_START_SCREEN).assertIsDisplayed()
            onNode(hasText(R.string.btn_settings)).assertIsDisplayed().performClick()

            onNodeWithContentDescription(CD_SETTINGS_SCREEN).assertIsDisplayed()

            activityRule.scenario.onActivity { activity ->
                activity.onBackPressedDispatcher.onBackPressed()
            }
            onNodeWithContentDescription(CD_START_SCREEN).assertIsDisplayed()
        }
    }
}
