package de.entikore.cyclopenten.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = C0Dark,
    primaryVariant = C0Accent,
    secondary = C0Complementary
)

private val LightColorPalette = lightColors(
    primary = C0Primary,
    primaryVariant = C0Accent,
    secondary = C0Complementary
)

@Composable
fun CyclopentenTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
