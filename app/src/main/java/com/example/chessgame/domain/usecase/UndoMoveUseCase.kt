package com.example.chessgame.domain.usecase

import com.example.chessgame.domain.model.GameState

class UndoMoveUseCase {

    operator fun invoke(history: List<GameState>): GameState? {
        if (history.size <= 1) return null
        return history[history.lastIndex - 1]
    }
}
