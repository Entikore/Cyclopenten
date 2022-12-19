package de.entikore.cyclopenten.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.entikore.cyclopenten.data.Result
import de.entikore.cyclopenten.data.local.entity.SaveGame
import de.entikore.cyclopenten.domain.usecases.DeleteSaveGameUseCase
import de.entikore.cyclopenten.domain.usecases.GetSaveGameUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class StartScreenViewModel @Inject constructor(
    private val deleteSaveGameUseCase: DeleteSaveGameUseCase,
    private val getSaveGameUseCase: GetSaveGameUseCase
) : ViewModel() {

    private val _saveGame: MutableStateFlow<SaveGame?> = MutableStateFlow(null)
    val saveGame: StateFlow<SaveGame?> = _saveGame
    val enabled: Flow<Boolean> = _saveGame.map { it != null }
    init {
        viewModelScope.launch {
            getSaveGameUseCase().collect {
                if (it is Result.Success) {
                    _saveGame.value = it.data
                }
            }
        }
    }

    fun deleteSaveGame() {
        viewModelScope.launch {
            deleteSaveGameUseCase()
        }
    }
}
