package de.entikore.cyclopenten.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.entikore.cyclopenten.R
import de.entikore.cyclopenten.data.ChemicalElementRepository
import de.entikore.cyclopenten.data.DefaultChemicalElementRepository
import de.entikore.cyclopenten.data.local.ChemicalElementDao
import de.entikore.cyclopenten.data.local.ChemicalElementDatabase
import de.entikore.cyclopenten.data.local.entity.ChemicalElement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
        }

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
        json: Json,
        daoProvider: Provider<ChemicalElementDao>,
    ): ChemicalElementDatabase =
        Room
            .databaseBuilder(
                context.applicationContext,
                ChemicalElementDatabase::class.java,
                "elements-db",
            ).addCallback(
                object : RoomDatabase.Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val jsonString =
                                context.resources
                                    .openRawResource(R.raw.elements)
                                    .bufferedReader()
                                    .use { it.readText() }
                            kotlin.runCatching {
                                val elements = json.decodeFromString<List<ChemicalElement>>(jsonString)
                                daoProvider.get().insertAll(elements)
                            }
                        }
                    }
                },
            ).build()

    @Provides
    fun provideChemicalElementDao(database: ChemicalElementDatabase): ChemicalElementDao = database.chemicalElementDao()

    @Provides
    fun provideChemicalElementRepository(elementDao: ChemicalElementDao): ChemicalElementRepository =
        DefaultChemicalElementRepository(dao = elementDao)
}
