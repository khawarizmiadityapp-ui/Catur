package com.example.chessgame.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chessgame.domain.model.GameStatus
import com.example.chessgame.domain.model.PieceColor

@Composable
fun GameStatusBar(
    currentTurn: PieceColor,
    status: GameStatus,
    statusMessage: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Giliran: ${if (currentTurn == PieceColor.WHITE) "Putih ♔" else "Hitam ♚"}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (status == GameStatus.CHECK) {
                    Text(
                        text = "SKAK",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            if (statusMessage.isNotEmpty()) {
                Text(
                    text = statusMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (status == GameStatus.CHECKMATE || status == GameStatus.STALEMATE) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    },
                    color = when (status) {
                        GameStatus.CHECKMATE -> MaterialTheme.colorScheme.primary
                        GameStatus.STALEMATE -> MaterialTheme.colorScheme.secondary
                        GameStatus.CHECK -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }
    }
}
