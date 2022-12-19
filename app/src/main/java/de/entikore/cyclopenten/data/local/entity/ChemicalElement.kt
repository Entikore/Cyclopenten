package de.entikore.cyclopenten.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "chemical_elements")
data class ChemicalElement(
    @PrimaryKey
    @ColumnInfo(name = "atomic_number")
    val atomicNumber: Int,
    @ColumnInfo(name = "iupac_group") val iupacGroup: Int,
    val period: Int,
    val category: String,
    val symbol: String,
    val name: String,
    val choices: List<String>
)
