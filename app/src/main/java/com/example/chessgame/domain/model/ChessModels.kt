package com.example.chessgame.domain.model

enum class PieceColor {
    WHITE,
    BLACK;

    fun opposite(): PieceColor = if (this == WHITE) BLACK else WHITE
}

enum class PieceType {
    KING,
    QUEEN,
    ROOK,
    BISHOP,
    KNIGHT,
    PAWN
}

data class Piece(
    val type: PieceType,
    val color: PieceColor
)

data class Position(
    val row: Int,
    val col: Int
) {
    init {
        require(row in 0..7 && col in 0..7) { "Position out of bounds: ($row, $col)" }
    }

    fun fileChar(): Char = 'a' + col

    fun rankNumber(): Int = 8 - row

    fun toAlgebraic(): String = "${fileChar()}${rankNumber()}"

    companion object {
        fun fromAlgebraic(notation: String): Position {
            val col = notation[0] - 'a'
            val row = 8 - notation[1].digitToInt()
            return Position(row, col)
        }
    }
}

enum class MoveKind {
    NORMAL,
    CASTLING,
    EN_PASSANT,
    PROMOTION
}

data class Move(
    val from: Position,
    val to: Position,
    val promotionPiece: PieceType? = null,
    val kind: MoveKind = MoveKind.NORMAL
) {
    val isCastling: Boolean get() = kind == MoveKind.CASTLING
    val isEnPassant: Boolean get() = kind == MoveKind.EN_PASSANT
    val isPromotion: Boolean get() = kind == MoveKind.PROMOTION
}

data class CastlingRights(
    val whiteKingSide: Boolean = true,
    val whiteQueenSide: Boolean = true,
    val blackKingSide: Boolean = true,
    val blackQueenSide: Boolean = true
) {
    fun canCastle(color: PieceColor, kingSide: Boolean): Boolean = when (color) {
        PieceColor.WHITE -> if (kingSide) whiteKingSide else whiteQueenSide
        PieceColor.BLACK -> if (kingSide) blackKingSide else blackQueenSide
    }

    fun updateAfterMove(piece: Piece, from: Position, to: Position): CastlingRights {
        var rights = this
        if (piece.type == PieceType.KING) {
            rights = when (piece.color) {
                PieceColor.WHITE -> rights.copy(whiteKingSide = false, whiteQueenSide = false)
                PieceColor.BLACK -> rights.copy(blackKingSide = false, blackQueenSide = false)
            }
        }
        if (piece.type == PieceType.ROOK) {
            rights = when {
                from == Position(7, 0) -> rights.copy(whiteQueenSide = false)
                from == Position(7, 7) -> rights.copy(whiteKingSide = false)
                from == Position(0, 0) -> rights.copy(blackQueenSide = false)
                from == Position(0, 7) -> rights.copy(blackKingSide = false)
                else -> rights
            }
        }
        if (to == Position(7, 0)) rights = rights.copy(whiteQueenSide = false)
        if (to == Position(7, 7)) rights = rights.copy(whiteKingSide = false)
        if (to == Position(0, 0)) rights = rights.copy(blackQueenSide = false)
        if (to == Position(0, 7)) rights = rights.copy(blackKingSide = false)
        return rights
    }
}

data class Board(
    val squares: List<List<Piece?>>
) {
    init {
        require(squares.size == 8 && squares.all { it.size == 8 }) { "Board must be 8x8" }
    }

    fun pieceAt(position: Position): Piece? = squares[position.row][position.col]

    fun withPiece(position: Position, piece: Piece?): Board {
        val newRows = squares.mapIndexed { rowIndex, row ->
            if (rowIndex == position.row) {
                row.mapIndexed { colIndex, p ->
                    if (colIndex == position.col) piece else p
                }
            } else {
                row
            }
        }
        return copy(squares = newRows)
    }

    fun movePiece(from: Position, to: Position): Board {
        val piece = pieceAt(from) ?: return this
        return withPiece(from, null).withPiece(to, piece)
    }

    fun findKing(color: PieceColor): Position? {
        for (row in 0..7) {
            for (col in 0..7) {
                val piece = squares[row][col]
                if (piece?.type == PieceType.KING && piece.color == color) {
                    return Position(row, col)
                }
            }
        }
        return null
    }

    companion object {
        fun initial(): Board {
            val emptyRow = List(8) { null as Piece? }
            val blackBack = listOf(
                Piece(PieceType.ROOK, PieceColor.BLACK),
                Piece(PieceType.KNIGHT, PieceColor.BLACK),
                Piece(PieceType.BISHOP, PieceColor.BLACK),
                Piece(PieceType.QUEEN, PieceColor.BLACK),
                Piece(PieceType.KING, PieceColor.BLACK),
                Piece(PieceType.BISHOP, PieceColor.BLACK),
                Piece(PieceType.KNIGHT, PieceColor.BLACK),
                Piece(PieceType.ROOK, PieceColor.BLACK)
            )
            val blackPawns = List(8) { Piece(PieceType.PAWN, PieceColor.BLACK) }
            val whitePawns = List(8) { Piece(PieceType.PAWN, PieceColor.WHITE) }
            val whiteBack = listOf(
                Piece(PieceType.ROOK, PieceColor.WHITE),
                Piece(PieceType.KNIGHT, PieceColor.WHITE),
                Piece(PieceType.BISHOP, PieceColor.WHITE),
                Piece(PieceType.QUEEN, PieceColor.WHITE),
                Piece(PieceType.KING, PieceColor.WHITE),
                Piece(PieceType.BISHOP, PieceColor.WHITE),
                Piece(PieceType.KNIGHT, PieceColor.WHITE),
                Piece(PieceType.ROOK, PieceColor.WHITE)
            )
            return Board(
                squares = listOf(
                    blackBack,
                    blackPawns,
                    emptyRow,
                    emptyRow,
                    emptyRow,
                    emptyRow,
                    whitePawns,
                    whiteBack
                )
            )
        }
    }
}

enum class GameStatus {
    IN_PROGRESS,
    CHECK,
    CHECKMATE,
    STALEMATE
}

data class RecordedMove(
    val move: Move,
    val notation: String,
    val color: PieceColor
)

data class GameState(
    val board: Board,
    val currentTurn: PieceColor,
    val castlingRights: CastlingRights = CastlingRights(),
    val enPassantTarget: Position? = null,
    val halfMoveClock: Int = 0,
    val fullMoveNumber: Int = 1,
    val status: GameStatus = GameStatus.IN_PROGRESS,
    val moveHistory: List<RecordedMove> = emptyList(),
    val winner: PieceColor? = null
) {
    companion object {
        fun initial(): GameState = GameState(
            board = Board.initial(),
            currentTurn = PieceColor.WHITE
        )
    }
}
