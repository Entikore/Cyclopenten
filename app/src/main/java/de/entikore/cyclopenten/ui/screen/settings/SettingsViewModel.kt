package de.entikore.cyclopenten.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.entikore.cyclopenten.data.UserPreferences
import de.entikore.cyclopenten.data.UserPreferencesRepository
import de.entikore.cyclopenten.domain.usecases.DeleteScoreboardUseCase
import de.entikore.cyclopenten.ui.theme.ColorTheme
import de.entikore.cyclopenten.ui.theme.randomTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    private val deleteScoreboardUseCase: DeleteScoreboardUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    val userPrefs: StateFlow<UserPreferences> = userPreferencesRepository.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences(musicOn = true, soundEffectOn = true),
        )

    private val _colorTheme = MutableStateFlow(randomTheme())
    val colorTheme: StateFlow<ColorTheme> = _colorTheme.asStateFlow()

    fun updateMusicSetting(musicOn: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateMusicSetting(musicOn)
        }
    }

    fun updateSoundEffectSetting(soundEffectOn: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateSoundEffectSetting(soundEffectOn)
        }
    }

    fun clearScoreboard() {
        viewModelScope.launch {
            deleteScoreboardUseCase()
        }
    }
}
