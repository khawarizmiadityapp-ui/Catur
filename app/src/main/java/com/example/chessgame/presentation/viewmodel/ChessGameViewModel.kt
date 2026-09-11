package com.example.chessgame.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chessgame.domain.model.GameStatus
import com.example.chessgame.domain.model.PieceColor
import com.example.chessgame.domain.model.PieceType
import com.example.chessgame.domain.repository.ChessRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChessGameViewModel @Inject constructor(
    private val repository: ChessRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChessUiState())
    val uiState: StateFlow<ChessUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.gameState,
                repository.selectedSquare,
                repository.legalMoveTargets,
                repository.canUndo,
                repository.pendingPromotion
            ) { gameState, selected, legalTargets, canUndo, pendingPromotion ->
                ChessUiState(
                    board = gameState.board.squares,
                    currentTurn = gameState.currentTurn,
                    selectedSquare = selected,
                    legalMoveTargets = legalTargets,
                    moveHistory = gameState.moveHistory,
                    status = gameState.status,
                    statusMessage = buildStatusMessage(gameState.status, gameState.currentTurn, gameState.winner),
                    canUndo = canUndo,
                    showPromotionDialog = pendingPromotion != null,
                    winner = gameState.winner
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun onSquareClicked(row: Int, col: Int) {
        if (_uiState.value.showPromotionDialog) return
        if (_uiState.value.status == GameStatus.CHECKMATE ||
            _uiState.value.status == GameStatus.STALEMATE
        ) {
            return
        }
        repository.selectSquare(row, col)
    }

    fun onUndoClicked() {
        repository.undoMove()
    }

    fun onNewGameClicked() {
        repository.newGame()
    }

    fun onPromotionSelected(pieceType: PieceType) {
        repository.confirmPromotion(pieceType)
    }

    fun onPromotionDismissed() {
        repository.dismissPromotion()
    }

    private fun buildStatusMessage(
        status: GameStatus,
        currentTurn: PieceColor,
        winner: PieceColor?
    ): String = when (status) {
        GameStatus.IN_PROGRESS -> ""
        GameStatus.CHECK -> if (currentTurn == PieceColor.WHITE) "Skak! Giliran Putih" else "Skak! Giliran Hitam"
        GameStatus.CHECKMATE -> {
            val winnerName = when (winner) {
                PieceColor.WHITE -> "Putih"
                PieceColor.BLACK -> "Hitam"
                null -> "?"
            }
            "Skak Mat! $winnerName menang"
        }
        GameStatus.STALEMATE -> "Stalemate — Seri"
    }
}
