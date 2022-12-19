package de.entikore.cyclopenten.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.entikore.cyclopenten.data.UserPreferencesRepository
import de.entikore.cyclopenten.domain.usecases.DeleteScoreboardUseCase
import de.entikore.cyclopenten.ui.theme.randomTheme
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val deleteScoreboardUseCase: DeleteScoreboardUseCase,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val prefFlow = userPreferencesRepository.userPreferencesFlow
    val userPrefs = prefFlow.asLiveData()

    val colorTheme = MutableStateFlow(randomTheme())

    val initialSetupEvent = liveData {
        emit(userPreferencesRepository.fetchInitialPreferences())
    }

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
