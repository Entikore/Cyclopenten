package de.entikore.cyclopenten.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import de.entikore.cyclopenten.R
import de.entikore.cyclopenten.ui.screen.StartScreen
import de.entikore.cyclopenten.ui.screen.difficulty.DifficultyScreen
import de.entikore.cyclopenten.ui.screen.game.GameScreen
import de.entikore.cyclopenten.ui.screen.score.ScoreScreen
import de.entikore.cyclopenten.ui.screen.settings.SettingsScreen
import de.entikore.cyclopenten.util.Constants

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    val navArgDifficulty = Constants.DIFFICULTY
    val navArgScore = stringResource(R.string.nav_arg_score)
    val navArgWinner = stringResource(R.string.nav_arg_winner)

    NavHost(
        navController = navController,
        startDestination = CyclopentenScreen.Start.name,
        modifier = modifier.fillMaxSize(),
    ) {
        composable(CyclopentenScreen.Start.name) {
            StartScreen(
                hiltViewModel(),
                onNewGameClick = { navController.navigate(CyclopentenScreen.Difficulty.name) },
                onContinueClick = { difficulty ->
                    navigateToGameScreen(
                        navController = navController,
                        hardDifficulty = difficulty,
                    )
                },
                onScoreClick = { navController.navigate(CyclopentenScreen.Score.name) },
                onSettingsClick = { navController.navigate(CyclopentenScreen.Settings.name) },
            )
        }
        composable(CyclopentenScreen.Difficulty.name) {
            DifficultyScreen(onClick = { difficulty ->
                navigateToGameScreen(
                    navController = navController,
                    hardDifficulty = difficulty,
                )
            })
        }
        composable(CyclopentenScreen.Game.name) {
            GameScreen(viewModel = hiltViewModel()) { score, won, difficulty ->
                navigateToScoreboard(
                    score = score,
                    won = won,
                    hardDifficulty = difficulty,
                    navController = navController,
                )
            }
        }
        composable(
            route = "${CyclopentenScreen.Game.name}/{$navArgDifficulty}",
            arguments =
            listOf(
                navArgument(navArgDifficulty) {
                    type = NavType.BoolType
                },
            ),
        ) {
            val hardDifficulty = it.arguments?.getBoolean(navArgDifficulty) ?: false
            GameScreen(
                viewModel = hiltViewModel(),
                hardDifficulty = hardDifficulty,
            ) { score, won, difficulty ->
                navigateToScoreboard(
                    score = score,
                    won = won,
                    hardDifficulty = difficulty,
                    navController = navController,
                )
            }
        }
        composable(CyclopentenScreen.Score.name) {
            ScoreScreen(hiltViewModel())
        }
        composable(
            route =
            "${CyclopentenScreen.Score.name}/" +
                "{$navArgScore}/{$navArgWinner}/{$navArgDifficulty}",
            arguments =
            listOf(
                navArgument(navArgScore) {
                    type = NavType.IntType
                },
                navArgument(navArgWinner) {
                    type = NavType.BoolType
                },
                navArgument(navArgDifficulty) {
                    type = NavType.BoolType
                },
            ),
        ) {
            val score = it.arguments?.getInt(navArgScore) ?: 0
            val winner = it.arguments?.getBoolean(navArgWinner) ?: false
            val hardDifficulty = it.arguments?.getBoolean(navArgDifficulty) ?: false
            ScoreScreen(hiltViewModel(), score, winner, hardDifficulty)
        }
        composable(CyclopentenScreen.Settings.name) {
            SettingsScreen(hiltViewModel())
        }
    }
}

private fun navigateToGameScreen(navController: NavController, hardDifficulty: Boolean) {
    navController.navigate("${CyclopentenScreen.Game.name}/$hardDifficulty") {
        popUpTo(navController.graph.findStartDestination().id)
    }
}

private fun navigateToScoreboard(navController: NavController, score: Int, won: Boolean, hardDifficulty: Boolean) {
    navController.navigate("${CyclopentenScreen.Score.name}/$score/$won/$hardDifficulty") {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
