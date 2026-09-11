package com.example.chessgame.presentation.viewmodel

import com.example.chessgame.domain.model.GameStatus
import com.example.chessgame.domain.model.Piece
import com.example.chessgame.domain.model.PieceColor
import com.example.chessgame.domain.model.PieceType
import com.example.chessgame.domain.model.RecordedMove

data class ChessUiState(
    val board: List<List<Piece?>> = emptyList(),
    val currentTurn: PieceColor = PieceColor.WHITE,
    val selectedSquare: Pair<Int, Int>? = null,
    val legalMoveTargets: Set<Pair<Int, Int>> = emptySet(),
    val moveHistory: List<RecordedMove> = emptyList(),
    val status: GameStatus = GameStatus.IN_PROGRESS,
    val statusMessage: String = "",
    val canUndo: Boolean = false,
    val showPromotionDialog: Boolean = false,
    val winner: PieceColor? = null
)
