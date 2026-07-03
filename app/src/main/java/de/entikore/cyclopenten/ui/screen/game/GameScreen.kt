package de.entikore.cyclopenten.ui.screen.game

import android.content.res.Configuration
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    onGameOver: (Int, Boolean, Boolean) -> Unit,
) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    val soundEffectOn by viewModel.soundEffect.collectAsStateWithLifecycle()

    val onButtonClick = viewModel::evaluateAnswer

    val mContext = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current

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

    val correctAnswerClick: (String) -> Unit = {
        onButtonClick(it)
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        if (soundEffectOn) {
            correctAnswer.start()
        }
    }

    val wrongAnswerClick: (String) -> Unit = {
        onButtonClick(it)
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        if (soundEffectOn) {
            wrongAnswer.start()
        }
    }

    val showLoading by viewModel.showLoading.collectAsStateWithLifecycle()
    AnimatedGameScreen(
        gameState,
        showLoading,
        correctAnswerClick,
        wrongAnswerClick,
        hardDifficulty = hardDifficulty,
    )
}

@Composable
fun AnimatedGameScreen(
    gameState: GameScreenState,
    showLoading: Boolean,
    correctAnswerClick: (String) -> Unit,
    wrongAnswerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    hardDifficulty: Boolean = false,
) {
    val configuration = LocalConfiguration.current
    val useWideLayout = configuration.screenWidthDp >= 600 ||
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
        modifier
            .fillMaxSize()
            .background(gameState.colorTheme.primary)
            .semantics { contentDescription = CD_GAME_SCREEN }
            .padding(16.dp),
    ) {
        Title(
            title = stringResource(R.string.txt_game_title),
            textColor = gameState.colorTheme.accent,
        )

        if (useWideLayout) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Left Pane: Status & Card
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    GameStatus(
                        gameState.colorTheme.accent,
                        gameState.lives,
                        gameState.lostLives,
                        gameState.score,
                    )

                    ElementCard(
                        gameState.colorTheme.primary,
                        gameState.colorTheme.dark,
                        gameState.colorTheme.accent,
                        gameState.atomicNumber.toString(),
                        gameState.symbol,
                        gameState.hiddenElementName(),
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .aspectRatio(1f),
                    )

                    LinearProgressIndicator(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .alpha(if (showLoading) 1f else 0f),
                        color = gameState.colorTheme.dark,
                        backgroundColor = gameState.colorTheme.accent,
                    )
                }

                // Right Pane: Input Options (max width capped to avoid stretched layouts)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .fillMaxWidth(0.9f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    if (hardDifficulty) {
                        AnswerInputHard(
                            gameState.element,
                            gameState.colorTheme,
                            correctAnswerClick,
                            wrongAnswerClick,
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
                            wrongAnswerClick,
                        )
                    }
                }
            }
        } else {
            // Normal vertical portrait layout
            GameStatus(
                gameState.colorTheme.accent,
                gameState.lives,
                gameState.lostLives,
                gameState.score,
            )

            ElementCard(
                gameState.colorTheme.primary,
                gameState.colorTheme.dark,
                gameState.colorTheme.accent,
                gameState.atomicNumber.toString(),
                gameState.symbol,
                gameState.hiddenElementName(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 64.dp)
                    .aspectRatio(1f),
            )

            LinearProgressIndicator(
                modifier =
                Modifier
                    .padding(8.dp)
                    .alpha(if (showLoading) 1f else 0f),
                color = gameState.colorTheme.dark,
                backgroundColor = gameState.colorTheme.accent,
            )
            if (hardDifficulty) {
                AnswerInputHard(
                    gameState.element,
                    gameState.colorTheme,
                    correctAnswerClick,
                    wrongAnswerClick,
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
                    wrongAnswerClick,
                )
            }
        }
    }
}

@Composable
fun GameStatus(uiColor: Color, lives: Int, lostLives: Int, score: Int, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier =
        modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 24.dp),
    ) {
        val totalLives = lives + lostLives
        val livesAccessibilityText = stringResource(R.string.txt_lives_accessibility, lives, totalLives)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.semantics(mergeDescendants = true) {
                contentDescription = livesAccessibilityText
            },
        ) {
            Text(
                text = stringResource(R.string.txt_lives),
                color = uiColor,
                fontSize = MaterialTheme.typography.h5.fontSize,
                modifier = Modifier.padding(start = 32.dp, end = 4.dp),
            )
            repeat(lives) {
                Icon(
                    painterResource(R.drawable.favorite),
                    stringResource(R.string.content_description_lives_symbol),
                    tint = uiColor,
                )
            }
            repeat(lostLives) {
                Icon(
                    painterResource(R.drawable.heart_broken),
                    stringResource(R.string.content_description_lost_lives_symbol),
                    tint = uiColor,
                )
            }
        }
        Text(
            text = stringResource(R.string.txt_score) + score,
            color = uiColor,
            fontSize = MaterialTheme.typography.h5.fontSize,
            modifier = Modifier.padding(horizontal = 32.dp),
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
    answer: String,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier =
        modifier
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .border(
                width = 8.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp),
            )
            .semantics(mergeDescendants = true) {},
    ) {
        Text(
            text = atomicNumber,
            color = textColor,
            fontSize = MaterialTheme.typography.h4.fontSize,
            modifier =
            Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, top = 16.dp),
        )
        Text(
            text = symbol,
            color = textColor,
            fontSize = MaterialTheme.typography.h1.fontSize,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Text(
            text = answer,
            fontSize = MaterialTheme.typography.h5.fontSize,
            color = textColor,
            modifier =
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp),
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
    modifier: Modifier = Modifier,
) {
    val answerOptions =
        remember(choices) {
            choices.shuffled()
        }

    Column(verticalArrangement = Arrangement.Center, modifier = modifier.padding(16.dp)) {
        ButtonRow(
            correctAnswer,
            answerOptions.subList(0, 2),
            enabled,
            backgroundColor,
            borderColor,
            textColor,
            correctAnswerClick,
            wrongAnswerClick,
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
        )
    }
}

@Composable
fun AnswerInputHard(
    correctAnswer: String,
    colorTheme: ColorTheme,
    correctAnswerClick: (guess: String) -> Unit,
    wrongAnswerClick: (guess: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val onClick: (String) -> Unit = { answer ->
        if (answer.lowercase() == correctAnswer.lowercase()) {
            correctAnswerClick(answer)
        } else {
            wrongAnswerClick(answer)
        }
    }
    ColoredTextInput(colorTheme = colorTheme, onClick = onClick, modifier = modifier.padding(24.dp))
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
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxWidth(),
    ) {
        val buttonModifier =
            Modifier
                .fillMaxWidth(fraction = 0.5f)
                .padding(horizontal = 4.dp)
                .weight(1f)
        ColoredButton(
            buttonText = choices[0],
            enabled = enabled,
            textColor = textColor,
            onClick =
            if (choices[0] == correctAnswer) {
                { onButtonClick(choices[0]) }
            } else {
                { wrongAnswerClick(choices[0]) }
            },
            color = backgroundColor,
            borderStroke = BorderStroke(2.dp, borderColor),
            modifier = buttonModifier,
        )
        ColoredButton(
            buttonText = choices[1],
            enabled = enabled,
            textColor = textColor,
            onClick =
            if (choices[1] == correctAnswer) {
                { onButtonClick(choices[1]) }
            } else {
                { wrongAnswerClick(choices[1]) }
            },
            color = backgroundColor,
            borderStroke = BorderStroke(2.dp, borderColor),
            modifier = buttonModifier,
        )
    }
}
