package de.entikore.cyclopenten.ui.screen.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import de.entikore.cyclopenten.data.UserPreferences
import de.entikore.cyclopenten.ui.components.ColoredButton
import de.entikore.cyclopenten.ui.components.Title
import de.entikore.cyclopenten.util.Semantics.ALERT_SETTINGS
import de.entikore.cyclopenten.util.Semantics.BTN_ALERT_CONFIRM
import de.entikore.cyclopenten.util.Semantics.BTN_ALERT_DISMISS
import de.entikore.cyclopenten.util.Semantics.BTN_SETTINGS_DELETE
import de.entikore.cyclopenten.util.Semantics.CD_SETTINGS_SCREEN
import de.entikore.cyclopenten.util.Semantics.SWITCH_SETTINGS_MUSIC
import de.entikore.cyclopenten.util.Semantics.SWITCH_SETTINGS_SOUND

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val colorTheme by viewModel.colorTheme.collectAsState()
    val updateMusicSetting = viewModel::updateMusicSetting
    val updateSoundEffectSetting = viewModel::updateSoundEffectSetting
    val clearScoreboard = viewModel::clearScoreboard
    val userPres by viewModel.userPrefs.observeAsState(
        UserPreferences(
            musicOn = true,
            soundEffectOn = true
        )
    )
    SettingScreen(
        SettingsScreenState(userPres, colorTheme),
        updateMusicSetting,
        updateSoundEffectSetting,
        clearScoreboard
    )
}

@Composable
fun SettingScreen(
    screenState: SettingsScreenState,
    updateMusicSetting: (musicOn: Boolean) -> Unit,
    updateSoundEffectSetting: (soundEffectOn: Boolean) -> Unit,
    clearScoreboard: () -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .background(color = screenState.colorTheme.primary)
            .semantics { contentDescription = CD_SETTINGS_SCREEN }
    ) {
        Title(
            title = stringResource(R.string.txt_settings),
            textColor = screenState.colorTheme.accent
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.txt_settings_music),
                color = screenState.colorTheme.dark
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Switch(
                checked = screenState.userPreferences.musicOn,
                onCheckedChange = {
                    updateMusicSetting(it)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = screenState.colorTheme.accent,
                    uncheckedThumbColor = screenState.colorTheme.dark
                ),
                modifier = Modifier
                    .testTag(SWITCH_SETTINGS_MUSIC)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.txt_settings_sound_effects),
                color = screenState.colorTheme.dark
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Switch(
                checked = screenState.userPreferences.soundEffectOn,
                onCheckedChange = {
                    updateSoundEffectSetting(it)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = screenState.colorTheme.accent,
                    uncheckedThumbColor = screenState.colorTheme.dark
                ),
                modifier = Modifier.testTag(SWITCH_SETTINGS_SOUND)
            )
        }
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                title = {
                    Text(
                        text = stringResource(R.string.txt_delete_scoreboard),
                        color = screenState.colorTheme.accent,
                        style = MaterialTheme.typography.h5
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.txt_delete_scoreboard_clarification),
                        color = screenState.colorTheme.dark
                    )
                },
                confirmButton = {
                    ColoredButton(
                        buttonText = stringResource(R.string.btn_confirm),
                        onClick = {
                            clearScoreboard()
                            openDialog.value = false
                        },
                        color = screenState.colorTheme.accent,
                        borderStroke = BorderStroke(2.dp, screenState.colorTheme.dark),
                        textColor = screenState.colorTheme.dark,
                        modifier = Modifier.testTag(BTN_ALERT_CONFIRM)
                    )
                },
                dismissButton = {
                    ColoredButton(
                        buttonText = stringResource(R.string.btn_cancel),
                        onClick = { openDialog.value = false },
                        color = screenState.colorTheme.accent,
                        borderStroke = BorderStroke(2.dp, screenState.colorTheme.dark),
                        textColor = screenState.colorTheme.dark,
                        modifier = Modifier.testTag(BTN_ALERT_DISMISS)
                    )
                },
                backgroundColor = screenState.colorTheme.primary,
                modifier = Modifier
                    .border(2.dp, screenState.colorTheme.accent)
                    .testTag(ALERT_SETTINGS)
            )
        }
        ColoredButton(
            buttonText = stringResource(R.string.btn_delete_scoreboard),
            onClick = { openDialog.value = true },
            color = screenState.colorTheme.accent,
            borderStroke = BorderStroke(2.dp, screenState.colorTheme.dark),
            textColor = screenState.colorTheme.dark,
            modifier = Modifier.testTag(BTN_SETTINGS_DELETE)
        )
    }
}
