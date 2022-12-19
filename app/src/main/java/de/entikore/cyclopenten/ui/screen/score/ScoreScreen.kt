package de.entikore.cyclopenten.ui.screen.score

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import de.entikore.cyclopenten.R
import de.entikore.cyclopenten.data.local.entity.Highscore
import de.entikore.cyclopenten.ui.components.ColoredTextInput
import de.entikore.cyclopenten.ui.components.Title
import de.entikore.cyclopenten.ui.theme.ColorTheme
import de.entikore.cyclopenten.util.Semantics.CD_SCORE_SCREEN

@Composable
fun ScoreScreen(viewModel: ScoreViewModel, score: Int, won: Boolean, hardMode: Boolean) {
    val scoreboard by viewModel.scoreBoard.collectAsState()
    val showNameField by viewModel.showNameField.collectAsState()
    val colorTheme by viewModel.colorTheme.collectAsState()
    val gameEndMessage =
        if (won) stringResource(R.string.txt_game_won) else stringResource(R.string.txt_game_lost)
    val scoreMessage = String.format(stringResource(R.string.txt_final_score), score)
    ScoreScreen(
        screenState = ScoreScreenState(
            scoreboard,
            showNameField,
            gameEndMessage,
            scoreMessage,
            score,
            hardMode,
            colorTheme
        ),
        saveScore = viewModel::saveScore
    )
}

@Composable
fun ScoreScreen(
    screenState: ScoreScreenState,
    saveScore: (name: String, score: Int, hardMode: Boolean) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = screenState.colorTheme.primary)
    ) {
        ScoreBoard(
            scoreboard = screenState.scoreboard,
            gameEndMessage = listOf(screenState.gameEndMessage, screenState.scoreMessage),
            colorTheme = screenState.colorTheme,
            modifier = Modifier.weight(1f)
        )
        if (screenState.scoreboard.size < 10 ||
            screenState.scoreboard.any { it.score < screenState.score }
        ) {
            if (screenState.showNameField) {
                ScoreNameField(
                    saveScore,
                    screenState.score,
                    screenState.hardMode,
                    screenState.colorTheme
                )
            }
        }
    }
}

@Composable
fun ScoreNameField(
    saveScore: (String, Int, Boolean) -> Unit,
    score: Int,
    hardMode: Boolean,
    colorTheme: ColorTheme
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val onClick: (name: String) -> Unit =
            { name ->
                saveScore(name, score, hardMode)
            }
        ColoredTextInput(colorTheme = colorTheme, onClick = onClick)
    }
}

@Composable
fun ScoreScreen(viewModel: ScoreViewModel) {
    val scoreboard by viewModel.scoreBoard.collectAsState()
    val colorTheme by viewModel.colorTheme.collectAsState()
    ScoreBoard(scoreboard, colorTheme = colorTheme)
}

@Composable
fun ScoreBoard(
    scoreboard: List<Highscore>,
    modifier: Modifier = Modifier,
    gameEndMessage: List<String> = emptyList(),
    colorTheme: ColorTheme
) {
    LazyColumn(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(colorTheme.primary)
            .semantics { contentDescription = CD_SCORE_SCREEN }
    ) {
        item {
            Title(title = stringResource(R.string.txt_scoreboard), textColor = colorTheme.accent)
        }
        if (gameEndMessage.isNotEmpty()) {
            items(gameEndMessage) { message ->
                Text(
                    text = message,
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    color = colorTheme.dark
                )
            }
        }
        if (scoreboard.isNotEmpty()) {
            item {
                Divider(
                    color = colorTheme.dark,
                    thickness = 2.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
        itemsIndexed(scoreboard) { index, item ->
            ScoreboardEntry(
                index.inc(),
                item,
                colorTheme.dark,
                Modifier.testTag(tag = "ScoreboardEntry ${index.inc()}")
            )
        }
        if (scoreboard.isNotEmpty()) {
            item {
                Divider(
                    color = colorTheme.dark,
                    thickness = 2.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
fun ScoreboardEntry(place: Int, entry: Highscore, color: Color, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 16.dp)
    ) {
        val difficulty: String = if (entry.hardMode) "Hard" else ""
        Text(text = "$place.", color = color)
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        Text(text = entry.name, color = color)
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        Text(text = "${entry.score} $difficulty", color = color)
    }
}
