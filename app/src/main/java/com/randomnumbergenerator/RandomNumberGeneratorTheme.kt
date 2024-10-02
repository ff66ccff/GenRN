package com.randomnumbergenerator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Dark and Light color schemes
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color(0xFF000000),
    secondary = Color(0xFF03DAC5),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF000000),
    onSurface = Color(0xFF000000)
)

@Composable
fun RandomNumberGeneratorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
