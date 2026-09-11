package com.example.chessgame.domain.usecase

import com.example.chessgame.domain.model.GameState
import com.example.chessgame.domain.model.Move
import com.example.chessgame.domain.model.MoveKind
import com.example.chessgame.domain.model.PieceType

class ConvertMoveToNotationUseCase(
    private val detectCheckmateUseCase: DetectCheckmateUseCase,
    private val detectCheckUseCase: DetectCheckUseCase,
    private val generateLegalMovesUseCase: GenerateLegalMovesUseCase
) {

    operator fun invoke(beforeState: GameState, move: Move, givesCheck: Boolean): String {
        if (move.isCastling) {
            val isKingSide = move.to.col > move.from.col
            val base = if (isKingSide) "O-O" else "O-O-O"
            return appendCheckSuffix(beforeState, move, base, givesCheck)
        }

        val piece = beforeState.board.pieceAt(move.from)!!
        val captured = beforeState.board.pieceAt(move.to) != null || move.isEnPassant

        val notation = when (piece.type) {
            PieceType.PAWN -> {
                if (captured) {
                    "${move.from.fileChar()}x${move.to.toAlgebraic()}"
                } else {
                    move.to.toAlgebraic()
                }.let { base ->
                    if (move.isPromotion) "$base=${pieceSymbol(move.promotionPiece ?: PieceType.QUEEN)}" else base
                }
            }
            else -> {
                val symbol = pieceSymbol(piece.type)
                val disambiguation = disambiguation(beforeState, move, piece.type)
                val captureMark = if (captured) "x" else ""
                "$symbol$disambiguation$captureMark${move.to.toAlgebraic()}"
            }
        }

        return appendCheckSuffix(beforeState, move, notation, givesCheck)
    }

    private fun appendCheckSuffix(
        beforeState: GameState,
        move: Move,
        notation: String,
        givesCheck: Boolean
    ): String {
        if (!givesCheck) return notation

        val afterBoard = com.example.chessgame.domain.logic.ChessRules.applyMove(
            beforeState.board,
            move,
            beforeState.castlingRights,
            beforeState.enPassantTarget
        )
        val opponent = beforeState.currentTurn.opposite()
        val tempState = beforeState.copy(
            board = afterBoard,
            currentTurn = opponent
        )
        val isMate = detectCheckmateUseCase(tempState)
        return if (isMate) "$notation#" else "$notation+"
    }

    private fun pieceSymbol(type: PieceType): String = when (type) {
        PieceType.KING -> "K"
        PieceType.QUEEN -> "Q"
        PieceType.ROOK -> "R"
        PieceType.BISHOP -> "B"
        PieceType.KNIGHT -> "N"
        PieceType.PAWN -> ""
    }

    private fun disambiguation(beforeState: GameState, move: Move, type: PieceType): String {
        val sameTypeMoves = generateLegalMovesUseCase.allLegalMoves(
            beforeState.board,
            beforeState.currentTurn,
            beforeState.castlingRights,
            beforeState.enPassantTarget
        ).filter { m ->
            val p = beforeState.board.pieceAt(m.from)
            p?.type == type && m.to == move.to && m.from != move.from
        }

        if (sameTypeMoves.isEmpty()) return ""

        val sameFile = sameTypeMoves.any { it.from.col == move.from.col }
        val sameRank = sameTypeMoves.any { it.from.row == move.from.row }

        return when {
            !sameFile -> move.from.fileChar().toString()
            !sameRank -> move.from.rankNumber().toString()
            else -> move.from.toAlgebraic()
        }
    }
}
