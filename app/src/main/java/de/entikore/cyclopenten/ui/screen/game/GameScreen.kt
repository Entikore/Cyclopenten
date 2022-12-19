package de.entikore.cyclopenten.ui.screen.game

import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.HeartBroken
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import de.entikore.cyclopenten.R
import de.entikore.cyclopenten.ui.components.ColoredButton
import de.entikore.cyclopenten.ui.components.ColoredTextInput
import de.entikore.cyclopenten.ui.components.Title
import de.entikore.cyclopenten.ui.theme.ColorTheme
import de.entikore.cyclopenten.util.Semantics.CD_GAME_SCREEN

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    hardDifficulty: Boolean = false,
    onGameOver: (Int, Boolean, Boolean) -> Unit
) {
    val gameState by viewModel.gameState.collectAsState()
    val soundEffectOn by viewModel.soundEffect.collectAsState()

    val onButtonClick = viewModel::evaluateAnswer

    val mContext = LocalContext.current

    val correctAnswer = MediaPlayer.create(mContext, R.raw.correct_answer)
    val wrongAnswer = MediaPlayer.create(mContext, R.raw.wrong_answer)

    if (gameState.gameOver) {
        correctAnswer.release()
        wrongAnswer.release()
        val finalScore = gameState.score
        val winner = gameState.won()
        val difficulty = gameState.hardDifficulty
        viewModel.resetGameState()
        onGameOver(finalScore, winner, difficulty)
    }

    val correctAnswerClick: (String) -> Unit = { it ->
        onButtonClick(it)
        if (soundEffectOn) {
            correctAnswer.start()
        }
    }

    val wrongAnswerClick: (String) -> Unit = {
        onButtonClick(it)
        if (soundEffectOn) {
            wrongAnswer.start()
        }
    }

    val showLoading by viewModel.showLoading.collectAsState()
    AnimatedGameScreen(gameState, showLoading, hardDifficulty, correctAnswerClick, wrongAnswerClick)
}

@Composable
fun AnimatedGameScreen(
    gameState: GameScreenState,
    showLoading: Boolean,
    hardDifficulty: Boolean = false,
    correctAnswerClick: (String) -> Unit,
    wrongAnswerClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(gameState.colorTheme.primary)
            .semantics { contentDescription = CD_GAME_SCREEN }
    ) {
        Title(
            title = stringResource(R.string.txt_game_title),
            textColor = gameState.colorTheme.accent
        )

        GameStatus(
            gameState.colorTheme.accent,
            gameState.lives,
            gameState.lostLives,
            gameState.score
        )

        ElementCard(
            gameState.colorTheme.primary,
            gameState.colorTheme.dark,
            gameState.colorTheme.accent,
            gameState.atomicNumber.toString(),
            gameState.symbol,
            gameState.hiddenElementName()
        )

        LinearProgressIndicator(
            modifier = Modifier
                .padding(8.dp)
                .alpha(if (showLoading) 1f else 0f),
            color = gameState.colorTheme.dark,
            backgroundColor = gameState.colorTheme.accent
        )
        if (hardDifficulty) {
            AnswerInputHard(
                gameState.element,
                gameState.colorTheme,
                correctAnswerClick,
                wrongAnswerClick
            )
        } else {
            AnswerInput(
                gameState.element,
                gameState.answerOptions,
                gameState.hidden,
                gameState.colorTheme.accent,
                gameState.colorTheme.dark,
                gameState.colorTheme.dark,
                correctAnswerClick,
                wrongAnswerClick
            )
        }
    }
}

@Composable
fun GameStatus(uiColor: Color, lives: Int, lostLives: Int, score: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.txt_lives),
                color = uiColor,
                fontSize = MaterialTheme.typography.h5.fontSize,
                modifier = Modifier.padding(start = 32.dp, end = 4.dp)
            )
            for (i in 1..lives) {
                Icon(
                    Icons.Rounded.Favorite,
                    stringResource(R.string.content_description_lives_symbol),
                    tint = uiColor
                )
            }
            for (i in 1..lostLives) {
                Icon(
                    Icons.Rounded.HeartBroken,
                    stringResource(R.string.content_description_lost_lives_symbol),
                    tint = uiColor
                )
            }
        }
        Text(
            text = stringResource(R.string.txt_score) + score,
            color = uiColor,
            fontSize = MaterialTheme.typography.h5.fontSize,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
fun ElementCard(
    backgroundColor: Color,
    borderColor: Color,
    textColor: Color,
    atomicNumber: String,
    symbol: String,
    answer: String
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 64.dp)
            .aspectRatio(1f)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .border(
                width = 8.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            text = atomicNumber,
            color = textColor,
            fontSize = MaterialTheme.typography.h4.fontSize,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, top = 16.dp)
        )
        Text(
            text = symbol,
            color = textColor,
            fontSize = MaterialTheme.typography.h1.fontSize,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = answer,
            fontSize = MaterialTheme.typography.h5.fontSize,
            color = textColor,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp)
        )
    }
}

@Composable
fun AnswerInput(
    correctAnswer: String,
    choices: List<String>,
    enabled: Boolean,
    backgroundColor: Color,
    borderColor: Color,
    textColor: Color,
    correctAnswerClick: (guess: String) -> Unit,
    wrongAnswerClick: (guess: String) -> Unit,
) {
    val answerOptions = remember(choices) {
        choices.shuffled()
    }

    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.padding(16.dp)) {
        val modifier = Modifier
            .fillMaxWidth(fraction = 0.5f)
            .padding(horizontal = 4.dp)
            .weight(1f)
        ButtonRow(
            correctAnswer,
            answerOptions.subList(0, 2),
            enabled,
            backgroundColor,
            borderColor,
            textColor,
            correctAnswerClick,
            wrongAnswerClick,
            modifier
        )
        ButtonRow(
            correctAnswer,
            answerOptions.subList(2, choices.size),
            enabled,
            backgroundColor,
            borderColor,
            textColor,
            correctAnswerClick,
            wrongAnswerClick,
            modifier
        )
    }
}

@Composable
fun AnswerInputHard(
    correctAnswer: String,
    colorTheme: ColorTheme,
    correctAnswerClick: (guess: String) -> Unit,
    wrongAnswerClick: (guess: String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(24.dp)
    ) {
        val onClick: (String) -> Unit = { answer ->
            if (answer.lowercase() == correctAnswer.lowercase()) {
                correctAnswerClick(answer)
            } else {
                wrongAnswerClick(answer)
            }
        }
        ColoredTextInput(colorTheme = colorTheme, onClick = onClick)
    }
}

@Composable
fun ButtonRow(
    correctAnswer: String,
    choices: List<String>,
    enabled: Boolean,
    backgroundColor: Color,
    borderColor: Color,
    textColor: Color,
    onButtonClick: (guess: String) -> Unit,
    wrongAnswerClick: (guess: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        ColoredButton(
            buttonText = choices[0],
            enabled = enabled,
            textColor = textColor,
            onClick = if (choices[0] == correctAnswer) {
                { onButtonClick(choices[0]) }
            } else {
                { wrongAnswerClick(choices[0]) }
            },
            color = backgroundColor,
            borderStroke = BorderStroke(2.dp, borderColor),
            modifier = modifier
        )
        ColoredButton(
            buttonText = choices[1],
            enabled = enabled,
            textColor = textColor,
            onClick = if (choices[1] == correctAnswer) {
                { onButtonClick(choices[1]) }
            } else {
                { wrongAnswerClick(choices[1]) }
            },
            color = backgroundColor,
            borderStroke = BorderStroke(2.dp, borderColor),
            modifier = modifier
        )
    }
}
