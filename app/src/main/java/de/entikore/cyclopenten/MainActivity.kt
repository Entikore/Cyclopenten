package de.entikore.cyclopenten

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import de.entikore.cyclopenten.ui.navigation.NavGraph
import de.entikore.cyclopenten.ui.screen.settings.SettingsViewModel
import de.entikore.cyclopenten.ui.theme.CyclopentenTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var viewModel: SettingsViewModel
    private var playMusic: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.background_music_1)

        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        viewModel.initialSetupEvent.observe(this) { initialSetupEvent ->
            playMusic = initialSetupEvent.musicOn
            observePreferenceChanges()
        }
        setContent {
            CyclopentenTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavGraph(navController = navController)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (playMusic && !mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer.isPlaying) { mediaPlayer.pause() }
    }

    private fun observePreferenceChanges() {
        viewModel.userPrefs.observe(this) { tasksUiModel ->
            playMusic = tasksUiModel.musicOn
            if (playMusic) {
                mediaPlayer.start()
                mediaPlayer.isLooping = true
            } else {
                if (mediaPlayer.isPlaying) mediaPlayer.pause()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}
