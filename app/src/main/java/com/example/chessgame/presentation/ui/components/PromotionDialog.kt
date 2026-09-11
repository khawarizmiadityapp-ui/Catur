package com.example.chessgame.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chessgame.domain.model.PieceColor
import com.example.chessgame.domain.model.PieceType

@Composable
fun PromotionDialog(
    color: PieceColor,
    onPieceSelected: (PieceType) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Promosi Pion")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Pilih bidak untuk promosi:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PromotionOption(
                        label = if (color == PieceColor.WHITE) "♕" else "♛",
                        name = "Ratu",
                        onClick = { onPieceSelected(PieceType.QUEEN) }
                    )
                    PromotionOption(
                        label = if (color == PieceColor.WHITE) "♖" else "♜",
                        name = "Benteng",
                        onClick = { onPieceSelected(PieceType.ROOK) }
                    )
                    PromotionOption(
                        label = if (color == PieceColor.WHITE) "♗" else "♝",
                        name = "Gajah",
                        onClick = { onPieceSelected(PieceType.BISHOP) }
                    )
                    PromotionOption(
                        label = if (color == PieceColor.WHITE) "♘" else "♞",
                        name = "Kuda",
                        onClick = { onPieceSelected(PieceType.KNIGHT) }
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Composable
private fun PromotionOption(
    label: String,
    name: String,
    onClick: () -> Unit
) {
    Button(onClick = onClick, modifier = Modifier.padding(4.dp)) {
        Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
            Text(text = label, style = MaterialTheme.typography.headlineSmall)
            Text(text = name, style = MaterialTheme.typography.labelSmall)
        }
    }
}
