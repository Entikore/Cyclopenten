package de.entikore.cyclopenten.domain.usecases

import de.entikore.cyclopenten.data.UserPreferences
import de.entikore.cyclopenten.data.UserPreferencesRepository
import de.entikore.cyclopenten.domain.usecases.base.BaseUseCaseWithOutParams
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SoundEffectPreferenceUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) :
    BaseUseCaseWithOutParams<Flow<UserPreferences>> {

    override suspend fun invoke(): Flow<UserPreferences> = repository.userPreferencesFlow
}
