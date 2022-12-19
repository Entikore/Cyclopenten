package de.entikore.cyclopenten.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.entikore.cyclopenten.data.local.entity.ChemicalElement
import de.entikore.cyclopenten.data.local.entity.Highscore
import de.entikore.cyclopenten.data.local.entity.SaveGame

@Database(
    entities = [
        ChemicalElement::class,
        Highscore::class,
        SaveGame::class
    ],
    version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ChemicalElementDatabase : RoomDatabase() {
    abstract fun chemicalElementDao(): ChemicalElementDao
}
