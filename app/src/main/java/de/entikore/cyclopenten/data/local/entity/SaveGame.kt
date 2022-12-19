package de.entikore.cyclopenten.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "save_game")
data class SaveGame(
    @PrimaryKey
    val id: Int = SAVE_GAME_ID,
    val lives: Int,
    val lostLives: Int,
    val score: Int,
    val remainingQuestions: List<ChemicalElement>,
    val currentElement: ChemicalElement,
    val difficulty: Boolean
) {
    companion object {
        const val SAVE_GAME_ID = 1
    }
}
