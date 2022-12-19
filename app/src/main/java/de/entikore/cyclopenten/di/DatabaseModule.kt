package de.entikore.cyclopenten.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
import javax.inject.Provider
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
        moshi: Moshi,
        daoProvider: Provider<ChemicalElementDao>
    ): ChemicalElementDatabase {
        return Room.databaseBuilder(
            context.applicationContext, ChemicalElementDatabase::class.java, "elements-db"
        ).addCallback(
            object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        val jsonString =
                            context.resources.openRawResource(R.raw.elements).bufferedReader()
                                .use { it.readText() }
                        val elementListType =
                            Types.newParameterizedType(
                                List::class.java,
                                ChemicalElement::class.java
                            )
                        val moshiElementAdapter =
                            moshi.adapter<List<ChemicalElement>>(elementListType)
                        kotlin.runCatching {
                            daoProvider.get()
                                .insertAll(
                                    moshiElementAdapter.fromJson(jsonString)
                                        .orEmpty()
                                )
                        }
                    }
                }
            }

        ).build()
    }

    @Provides
    fun provideChemicalElementDao(database: ChemicalElementDatabase): ChemicalElementDao {
        return database.chemicalElementDao()
    }

    @Provides
    fun provideChemicalElementRepository(
        elementDao: ChemicalElementDao
    ): ChemicalElementRepository {
        return DefaultChemicalElementRepository(dao = elementDao)
    }
}
