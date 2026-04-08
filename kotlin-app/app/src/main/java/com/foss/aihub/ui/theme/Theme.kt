package com.foss.aihub.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val VGHColorScheme = lightColorScheme(
    primary = VGHPrimary,
    secondary = VGHSecondary,
    background = VGHBackground,
    surface = VGHSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = VGHTextPrimary,
    onSurface = VGHTextPrimary,
    error = VGHError
)

@Composable
fun AIHubTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = VGHColorScheme,
        typography = Typography,
        content = content
    )
}
