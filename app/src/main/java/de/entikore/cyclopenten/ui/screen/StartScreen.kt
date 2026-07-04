package de.entikore.cyclopenten.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.entikore.cyclopenten.R
import de.entikore.cyclopenten.ui.components.ColoredButton
import de.entikore.cyclopenten.ui.components.Title
import de.entikore.cyclopenten.ui.theme.ColorTheme
import de.entikore.cyclopenten.ui.theme.randomTheme
import de.entikore.cyclopenten.util.Semantics.CD_START_SCREEN

@Composable
fun StartScreen(
    viewModel: StartScreenViewModel,
    onNewGameClick: () -> Unit,
    onContinueClick: (Boolean) -> Unit,
    onScoreClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    val continueEnabled = viewModel.enabled.collectAsStateWithLifecycle(initialValue = false)
    val saveGame = viewModel.saveGame.collectAsStateWithLifecycle()
    val saveGameDifficulty = saveGame.value?.difficulty ?: false
    val newGameClickListener: () -> Unit = {
        viewModel::deleteSaveGame.invoke()
        onNewGameClick.invoke()
    }

    val configuration = LocalConfiguration.current
    val useWideLayout = configuration.screenWidthDp >= 600 ||
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    StartScreen(
        newGameClickListener,
        onContinueClick,
        onScoreClick,
        onSettingsClick,
        continueEnabled.value,
        saveGameDifficulty,
        useWideLayout,
    )
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun HorizontalStartScreenPreview() {
    StartScreen(
        onNewGameClick = {},
        onContinueClick = {},
        onScoreClick = {},
        onSettingsClick = {},
        isContinueEnabled = true,
        saveGameDifficulty = false,
        useWideLayout = true,
    )
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
private fun VerticalStartScreenPreview() {
    StartScreen(
        onNewGameClick = {},
        onContinueClick = {},
        onScoreClick = {},
        onSettingsClick = {},
        isContinueEnabled = true,
        saveGameDifficulty = false,
        useWideLayout = false,
    )
}

@Composable
fun StartScreen(
    onNewGameClick: () -> Unit,
    onContinueClick: (Boolean) -> Unit,
    onScoreClick: () -> Unit,
    onSettingsClick: () -> Unit,
    isContinueEnabled: Boolean,
    saveGameDifficulty: Boolean,
    useWideLayout: Boolean,
    modifier: Modifier = Modifier,
) {
    val colorTheme = remember { mutableStateOf(randomTheme()) }

    if (useWideLayout) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier =
            modifier
                .fillMaxSize()
                .background(colorTheme.value.primary)
                .semantics { contentDescription = CD_START_SCREEN },
        ) {
            Title(
                title = stringResource(id = R.string.app_name),
                textColor = colorTheme.value.accent,
                fontSize = MaterialTheme.typography.h2.fontSize,
                modifier = Modifier.padding(top = 8.dp),
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                Modifier
                    .fillMaxSize()
                    .background(colorTheme.value.primary)
                    .semantics { contentDescription = CD_START_SCREEN },
            ) {
                Image(
                    painter = painterResource(R.drawable.cyclopenten),
                    contentDescription = stringResource(R.string.app_logo),
                    colorFilter = ColorFilter.tint(colorTheme.value.dark),
                    modifier =
                    Modifier
                        .fillMaxHeight()
                        .widthIn(max = 300.dp)
                        .padding(horizontal = 8.dp),
                )

                StartScreenOptions(
                    colorTheme.value,
                    onNewGameClick,
                    onContinueClick,
                    onScoreClick,
                    onSettingsClick,
                    isContinueEnabled,
                    saveGameDifficulty,
                    modifier = Modifier.padding(bottom = 8.dp)
                        .widthIn(max = 400.dp),
                )
            }
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier =
            modifier
                .fillMaxSize()
                .background(colorTheme.value.primary)
                .semantics { contentDescription = CD_START_SCREEN },
        ) {
            Title(
                title = stringResource(id = R.string.app_name),
                textColor = colorTheme.value.accent,
                fontSize = MaterialTheme.typography.h2.fontSize,
                modifier = Modifier.padding(top = 8.dp),
            )

            Image(
                painter = painterResource(R.drawable.cyclopenten),
                contentDescription = stringResource(R.string.app_logo),
                colorFilter = ColorFilter.tint(colorTheme.value.dark),
                modifier =
                Modifier
                    .fillMaxWidth()
                    .widthIn(max = 300.dp)
                    .padding(horizontal = 48.dp),
            )

            StartScreenOptions(
                colorTheme.value,
                onNewGameClick,
                onContinueClick,
                onScoreClick,
                onSettingsClick,
                isContinueEnabled,
                saveGameDifficulty,
                modifier = Modifier.padding(bottom = 8.dp)
                    .widthIn(max = 400.dp),
            )
        }
    }
}

@Composable
fun StartScreenOptions(
    colorTheme: ColorTheme,
    onNewGameClick: () -> Unit,
    onContinueClick: (Boolean) -> Unit,
    onScoreClick: () -> Unit,
    onSettingsClick: () -> Unit,
    isContinueEnabled: Boolean,
    saveGameDifficulty: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        val buttonModifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 4.dp)
        ColoredButton(
            buttonText = stringResource(id = R.string.btn_new_game),
            onClick = onNewGameClick,
            textColor = colorTheme.dark,
            color = colorTheme.accent,
            borderStroke = BorderStroke(width = 2.dp, color = colorTheme.dark),
            modifier = buttonModifier,
        )
        ColoredButton(
            buttonText = stringResource(id = R.string.btn_continue),
            enabled = isContinueEnabled,
            onClick = { onContinueClick(saveGameDifficulty) },
            textColor = colorTheme.dark,
            color = colorTheme.accent,
            disabledColor = colorTheme.primary,
            borderStroke = BorderStroke(width = 2.dp, color = colorTheme.dark),
            modifier = buttonModifier,
        )
        ColoredButton(
            buttonText = stringResource(id = R.string.btn_highscore),
            onClick = onScoreClick,
            textColor = colorTheme.dark,
            color = colorTheme.accent,
            borderStroke = BorderStroke(width = 2.dp, color = colorTheme.dark),
            modifier = buttonModifier,
        )
        ColoredButton(
            buttonText = stringResource(id = R.string.btn_settings),
            onClick = onSettingsClick,
            textColor = colorTheme.dark,
            color = colorTheme.accent,
            borderStroke = BorderStroke(width = 2.dp, color = colorTheme.dark),
            modifier = buttonModifier,
        )
    }
}
