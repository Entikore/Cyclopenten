package de.entikore.cyclopenten.util

object Constants {
    const val SCORE_INCREASE_HARD_DIFF = 15
    const val SCORE_INCREASE_EASY_DIFF = 10
    const val DIFFICULTY = "difficulty"

    // categories
    const val CAT_NON_METALS = "Nonmetals"
    const val CAT_NOBLE_GASES = "Noble Gases"
    const val CAT_ALKALI_METALS = "Alkali Metals"
    const val CAT_ALKALINE_METALS = "Alkaline Earth Metals"
    const val CAT_SEMIMETALS = "Semimetals"
    const val CAT_HALOGENS = "Halogens"
    const val CAT_POST_TRANSITION_METALS = "Post-Transition Metals"
    const val CAT_TRANSITION_METALS = "Transition Metals"
    const val CAT_LANTHANIDE = "Lanthanide"
    const val CAT_ACTINIDES = "Actinides"
    const val CAT_UNKNOWN = "Unknown"

    val CATEGORIES = listOf(
        CAT_NON_METALS,
        CAT_NOBLE_GASES,
        CAT_ALKALI_METALS,
        CAT_ALKALINE_METALS,
        CAT_SEMIMETALS,
        CAT_HALOGENS,
        CAT_POST_TRANSITION_METALS,
        CAT_TRANSITION_METALS,
        CAT_LANTHANIDE,
        CAT_LANTHANIDE,
        CAT_ACTINIDES,
        CAT_UNKNOWN
    )
}
