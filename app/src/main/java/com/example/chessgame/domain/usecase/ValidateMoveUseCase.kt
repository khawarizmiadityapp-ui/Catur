package com.example.chessgame.domain.usecase

import com.example.chessgame.domain.model.GameState
import com.example.chessgame.domain.model.Move
import com.example.chessgame.domain.model.PieceType

class ValidateMoveUseCase(
    private val generateLegalMovesUseCase: GenerateLegalMovesUseCase
) {

    operator fun invoke(state: GameState, move: Move): Boolean {
        val legalMoves = generateLegalMovesUseCase(
            board = state.board,
            from = move.from,
            castlingRights = state.castlingRights,
            enPassantTarget = state.enPassantTarget,
            color = state.currentTurn
        )
        return legalMoves.any { legal ->
            legal.to == move.to &&
                legal.kind == move.kind &&
                (move.promotionPiece == null || legal.promotionPiece == move.promotionPiece)
        }
    }

    fun findMatchingLegalMove(state: GameState, from: Move): Move? {
        val legalMoves = generateLegalMovesUseCase(
            board = state.board,
            from = from.from,
            castlingRights = state.castlingRights,
            enPassantTarget = state.enPassantTarget,
            color = state.currentTurn
        )
        return legalMoves.find { legal ->
            legal.to == from.to &&
                (from.promotionPiece == null || legal.promotionPiece == from.promotionPiece)
        }
    }
}
