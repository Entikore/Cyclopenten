package de.entikore.cyclopenten.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

const val FAKE_USER_PREFERENCES = "fake_user_preferences"

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class]
)
object FakeDataStoreModule {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testCoroutineDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testCoroutineScope = TestScope(testCoroutineDispatcher + Job())

    // no better solution to provide only one instance of datastore in navigation test atm
    var DATASTORE: DataStore<Preferences>? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    @Singleton
    @Provides
    fun providePreferencesDataStore(
        @ApplicationContext
        testContext: Context
    ): DataStore<Preferences> {
        if (DATASTORE == null) {
            DATASTORE = PreferenceDataStoreFactory.create(
                scope = testCoroutineScope,
                produceFile = { testContext.preferencesDataStoreFile(FAKE_USER_PREFERENCES) }
            )
        }
        return DATASTORE!!
    }
}
