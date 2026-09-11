package com.example.chessgame.domain.usecase

import com.example.chessgame.domain.model.GameState
import com.example.chessgame.domain.model.GameStatus

class DetectStalemateUseCase(
    private val generateLegalMovesUseCase: GenerateLegalMovesUseCase,
    private val detectCheckUseCase: DetectCheckUseCase
) {

    operator fun invoke(state: GameState): Boolean {
        if (detectCheckUseCase(state.board, state.currentTurn)) return false
        val legalMoves = generateLegalMovesUseCase.allLegalMoves(
            board = state.board,
            color = state.currentTurn,
            castlingRights = state.castlingRights,
            enPassantTarget = state.enPassantTarget
        )
        return legalMoves.isEmpty()
    }
}
