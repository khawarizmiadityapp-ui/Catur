package com.example.chessgame.data.repository

import com.example.chessgame.domain.model.GameState
import com.example.chessgame.domain.model.GameStatus
import com.example.chessgame.domain.model.Move
import com.example.chessgame.domain.model.MoveKind
import com.example.chessgame.domain.model.PieceType
import com.example.chessgame.domain.model.Position
import com.example.chessgame.domain.repository.ChessRepository
import com.example.chessgame.domain.usecase.GenerateLegalMovesUseCase
import com.example.chessgame.domain.usecase.MakeMoveUseCase
import com.example.chessgame.domain.usecase.UndoMoveUseCase
import com.example.chessgame.domain.usecase.ValidateMoveUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.locks.ReentrantLock
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.withLock

@Singleton
class ChessRepositoryImpl @Inject constructor(
    private val makeMoveUseCase: MakeMoveUseCase,
    private val undoMoveUseCase: UndoMoveUseCase,
    private val generateLegalMovesUseCase: GenerateLegalMovesUseCase,
    private val validateMoveUseCase: ValidateMoveUseCase
) : ChessRepository {

    private val stateHistory = mutableListOf(GameState.initial())

    private val _gameState = MutableStateFlow(GameState.initial())
    override val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _canUndo = MutableStateFlow(false)
    override val canUndo: StateFlow<Boolean> = _canUndo.asStateFlow()

    private val _selectedSquare = MutableStateFlow<Pair<Int, Int>?>(null)
    override val selectedSquare: StateFlow<Pair<Int, Int>?> = _selectedSquare.asStateFlow()

    private val _legalMoveTargets = MutableStateFlow<Set<Pair<Int, Int>>>(emptySet())
    override val legalMoveTargets: StateFlow<Set<Pair<Int, Int>>> = _legalMoveTargets.asStateFlow()

    private val _pendingPromotion = MutableStateFlow<Move?>(null)
    override val pendingPromotion: StateFlow<Move?> = _pendingPromotion.asStateFlow()

    private val operationLock = ReentrantLock()

    override fun selectSquare(row: Int, col: Int) = operationLock.withLock {
        if (_pendingPromotion.value != null) return
        val state = _gameState.value
        if (state.status == GameStatus.CHECKMATE || state.status == GameStatus.STALEMATE) return

        val pos = Position(row, col)
        val piece = state.board.pieceAt(pos)
        val currentSelection = _selectedSquare.value

        if (currentSelection != null && currentSelection.first == row && currentSelection.second == col) {
            clearSelectionInternal()
            return
        }

        if (currentSelection != null) {
            attemptMoveInternal(row, col, state, currentSelection)
            return
        }

        if (piece != null && piece.color == state.currentTurn) {
            _selectedSquare.value = row to col
            val legalMoves = generateLegalMovesUseCase(
                board = state.board,
                from = pos,
                castlingRights = state.castlingRights,
                enPassantTarget = state.enPassantTarget,
                color = state.currentTurn
            )
            _legalMoveTargets.value = legalMoves.map { it.to.row to it.to.col }.toSet()
        }
    }

    override fun clearSelection() = operationLock.withLock {
        _selectedSquare.value = null
        _legalMoveTargets.value = emptySet()
    }

    override fun attemptMove(toRow: Int, toCol: Int, promotionPiece: PieceType?) = operationLock.withLock {
        val state = _gameState.value
        val selection = _selectedSquare.value ?: return
        val from = Position(selection.first, selection.second)
        val to = Position(toRow, toCol)

        val candidate = Move(from, to, promotionPiece = promotionPiece)
        val legalMove = validateMoveUseCase.findMatchingLegalMove(state, candidate) ?: run {
            val targetPiece = state.board.pieceAt(to)
            if (targetPiece != null && targetPiece.color == state.currentTurn) {
                selectSquareInternal(toRow, toCol, state)
            } else {
                clearSelectionInternal()
            }
            return
        }

        if (legalMove.isPromotion && promotionPiece == null) {
            _pendingPromotion.value = legalMove
            return
        }

        executeMoveInternal(legalMove)
    }

    private fun attemptMoveInternal(toRow: Int, toCol: Int, state: GameState, selection: Pair<Int, Int>) {
        val from = Position(selection.first, selection.second)
        val to = Position(toRow, toCol)

        val candidate = Move(from, to, promotionPiece = null)
        val legalMove = validateMoveUseCase.findMatchingLegalMove(state, candidate) ?: run {
            val targetPiece = state.board.pieceAt(to)
            if (targetPiece != null && targetPiece.color == state.currentTurn) {
                selectSquareInternal(toRow, toCol, state)
            } else {
                clearSelectionInternal()
            }
            return
        }

        if (legalMove.isPromotion) {
            _pendingPromotion.value = legalMove
            return
        }

        executeMoveInternal(legalMove)
    }

    private fun selectSquareInternal(row: Int, col: Int, state: GameState) {
        _selectedSquare.value = row to col
        val legalMoves = generateLegalMovesUseCase(
            board = state.board,
            from = Position(row, col),
            castlingRights = state.castlingRights,
            enPassantTarget = state.enPassantTarget,
            color = state.currentTurn
        )
        _legalMoveTargets.value = legalMoves.map { it.to.row to it.to.col }.toSet()
    }

    private fun clearSelectionInternal() {
        _selectedSquare.value = null
        _legalMoveTargets.value = emptySet()
    }

    private fun executeMoveInternal(move: Move) {
        val result = makeMoveUseCase(_gameState.value, move)
        result.onSuccess { newState ->
            stateHistory.add(newState)
            _gameState.value = newState
            _canUndo.value = stateHistory.size > 1
            clearSelectionInternal()
            _pendingPromotion.value = null
        }
    }

    override fun confirmPromotion(pieceType: PieceType) = operationLock.withLock {
        val pending = _pendingPromotion.value ?: return
        val promotionMove = pending.copy(promotionPiece = pieceType, kind = MoveKind.PROMOTION)
        _pendingPromotion.value = null
        executeMoveInternal(promotionMove)
    }

    override fun dismissPromotion() = operationLock.withLock {
        _pendingPromotion.value = null
        clearSelectionInternal()
    }

    override fun undoMove() = operationLock.withLock {
        val previous = undoMoveUseCase(stateHistory) ?: return
        stateHistory.removeAt(stateHistory.lastIndex)
        _gameState.value = previous
        _canUndo.value = stateHistory.size > 1
        clearSelectionInternal()
        _pendingPromotion.value = null
    }

    override fun newGame() = operationLock.withLock {
        stateHistory.clear()
        stateHistory.add(GameState.initial())
        _gameState.value = GameState.initial()
        _canUndo.value = false
        clearSelectionInternal()
        _pendingPromotion.value = null
    }
}
