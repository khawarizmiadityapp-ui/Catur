package com.example.chessgame.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ChessColorScheme = lightColorScheme(
    primary = Color(0xFF2E5939),
    secondary = Color(0xFF769656),
    background = Color(0xFFF5F5F0),
    surface = Color(0xFFFFFFFF)
)

@Composable
fun ChessGameTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ChessColorScheme,
        content = content
    )
}
