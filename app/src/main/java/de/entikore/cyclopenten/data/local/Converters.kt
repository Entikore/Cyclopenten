package de.entikore.cyclopenten.data.local

import androidx.room.TypeConverter
import de.entikore.cyclopenten.data.local.entity.ChemicalElement
import kotlinx.serialization.json.Json
import timber.log.Timber

class Converters {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun toChoicesList(value: String): List<String> {
        return try {
            json.decodeFromString<List<String>>(value)
        } catch (e: Exception) {
            Timber.e(e, "Failed to convert json to list.")
            emptyList()
        }
    }

    @TypeConverter
    fun fromChoicesList(list: List<String>): String {
        return json.encodeToString(list)
    }

    @TypeConverter
    fun toElement(value: String): ChemicalElement {
        return try {
            json.decodeFromString<ChemicalElement>(value)
        } catch (e: Exception) {
            Timber.e(e, "Failed to convert json to element.")
            ChemicalElement(-1, -1, -1, "", "", "", emptyList())
        }
    }

    @TypeConverter
    fun fromElement(element: ChemicalElement): String {
        return json.encodeToString(element)
    }

    @TypeConverter
    fun toElementsList(value: String): List<ChemicalElement> {
        return try {
            json.decodeFromString<List<ChemicalElement>>(value)
        } catch (e: Exception) {
            Timber.e(e, "Failed to convert json to elements list.")
            emptyList()
        }
    }

    @TypeConverter
    fun fromElementsList(list: List<ChemicalElement>): String {
        return json.encodeToString(list)
    }
}
