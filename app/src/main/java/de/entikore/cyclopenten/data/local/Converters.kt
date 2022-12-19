package de.entikore.cyclopenten.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.entikore.cyclopenten.data.local.entity.ChemicalElement
import timber.log.Timber

class Converters {

    @TypeConverter
    fun toChoicesList(json: String): List<String> {
        return try {
            Gson().fromJson(json, object : TypeToken<ArrayList<String>>() {}.type)
        } catch (e: Exception) {
            Timber.e("Failed to convert json to list.", e)
            arrayListOf()
        }
    }

    @TypeConverter
    fun fromChoicesList(list: List<String>): String {
        return Gson().toJson(list, object : TypeToken<ArrayList<String>>() {}.type)
    }

    @TypeConverter
    fun toElement(json: String): ChemicalElement {
        return try {
            Gson().fromJson(json, object : TypeToken<ChemicalElement>() {}.type)
        } catch (e: Exception) {
            Timber.e("Failed to convert json.", e)
            ChemicalElement(-1, -1, -1, "", "", "", emptyList())
        }
    }

    @TypeConverter
    fun fromElement(element: ChemicalElement): String {
        return Gson().toJson(element, object : TypeToken<ChemicalElement>() {}.type)
    }

    @TypeConverter
    fun toElementsList(json: String): List<ChemicalElement> {
        return try {
            Gson().fromJson(json, object : TypeToken<ArrayList<ChemicalElement>>() {}.type)
        } catch (e: Exception) {
            Timber.e("Failed to convert json to list.", e)
            arrayListOf()
        }
    }

    @TypeConverter
    fun fromElementsList(list: List<ChemicalElement>): String {
        return Gson().toJson(list, object : TypeToken<ArrayList<ChemicalElement>>() {}.type)
    }
}
