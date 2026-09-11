package com.example.chessgame.domain.usecase

import com.example.chessgame.domain.model.Board
import com.example.chessgame.domain.model.GameState
import com.example.chessgame.domain.model.Move
import com.example.chessgame.domain.model.Piece
import com.example.chessgame.domain.model.PieceColor
import com.example.chessgame.domain.model.PieceType
import com.example.chessgame.domain.model.Position
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GenerateLegalMovesUseCaseTest {

    private lateinit var generateLegalMoves: GenerateLegalMovesUseCase
    private lateinit var validateMove: ValidateMoveUseCase
    private lateinit var detectCheck: DetectCheckUseCase

    @Before
    fun setup() {
        generateLegalMoves = GenerateLegalMovesUseCase()
        validateMove = ValidateMoveUseCase(generateLegalMoves)
        detectCheck = DetectCheckUseCase()
    }

    @Test
    fun whitePawnCanMoveTwoSquaresFromStart() {
        val state = GameState.initial()
        val moves = generateLegalMoves(
            state.board,
            Position(6, 4),
            state.castlingRights,
            state.enPassantTarget,
            PieceColor.WHITE
        )
        assertTrue(moves.any { it.to == Position(4, 4) })
    }

    @Test
    fun knightMoveIsLegalFromStart() {
        val state = GameState.initial()
        val moves = generateLegalMoves(
            state.board,
            Position(7, 6),
            state.castlingRights,
            state.enPassantTarget,
            PieceColor.WHITE
        )
        assertTrue(moves.any { it.to == Position(5, 5) })
        assertTrue(moves.any { it.to == Position(5, 7) })
    }

    @Test
    fun kingCannotMoveIntoCheck() {
        val board = Board.initial()
            .withPiece(Position(5, 3), Piece(PieceType.QUEEN, PieceColor.BLACK))

        val state = GameState.initial().copy(board = board)
        val kingMoves = generateLegalMoves(
            state.board,
            Position(7, 4),
            state.castlingRights,
            state.enPassantTarget,
            PieceColor.WHITE
        )
        assertFalse(kingMoves.any { it.to == Position(6, 3) })
    }

    @Test
    fun pinnedPieceCannotMove() {
        val board = Board.initial()
            .withPiece(Position(1, 4), null)
            .withPiece(Position(6, 4), Piece(PieceType.KNIGHT, PieceColor.WHITE))
            .withPiece(Position(5, 4), Piece(PieceType.ROOK, PieceColor.BLACK))

        val state = GameState.initial().copy(board = board)
        val knightMoves = generateLegalMoves(
            state.board,
            Position(6, 4),
            state.castlingRights,
            state.enPassantTarget,
            PieceColor.WHITE
        )
        assertTrue(knightMoves.isEmpty())
    }
}

class DetectCheckmateUseCaseTest {

    private lateinit var detectCheckmate: DetectCheckmateUseCase
    private lateinit var makeMove: MakeMoveUseCase

    @Before
    fun setup() {
        val generateLegalMoves = GenerateLegalMovesUseCase()
        val detectCheck = DetectCheckUseCase()
        val detectCheckmateUseCase = DetectCheckmateUseCase(generateLegalMoves, detectCheck)
        val detectStalemate = DetectStalemateUseCase(generateLegalMoves, detectCheck)
        val validateMove = ValidateMoveUseCase(generateLegalMoves)
        val convertNotation = ConvertMoveToNotationUseCase(
            detectCheckmateUseCase,
            detectCheck,
            generateLegalMoves
        )
        detectCheckmate = detectCheckmateUseCase
        makeMove = MakeMoveUseCase(
            validateMove,
            generateLegalMoves,
            detectCheck,
            detectCheckmateUseCase,
            detectStalemate,
            convertNotation
        )
    }

    @Test
    fun foolsMateIsCheckmate() {
        var state = GameState.initial()
        state = makeMove(state, Move(Position(6, 5), Position(5, 5))).getOrThrow()
        state = makeMove(state, Move(Position(1, 4), Position(3, 4))).getOrThrow()
        state = makeMove(state, Move(Position(6, 6), Position(4, 6))).getOrThrow()
        state = makeMove(state, Move(Position(0, 3), Position(4, 7))).getOrThrow()

        assertTrue(detectCheckmate(state))
    }

    @Test
    fun detectsCheckmatePosition() {
        val emptyRow = List(8) { null as Piece? }
        val board = Board(squares = List(8) { emptyRow })
            .withPiece(Position(7, 4), Piece(PieceType.KING, PieceColor.WHITE))
            .withPiece(Position(6, 4), Piece(PieceType.QUEEN, PieceColor.BLACK))
            .withPiece(Position(5, 4), Piece(PieceType.KING, PieceColor.BLACK))

        val state = GameState(
            board = board,
            currentTurn = PieceColor.WHITE,
            castlingRights = com.example.chessgame.domain.model.CastlingRights(
                whiteKingSide = false,
                whiteQueenSide = false,
                blackKingSide = false,
                blackQueenSide = false
            )
        )

        assertTrue(detectCheckmate(state))
    }
}
