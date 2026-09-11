package com.example.chessgame.domain.logic

import com.example.chessgame.domain.model.Board
import com.example.chessgame.domain.model.CastlingRights
import com.example.chessgame.domain.model.Move
import com.example.chessgame.domain.model.MoveKind
import com.example.chessgame.domain.model.Piece
import com.example.chessgame.domain.model.PieceColor
import com.example.chessgame.domain.model.PieceType
import com.example.chessgame.domain.model.Position

internal object ChessRules {

    fun isSquareAttacked(board: Board, target: Position, byColor: PieceColor): Boolean {
        for (row in 0..7) {
            for (col in 0..7) {
                val piece = board.pieceAt(Position(row, col)) ?: continue
                if (piece.color != byColor) continue
                if (canAttackSquare(board, piece, Position(row, col), target, enPassantTarget = null)) {
                    return true
                }
            }
        }
        return false
    }

    fun isInCheck(board: Board, color: PieceColor): Boolean {
        val kingPos = board.findKing(color) ?: return false
        return isSquareAttacked(board, kingPos, color.opposite())
    }

    fun generatePseudoLegalMoves(
        board: Board,
        from: Position,
        castlingRights: CastlingRights,
        enPassantTarget: Position?
    ): List<Move> {
        val piece = board.pieceAt(from) ?: return emptyList()
        return when (piece.type) {
            PieceType.PAWN -> generatePawnMoves(board, from, piece, enPassantTarget)
            PieceType.KNIGHT -> generateKnightMoves(board, from, piece)
            PieceType.BISHOP -> generateSlidingMoves(board, from, piece, listOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1))
            PieceType.ROOK -> generateSlidingMoves(board, from, piece, listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1))
            PieceType.QUEEN -> generateSlidingMoves(
                board, from, piece,
                listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)
            )
            PieceType.KING -> generateKingMoves(board, from, piece, castlingRights)
        }
    }

    fun applyMove(
        board: Board,
        move: Move,
        castlingRights: CastlingRights,
        enPassantTarget: Position?
    ): Board {
        val piece = board.pieceAt(move.from) ?: return board
        var newBoard = board.withPiece(move.from, null)

        when {
            move.isCastling -> {
                newBoard = newBoard.withPiece(move.to, piece)
                val isKingSide = move.to.col > move.from.col
                val rookFromCol = if (isKingSide) 7 else 0
                val rookToCol = if (isKingSide) move.to.col - 1 else move.to.col + 1
                val rook = board.pieceAt(Position(move.from.row, rookFromCol))
                newBoard = newBoard.withPiece(Position(move.from.row, rookFromCol), null)
                if (rook != null) {
                    newBoard = newBoard.withPiece(Position(move.from.row, rookToCol), rook)
                }
            }
            move.isEnPassant -> {
                newBoard = newBoard.withPiece(move.to, piece)
                val capturedRow = move.from.row
                newBoard = newBoard.withPiece(Position(capturedRow, move.to.col), null)
            }
            move.isPromotion -> {
                val promoted = Piece(move.promotionPiece ?: PieceType.QUEEN, piece.color)
                newBoard = newBoard.withPiece(move.to, promoted)
            }
            else -> {
                newBoard = newBoard.withPiece(move.to, piece)
            }
        }
        return newBoard
    }

    fun computeEnPassantTarget(piece: Piece, from: Position, to: Position): Position? {
        if (piece.type != PieceType.PAWN) return null
        val direction = if (piece.color == PieceColor.WHITE) -1 else 1
        if (to.row == from.row + 2 * direction) {
            return Position(from.row + direction, from.col)
        }
        return null
    }

    private fun generatePawnMoves(
        board: Board,
        from: Position,
        piece: Piece,
        enPassantTarget: Position?
    ): List<Move> {
        val moves = mutableListOf<Move>()
        val direction = if (piece.color == PieceColor.WHITE) -1 else 1
        val startRow = if (piece.color == PieceColor.WHITE) 6 else 1
        val promotionRow = if (piece.color == PieceColor.WHITE) 0 else 7

        val oneForwardRow = from.row + direction
        if (oneForwardRow in 0..7) {
            val oneForward = Position(oneForwardRow, from.col)
            if (board.pieceAt(oneForward) == null) {
                if (oneForward.row == promotionRow) {
                    for (promo in listOf(PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT)) {
                        moves.add(Move(from, oneForward, promotionPiece = promo, kind = MoveKind.PROMOTION))
                    }
                } else {
                    moves.add(Move(from, oneForward))
                    if (from.row == startRow) {
                        val twoForwardRow = from.row + 2 * direction
                        if (twoForwardRow in 0..7) {
                            val twoForward = Position(twoForwardRow, from.col)
                            if (board.pieceAt(twoForward) == null) {
                                moves.add(Move(from, twoForward))
                            }
                        }
                    }
                }
            }
        }

        for (dc in listOf(-1, 1)) {
            val captureCol = from.col + dc
            val captureRow = from.row + direction
            if (captureCol !in 0..7 || captureRow !in 0..7) continue
            val capturePos = Position(captureRow, captureCol)
            val target = board.pieceAt(capturePos)
            if (target != null && target.color != piece.color) {
                if (capturePos.row == promotionRow) {
                    for (promo in listOf(PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT)) {
                        moves.add(Move(from, capturePos, promotionPiece = promo, kind = MoveKind.PROMOTION))
                    }
                } else {
                    moves.add(Move(from, capturePos))
                }
            }
            if (enPassantTarget == capturePos) {
                moves.add(Move(from, capturePos, kind = MoveKind.EN_PASSANT))
            }
        }
        return moves
    }

    private fun generateKnightMoves(board: Board, from: Position, piece: Piece): List<Move> {
        val offsets = listOf(-2 to -1, -2 to 1, -1 to -2, -1 to 2, 1 to -2, 1 to 2, 2 to -1, 2 to 1)
        return offsets.mapNotNull { (dr, dc) ->
            val row = from.row + dr
            val col = from.col + dc
            if (row !in 0..7 || col !in 0..7) return@mapNotNull null
            val to = Position(row, col)
            if (!isValidMoveTarget(board, piece, to)) null else Move(from, to)
        }
    }

    private fun generateSlidingMoves(
        board: Board,
        from: Position,
        piece: Piece,
        directions: List<Pair<Int, Int>>
    ): List<Move> {
        val moves = mutableListOf<Move>()
        for ((dr, dc) in directions) {
            var row = from.row + dr
            var col = from.col + dc
            while (row in 0..7 && col in 0..7) {
                val to = Position(row, col)
                val target = board.pieceAt(to)
                if (target == null) {
                    moves.add(Move(from, to))
                } else {
                    if (target.color != piece.color) {
                        moves.add(Move(from, to))
                    }
                    break
                }
                row += dr
                col += dc
            }
        }
        return moves
    }

    private fun generateKingMoves(
        board: Board,
        from: Position,
        piece: Piece,
        castlingRights: CastlingRights
    ): List<Move> {
        val moves = mutableListOf<Move>()
        for (dr in -1..1) {
            for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                val row = from.row + dr
                val col = from.col + dc
                if (row !in 0..7 || col !in 0..7) continue
                val to = Position(row, col)
                if (isValidMoveTarget(board, piece, to)) {
                    moves.add(Move(from, to))
                }
            }
        }

        val row = from.row
        if (piece.color == PieceColor.WHITE && row == 7) {
            addCastlingMoves(board, from, piece, castlingRights, moves)
        } else if (piece.color == PieceColor.BLACK && row == 0) {
            addCastlingMoves(board, from, piece, castlingRights, moves)
        }
        return moves
    }

    private fun addCastlingMoves(
        board: Board,
        from: Position,
        piece: Piece,
        castlingRights: CastlingRights,
        moves: MutableList<Move>
    ) {
        val row = from.row
        val enemy = piece.color.opposite()

        if (castlingRights.canCastle(piece.color, kingSide = true)) {
            val f = Position(row, 5)
            val g = Position(row, 6)
            if (board.pieceAt(f) == null && board.pieceAt(g) == null &&
                !isSquareAttacked(board, from, enemy) &&
                !isSquareAttacked(board, f, enemy) &&
                !isSquareAttacked(board, g, enemy)
            ) {
                moves.add(Move(from, g, kind = MoveKind.CASTLING))
            }
        }

        if (castlingRights.canCastle(piece.color, kingSide = false)) {
            val b = Position(row, 1)
            val c = Position(row, 2)
            val d = Position(row, 3)
            if (board.pieceAt(b) == null && board.pieceAt(c) == null && board.pieceAt(d) == null &&
                !isSquareAttacked(board, from, enemy) &&
                !isSquareAttacked(board, c, enemy) &&
                !isSquareAttacked(board, d, enemy)
            ) {
                moves.add(Move(from, c, kind = MoveKind.CASTLING))
            }
        }
    }

    private fun isValidMoveTarget(board: Board, piece: Piece, to: Position): Boolean {
        val target = board.pieceAt(to)
        return target == null || target.color != piece.color
    }

    private fun canAttackSquare(
        board: Board,
        piece: Piece,
        from: Position,
        target: Position,
        enPassantTarget: Position?
    ): Boolean {
        if (from == target) return false
        return when (piece.type) {
            PieceType.PAWN -> {
                val direction = if (piece.color == PieceColor.WHITE) -1 else 1
                target.row == from.row + direction && kotlin.math.abs(target.col - from.col) == 1
            }
            PieceType.KNIGHT -> {
                val dr = kotlin.math.abs(target.row - from.row)
                val dc = kotlin.math.abs(target.col - from.col)
                (dr == 2 && dc == 1) || (dr == 1 && dc == 2)
            }
            PieceType.BISHOP -> canSlideAttack(board, from, target, listOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1))
            PieceType.ROOK -> canSlideAttack(board, from, target, listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1))
            PieceType.QUEEN -> canSlideAttack(
                board, from, target,
                listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)
            )
            PieceType.KING -> {
                kotlin.math.abs(target.row - from.row) <= 1 && kotlin.math.abs(target.col - from.col) <= 1
            }
        }
    }

    private fun canSlideAttack(
        board: Board,
        from: Position,
        target: Position,
        directions: List<Pair<Int, Int>>
    ): Boolean {
        for ((dr, dc) in directions) {
            var row = from.row + dr
            var col = from.col + dc
            while (row in 0..7 && col in 0..7) {
                if (row == target.row && col == target.col) return true
                if (board.pieceAt(Position(row, col)) != null) break
                row += dr
                col += dc
            }
        }
        return false
    }
}
