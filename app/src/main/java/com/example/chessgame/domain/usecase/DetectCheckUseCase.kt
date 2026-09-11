package com.example.chessgame.domain.usecase

import com.example.chessgame.domain.logic.ChessRules
import com.example.chessgame.domain.model.Board
import com.example.chessgame.domain.model.CastlingRights
import com.example.chessgame.domain.model.Move
import com.example.chessgame.domain.model.PieceColor
import com.example.chessgame.domain.model.Position

class DetectCheckUseCase {

    operator fun invoke(board: Board, color: PieceColor): Boolean {
        return ChessRules.isInCheck(board, color)
    }
}
