package de.entikore.cyclopenten.domain.usecases

import de.entikore.cyclopenten.data.UserPreferences
import de.entikore.cyclopenten.data.UserPreferencesRepository
import de.entikore.cyclopenten.domain.usecases.base.BaseUseCaseWithOutParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SoundEffectPreferenceUseCase
@Inject
constructor(private val repository: UserPreferencesRepository) :
    BaseUseCaseWithOutParams<Flow<UserPreferences>> {
    override fun invoke(): Flow<UserPreferences> = repository.userPreferencesFlow
}
