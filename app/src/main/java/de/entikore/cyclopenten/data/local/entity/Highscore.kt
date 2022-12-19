package de.entikore.cyclopenten.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "highscore")
data class Highscore(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val score: Int,
    val hardMode: Boolean
)

data class NameScoreAndDifficulty(
    val name: String,
    val score: Int,
    val hardMode: Boolean
)
