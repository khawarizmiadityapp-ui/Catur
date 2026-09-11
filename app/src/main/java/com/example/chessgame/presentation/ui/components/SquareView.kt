package com.example.chessgame.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chessgame.domain.model.Piece
import com.example.chessgame.domain.model.PieceColor
import com.example.chessgame.domain.model.PieceType
import com.example.chessgame.presentation.ui.theme.CheckHighlight
import com.example.chessgame.presentation.ui.theme.DarkSquare
import com.example.chessgame.presentation.ui.theme.LegalMoveDot
import com.example.chessgame.presentation.ui.theme.LightSquare
import com.example.chessgame.presentation.ui.theme.SelectedSquare

@Composable
fun SquareView(
    row: Int,
    col: Int,
    piece: Piece?,
    isSelected: Boolean,
    isLegalTarget: Boolean,
    isKingInCheck: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLight = (row + col) % 2 == 0
    val baseColor = when {
        isKingInCheck -> CheckHighlight
        isSelected -> SelectedSquare
        isLight -> LightSquare
        else -> DarkSquare
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(baseColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isLegalTarget && piece == null) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(LegalMoveDot)
            )
        }
        if (piece != null) {
            PieceView(piece = piece)
            if (isLegalTarget) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(0.85f)
                        .clip(CircleShape)
                        .background(LegalMoveDot.copy(alpha = 0.3f))
                )
            }
        }
    }
}

@Composable
fun PieceView(piece: Piece, modifier: Modifier = Modifier) {
    Text(
        text = piece.toUnicode(),
        fontSize = 36.sp,
        fontWeight = FontWeight.Normal,
        color = if (piece.color == PieceColor.WHITE) Color.White else Color(0xFF1A1A1A),
        modifier = modifier
    )
}

private fun Piece.toUnicode(): String = when (type) {
    PieceType.KING -> if (color == PieceColor.WHITE) "♔" else "♚"
    PieceType.QUEEN -> if (color == PieceColor.WHITE) "♕" else "♛"
    PieceType.ROOK -> if (color == PieceColor.WHITE) "♖" else "♜"
    PieceType.BISHOP -> if (color == PieceColor.WHITE) "♗" else "♝"
    PieceType.KNIGHT -> if (color == PieceColor.WHITE) "♘" else "♞"
    PieceType.PAWN -> if (color == PieceColor.WHITE) "♙" else "♟"
}
