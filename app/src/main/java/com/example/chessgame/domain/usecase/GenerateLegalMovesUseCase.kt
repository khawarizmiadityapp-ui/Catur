package com.example.chessgame.domain.usecase

import com.example.chessgame.domain.logic.ChessRules
import com.example.chessgame.domain.model.Board
import com.example.chessgame.domain.model.CastlingRights
import com.example.chessgame.domain.model.Move
import com.example.chessgame.domain.model.PieceColor
import com.example.chessgame.domain.model.Position

class GenerateLegalMovesUseCase {

    operator fun invoke(
        board: Board,
        from: Position,
        castlingRights: CastlingRights,
        enPassantTarget: Position?,
        color: PieceColor
    ): List<Move> {
        val piece = board.pieceAt(from) ?: return emptyList()
        if (piece.color != color) return emptyList()

        val pseudoLegal = ChessRules.generatePseudoLegalMoves(board, from, castlingRights, enPassantTarget)
        return pseudoLegal.filter { move ->
            val newBoard = ChessRules.applyMove(board, move, castlingRights, enPassantTarget)
            !ChessRules.isInCheck(newBoard, color)
        }
    }

    fun allLegalMoves(
        board: Board,
        color: PieceColor,
        castlingRights: CastlingRights,
        enPassantTarget: Position?
    ): List<Move> {
        val moves = mutableListOf<Move>()
        for (row in 0..7) {
            for (col in 0..7) {
                val pos = Position(row, col)
                val piece = board.pieceAt(pos)
                if (piece?.color == color) {
                    moves.addAll(invoke(board, pos, castlingRights, enPassantTarget, color))
                }
            }
        }
        return moves
    }
}
