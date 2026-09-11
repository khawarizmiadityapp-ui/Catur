package com.example.chessgame.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chessgame.domain.model.GameStatus
import com.example.chessgame.domain.model.PieceType
import com.example.chessgame.presentation.ui.components.GameStatusBar
import com.example.chessgame.presentation.ui.components.MoveHistoryPanel
import com.example.chessgame.presentation.ui.components.PromotionDialog
import com.example.chessgame.presentation.ui.components.SquareView
import com.example.chessgame.presentation.viewmodel.ChessGameViewModel
import com.example.chessgame.presentation.viewmodel.ChessUiState

@Composable
fun ChessBoardScreen(
    viewModel: ChessGameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val isWide = maxWidth > 600.dp

            if (isWide) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ChessBoard(
                        uiState = uiState,
                        onSquareClick = viewModel::onSquareClicked,
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    )
                    SidePanel(
                        uiState = uiState,
                        onUndo = viewModel::onUndoClicked,
                        onNewGame = viewModel::onNewGameClicked,
                        modifier = Modifier
                            .weight(0.45f)
                            .fillMaxSize()
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ChessBoard(
                        uiState = uiState,
                        onSquareClick = viewModel::onSquareClicked,
                        modifier = Modifier.fillMaxWidth()
                    )
                    SidePanel(
                        uiState = uiState,
                        onUndo = viewModel::onUndoClicked,
                        onNewGame = viewModel::onNewGameClicked,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    if (uiState.showPromotionDialog) {
        PromotionDialog(
            color = uiState.currentTurn,
            onPieceSelected = viewModel::onPromotionSelected,
            onDismiss = viewModel::onPromotionDismissed
        )
    }
}

@Composable
private fun ChessBoard(
    uiState: ChessUiState,
    onSquareClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val kingInCheckPos = findKingInCheckPosition(uiState)

    Column(
        modifier = modifier.widthIn(max = 480.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in 0..7) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0..7) {
                    val piece = uiState.board.getOrNull(row)?.getOrNull(col)
                    val isSelected = uiState.selectedSquare == (row to col)
                    val isLegal = (row to col) in uiState.legalMoveTargets
                    val isKingCheck = kingInCheckPos == (row to col)

                    SquareView(
                        row = row,
                        col = col,
                        piece = piece,
                        isSelected = isSelected,
                        isLegalTarget = isLegal,
                        isKingInCheck = isKingCheck,
                        onClick = { onSquareClick(row, col) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SidePanel(
    uiState: ChessUiState,
    onUndo: () -> Unit,
    onNewGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        GameStatusBar(
            currentTurn = uiState.currentTurn,
            status = uiState.status,
            statusMessage = uiState.statusMessage
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onUndo,
                enabled = uiState.canUndo,
                modifier = Modifier.weight(1f)
            ) {
                Text("Undo")
            }
            Button(
                onClick = onNewGame,
                modifier = Modifier.weight(1f)
            ) {
                Text("New Game")
            }
        }

        Text(
            text = "Riwayat Langkah",
            style = androidx.compose.material3.MaterialTheme.typography.titleSmall
        )

        MoveHistoryPanel(
            moves = uiState.moveHistory,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp, max = 280.dp)
        )
    }
}

private fun findKingInCheckPosition(uiState: ChessUiState): Pair<Int, Int>? {
    if (uiState.status != GameStatus.CHECK && uiState.status != GameStatus.CHECKMATE) {
        return null
    }
    for (row in 0..7) {
        for (col in 0..7) {
            val piece = uiState.board.getOrNull(row)?.getOrNull(col)
            if (piece?.type == PieceType.KING && piece.color == uiState.currentTurn) {
                return row to col
            }
        }
    }
    return null
}
