package com.example.chessgame.domain.usecase

import com.example.chessgame.domain.logic.ChessRules
import com.example.chessgame.domain.model.GameState
import com.example.chessgame.domain.model.GameStatus
import com.example.chessgame.domain.model.Move
import com.example.chessgame.domain.model.PieceType
import com.example.chessgame.domain.model.RecordedMove

class MakeMoveUseCase(
    private val validateMoveUseCase: ValidateMoveUseCase,
    private val generateLegalMovesUseCase: GenerateLegalMovesUseCase,
    private val detectCheckUseCase: DetectCheckUseCase,
    private val detectCheckmateUseCase: DetectCheckmateUseCase,
    private val detectStalemateUseCase: DetectStalemateUseCase,
    private val convertMoveToNotationUseCase: ConvertMoveToNotationUseCase
) {

    operator fun invoke(state: GameState, move: Move): Result<GameState> {
        val legalMove = validateMoveUseCase.findMatchingLegalMove(state, move)
            ?: return Result.failure(IllegalArgumentException("Illegal move"))

        val piece = state.board.pieceAt(legalMove.from)!!
        val newBoard = ChessRules.applyMove(
            state.board,
            legalMove,
            state.castlingRights,
            state.enPassantTarget
        )

        val newEnPassant = ChessRules.computeEnPassantTarget(piece, legalMove.from, legalMove.to)
        val newCastling = state.castlingRights.updateAfterMove(piece, legalMove.from, legalMove.to)

        val isCapture = legalMove.isEnPassant ||
            state.board.pieceAt(legalMove.to) != null ||
            legalMove.isEnPassant
        val newHalfMove = if (piece.type == PieceType.PAWN || isCapture) 0 else state.halfMoveClock + 1
        val newFullMove = if (state.currentTurn == com.example.chessgame.domain.model.PieceColor.BLACK) {
            state.fullMoveNumber + 1
        } else {
            state.fullMoveNumber
        }

        val nextTurn = state.currentTurn.opposite()
        val opponentInCheck = detectCheckUseCase(newBoard, nextTurn)

        val tempState = state.copy(
            board = newBoard,
            currentTurn = nextTurn,
            castlingRights = newCastling,
            enPassantTarget = newEnPassant,
            halfMoveClock = newHalfMove,
            fullMoveNumber = newFullMove
        )

        val notation = convertMoveToNotationUseCase(state, legalMove, opponentInCheck)
        val recorded = RecordedMove(legalMove, notation, state.currentTurn)
        val history = state.moveHistory + recorded

        val status = when {
            detectCheckmateUseCase(tempState) -> GameStatus.CHECKMATE
            detectStalemateUseCase(tempState) -> GameStatus.STALEMATE
            opponentInCheck -> GameStatus.CHECK
            else -> GameStatus.IN_PROGRESS
        }

        val winner = if (status == GameStatus.CHECKMATE) state.currentTurn else null

        return Result.success(
            tempState.copy(
                status = status,
                moveHistory = history,
                winner = winner
            )
        )
    }
}
