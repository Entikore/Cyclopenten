package de.entikore.cyclopenten.data.local

import androidx.room.TypeConverter
import de.entikore.cyclopenten.data.local.entity.ChemicalElement
import kotlinx.serialization.json.Json
import timber.log.Timber

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun toChoicesList(value: String): List<String> =
        try {
            json.decodeFromString<List<String>>(value)
        } catch (e: Exception) {
            Timber.e(e, "Failed to convert json to list.")
            emptyList()
        }

    @TypeConverter
    fun fromChoicesList(list: List<String>): String = json.encodeToString(list)

    @TypeConverter
    fun toElement(value: String): ChemicalElement =
        try {
            json.decodeFromString<ChemicalElement>(value)
        } catch (e: Exception) {
            Timber.e(e, "Failed to convert json to element.")
            ChemicalElement(-1, -1, -1, "", "", "", emptyList())
        }

    @TypeConverter
    fun fromElement(element: ChemicalElement): String = json.encodeToString(element)

    @TypeConverter
    fun toElementsList(value: String): List<ChemicalElement> =
        try {
            json.decodeFromString<List<ChemicalElement>>(value)
        } catch (e: Exception) {
            Timber.e(e, "Failed to convert json to elements list.")
            emptyList()
        }

    @TypeConverter
    fun fromElementsList(list: List<ChemicalElement>): String = json.encodeToString(list)
}
