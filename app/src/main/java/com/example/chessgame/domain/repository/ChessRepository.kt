package com.example.chessgame.domain.repository

import com.example.chessgame.domain.model.GameState
import com.example.chessgame.domain.model.Move
import com.example.chessgame.domain.model.PieceType
import kotlinx.coroutines.flow.StateFlow

interface ChessRepository {
    val gameState: StateFlow<GameState>
    val canUndo: StateFlow<Boolean>

    fun selectSquare(row: Int, col: Int)
    fun clearSelection()
    fun attemptMove(toRow: Int, toCol: Int, promotionPiece: PieceType? = null)
    fun confirmPromotion(pieceType: PieceType)
    fun dismissPromotion()
    fun undoMove()
    fun newGame()

    val selectedSquare: StateFlow<Pair<Int, Int>?>
    val legalMoveTargets: StateFlow<Set<Pair<Int, Int>>>
    val pendingPromotion: StateFlow<Move?>
}
