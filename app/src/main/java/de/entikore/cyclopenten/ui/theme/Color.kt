package de.entikore.cyclopenten.ui.theme

import androidx.compose.ui.graphics.Color
import de.entikore.cyclopenten.util.Constants.CATEGORIES
import de.entikore.cyclopenten.util.Constants.CAT_ACTINIDES
import de.entikore.cyclopenten.util.Constants.CAT_ALKALINE_METALS
import de.entikore.cyclopenten.util.Constants.CAT_ALKALI_METALS
import de.entikore.cyclopenten.util.Constants.CAT_HALOGENS
import de.entikore.cyclopenten.util.Constants.CAT_LANTHANIDE
import de.entikore.cyclopenten.util.Constants.CAT_NOBLE_GASES
import de.entikore.cyclopenten.util.Constants.CAT_NON_METALS
import de.entikore.cyclopenten.util.Constants.CAT_POST_TRANSITION_METALS
import de.entikore.cyclopenten.util.Constants.CAT_SEMIMETALS
import de.entikore.cyclopenten.util.Constants.CAT_TRANSITION_METALS
import de.entikore.cyclopenten.util.Constants.CAT_UNKNOWN

val C0Primary = Color(0xFFAA3939)
val C0Dark = Color(0xFF550000)
val C0Accent = Color(0xFFFFAAAA)
val C0Complementary = Color(0xFF2D882D)

val C1Primary = Color(0xFFAA6C39)
val C1Dark = Color(0xFF552700)
val C1Accent = Color(0xFFFFD1AA)
val C1Complementary = Color(0xFF226765)

val C2Primary = Color(0xFFAA8439)
val C2Dark = Color(0xFF553900)
val C2Accent = Color(0xFFFFE3AA)
val C2Complementary = Color(0xFF2E4272)

val C3Primary = Color(0xFFAA9739)
val C3Dark = Color(0xFF554600)
val C3Accent = Color(0xFFFFF0AA)
val C3Complementary = Color(0xFF403075)

val C4Primary = Color(0xFFAAAA39)
val C4Dark = Color(0xFF555500)
val C4Accent = Color(0xFFFFFFAA)
val C4Complementary = Color(0xFF582A72)

val C5Primary = Color(0xFF7B9F35)
val C5Dark = Color(0xFF354F00)
val C5Accent = Color(0xFFD4EE9F)
val C5Complementary = Color(0xFF882D61)

val C6Primary = Color(0xFF2D882D)
val C6Dark = Color(0xFF004400)
val C6Accent = Color(0xFF88CC88)
val C6Complementary = Color(0xFFAA3939)

val C7Primary = Color(0xFF226666)
val C7Dark = Color(0xFF003333)
val C7Accent = Color(0xFF669999)
val C7Complementary = Color(0xFFAA6C39)

val C8Primary = Color(0xFF2E4272)
val C8Dark = Color(0xFF061539)
val C8Accent = Color(0xFF7887AB)
val C8Complementary = Color(0xFFAA8439)

val C9Primary = Color(0xFF403075)
val C9Dark = Color(0xFF13073A)
val C9Accent = Color(0xFF887CAF)
val C9Complementary = Color(0xFFAA9739)

val C10Primary = Color(0xFF582A72)
val C10Dark = Color(0xFF260339)
val C10Accent = Color(0xFF9775AA)
val C10Complementary = Color(0xFFAAAA39)

val C11Primary = Color(0xFF882D61)
val C11Dark = Color(0xFF440027)
val C11Accent = Color(0xFFCD88AF)
val C11Complementary = Color(0xFF7B9F35)

fun randomTheme() = ColorTheme(CATEGORIES.random())

data class ColorTheme(val category: String) {
    val primary: Color
    val dark: Color
    val accent: Color
    val complementary: Color

    init {
        val theme = getColorTheme(category)
        val (one, two, three) = theme.first
        primary = one
        dark = two
        accent = three
        complementary = theme.second
    }

    private fun getColorTheme(category: String): Pair<Triple<Color, Color, Color>, Color> {
        return when (category) {
            CAT_NON_METALS -> Pair(Triple(C0Primary, C0Dark, C0Accent), C0Complementary)
            CAT_NOBLE_GASES -> Pair(Triple(C1Primary, C1Dark, C1Accent), C1Complementary)
            CAT_ALKALI_METALS -> Pair(Triple(C2Primary, C2Dark, C2Accent), C2Complementary)
            CAT_ALKALINE_METALS -> Pair(Triple(C3Primary, C3Dark, C3Accent), C3Complementary)
            CAT_SEMIMETALS -> Pair(Triple(C4Primary, C4Dark, C4Accent), C4Complementary)
            CAT_HALOGENS -> Pair(Triple(C5Primary, C5Dark, C5Accent), C5Complementary)
            CAT_POST_TRANSITION_METALS -> Pair(Triple(C6Primary, C6Dark, C6Accent), C6Complementary)
            CAT_TRANSITION_METALS -> Pair(Triple(C7Primary, C7Dark, C7Accent), C7Complementary)
            CAT_LANTHANIDE -> Pair(Triple(C8Primary, C8Dark, C8Accent), C8Complementary)
            CAT_ACTINIDES -> Pair(Triple(C9Primary, C9Dark, C9Accent), C9Complementary)
            CAT_UNKNOWN -> Pair(Triple(C10Primary, C10Dark, C10Accent), C10Complementary)
            else -> Pair(Triple(C11Primary, C11Dark, C11Accent), C11Complementary)
        }
    }
}
