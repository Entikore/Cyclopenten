package de.entikore.cyclopenten.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import de.entikore.cyclopenten.R
import de.entikore.cyclopenten.ui.components.ColoredButton
import de.entikore.cyclopenten.ui.components.Title
import de.entikore.cyclopenten.ui.theme.randomTheme
import de.entikore.cyclopenten.util.Semantics.CD_START_SCREEN

@Composable
fun StartScreen(
    viewModel: StartScreenViewModel,
    onNewGameClicked: () -> Unit,
    onContinueClicked: (Boolean) -> Unit,
    onScoreClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
) {
    val continueEnabled = viewModel.enabled.collectAsState(initial = false)
    val saveGame = viewModel.saveGame.collectAsState()
    val saveGameDifficulty = saveGame.value?.difficulty ?: false
    val newGameClickListener: () -> Unit = {
        viewModel::deleteSaveGame.invoke()
        onNewGameClicked.invoke()
    }

    StartScreen(
        newGameClickListener,
        onContinueClicked,
        onScoreClicked,
        onSettingsClicked,
        continueEnabled.value,
        saveGameDifficulty
    )
}

@Composable
fun StartScreen(
    onNewGameClicked: () -> Unit,
    onContinueClicked: (Boolean) -> Unit,
    onScoreClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    continueEnabled: Boolean,
    saveGameDifficulty: Boolean
) {
    val colorTheme = remember { mutableStateOf(randomTheme()) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .background(colorTheme.value.primary)
            .semantics { contentDescription = CD_START_SCREEN }
    ) {
        Title(
            title = stringResource(id = R.string.app_name),
            textColor = colorTheme.value.accent,
            fontSize = MaterialTheme.typography.h2.fontSize,
            modifier = Modifier.padding(top = 8.dp)
        )

        Image(
            painter = painterResource(R.drawable.cyclopenten),
            contentDescription = stringResource(R.string.app_logo),
            colorFilter = ColorFilter.tint(colorTheme.value.dark),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            val modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
            ColoredButton(
                buttonText = stringResource(id = R.string.btn_new_game),
                onClick = onNewGameClicked,
                textColor = colorTheme.value.dark,
                color = colorTheme.value.accent,
                borderStroke = BorderStroke(width = 2.dp, color = colorTheme.value.dark),
                modifier = modifier
            )
            ColoredButton(
                buttonText = stringResource(id = R.string.btn_continue),
                enabled = continueEnabled,
                onClick = { onContinueClicked(saveGameDifficulty) },
                textColor = colorTheme.value.dark,
                color = colorTheme.value.accent,
                disabledColor = colorTheme.value.primary,
                borderStroke = BorderStroke(width = 2.dp, color = colorTheme.value.dark),
                modifier = modifier
            )
            ColoredButton(
                buttonText = stringResource(id = R.string.btn_highscore),
                onClick = onScoreClicked,
                textColor = colorTheme.value.dark,
                color = colorTheme.value.accent,
                borderStroke = BorderStroke(width = 2.dp, color = colorTheme.value.dark),
                modifier = modifier
            )
            ColoredButton(
                buttonText = stringResource(id = R.string.btn_settings),
                onClick = onSettingsClicked,
                textColor = colorTheme.value.dark,
                color = colorTheme.value.accent,
                borderStroke = BorderStroke(width = 2.dp, color = colorTheme.value.dark),
                modifier = modifier
            )
        }
    }
}
