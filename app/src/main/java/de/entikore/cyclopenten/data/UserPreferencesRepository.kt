package de.entikore.cyclopenten.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber

class UserPreferencesRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val MUSIC_ON = booleanPreferencesKey("music_on")
        val SOUND_EFFECT_ON = booleanPreferencesKey("sound_effect_on")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e("Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())

    suspend fun updateMusicSetting(musicOn: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MUSIC_ON] = musicOn
        }
    }

    suspend fun updateSoundEffectSetting(soundEffectOn: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SOUND_EFFECT_ON] = soundEffectOn
        }
    }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val musicOn = preferences[PreferencesKeys.MUSIC_ON] ?: true
        val soundEffectOn = preferences[PreferencesKeys.SOUND_EFFECT_ON] ?: true
        return UserPreferences(musicOn, soundEffectOn)
    }
}

data class UserPreferences(
    val musicOn: Boolean,
    val soundEffectOn: Boolean
)
