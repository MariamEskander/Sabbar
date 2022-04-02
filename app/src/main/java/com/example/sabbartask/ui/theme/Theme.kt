package com.example.sabbartask.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val ColorPalette = lightColors(
    primary = Yellow,
    primaryVariant = Black200,
    secondary = Yellow,
    background = Yellow
)

@Composable
fun ComposeSampleTheme(
    content: @Composable () -> Unit
) {
    val colors = ColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}