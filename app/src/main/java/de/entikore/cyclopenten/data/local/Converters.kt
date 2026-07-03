package de.entikore.cyclopenten.data.local

import androidx.room.TypeConverter
import de.entikore.cyclopenten.data.local.entity.ChemicalElement
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun toChoicesList(value: String): List<String> = try {
        json.decodeFromString<List<String>>(value)
    } catch (e: SerializationException) {
        Timber.e(e, "Failed to decode $value.")
        emptyList()
    } catch (ex: IllegalArgumentException) {
        Timber.e(ex, "$value is not a valid json string.")
        emptyList()
    }

    @TypeConverter
    fun fromChoicesList(list: List<String>): String = json.encodeToString(list)

    @TypeConverter
    fun toElement(value: String): ChemicalElement = try {
        json.decodeFromString<ChemicalElement>(value)
    } catch (e: SerializationException) {
        Timber.e(e, "Failed to decode $value.")
        ChemicalElement(-1, -1, -1, "", "", "", emptyList())
    } catch (ex: IllegalArgumentException) {
        Timber.e(ex, "$value is not a valid json string.")
        ChemicalElement(-1, -1, -1, "", "", "", emptyList())
    }

    @TypeConverter
    fun fromElement(element: ChemicalElement): String = json.encodeToString(element)

    @TypeConverter
    fun toElementsList(value: String): List<ChemicalElement> = try {
        json.decodeFromString<List<ChemicalElement>>(value)
    } catch (e: SerializationException) {
        Timber.e(e, "Failed to decode $value.")
        emptyList()
    } catch (ex: IllegalArgumentException) {
        Timber.e(ex, "$value is not a valid json string.")
        emptyList()
    }

    @TypeConverter
    fun fromElementsList(list: List<ChemicalElement>): String = json.encodeToString(list)
}
