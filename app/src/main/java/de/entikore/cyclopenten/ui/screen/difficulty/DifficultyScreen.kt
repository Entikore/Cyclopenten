package de.entikore.cyclopenten.ui.screen.difficulty

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import de.entikore.cyclopenten.R
import de.entikore.cyclopenten.ui.components.ColoredButton
import de.entikore.cyclopenten.ui.components.Title
import de.entikore.cyclopenten.ui.theme.randomTheme
import de.entikore.cyclopenten.util.Semantics

@Composable
fun DifficultyScreen(onClick: (Boolean) -> Unit) {
    val colorTheme = remember { mutableStateOf(randomTheme()) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorTheme.value.primary)
            .semantics { contentDescription = Semantics.CD_DIFFICULTY_SCREEN }
    ) {
        val openDialog = remember { mutableStateOf(false) }

        Title(
            title = stringResource(R.string.txt_difficulty_title),
            textColor = colorTheme.value.accent,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.padding(32.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            ColoredButton(
                buttonText = stringResource(R.string.btn_difficulty_easy),
                textColor = colorTheme.value.dark,
                onClick = {
                    onClick(false)
                },
                color = colorTheme.value.accent,
                borderStroke = BorderStroke(2.dp, colorTheme.value.dark),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .testTag(Semantics.BTN_DIFFICULTY_EASY)
            )
            ColoredButton(
                buttonText = stringResource(R.string.btn_difficulty_hard),
                textColor = colorTheme.value.dark,
                onClick = {
                    onClick(true)
                },
                color = colorTheme.value.accent,
                borderStroke = BorderStroke(2.dp, colorTheme.value.dark),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .testTag(Semantics.BTN_DIFFICULTY_HARD)
            )
        }

        Spacer(modifier = Modifier.padding(32.dp))

        IconButton(
            onClick = { openDialog.value = !openDialog.value },
            modifier = Modifier
                .padding(horizontal = 144.dp)
                .border(2.dp, colorTheme.value.dark)
                .background(color = colorTheme.value.accent)
                .testTag(Semantics.BTN_DIFFICULTY_HELP)
        ) {
            Icon(
                Icons.Filled.QuestionMark,
                stringResource(R.string.txt_icon_content_description),
                tint = colorTheme.value.dark,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
        }
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                title = {
                    Text(
                        text = stringResource(R.string.txt_title_different_difficulties),
                        color = colorTheme.value.accent,
                        style = MaterialTheme.typography.h5
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.txt_difficulty_explanation),
                        color = colorTheme.value.dark
                    )
                },
                confirmButton = {
                    ColoredButton(
                        buttonText = stringResource(R.string.btn_confirm),
                        onClick = {
                            openDialog.value = false
                        },
                        color = colorTheme.value.accent,
                        borderStroke = BorderStroke(2.dp, colorTheme.value.dark),
                        textColor = colorTheme.value.dark
                    )
                },
                backgroundColor = colorTheme.value.primary,
                modifier = Modifier.border(2.dp, colorTheme.value.accent)
                    .testTag(Semantics.ALERT_DIFFICULTY)
            )
        }
    }
}
