package chess;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PawnTest {
    @Test
    void testValidMoves() {
        ChessBoard board = new ChessBoard();
        Pawn whitePawn = new Pawn(5, 4, "white", null);
        board.setPiece(5, 4, whitePawn);
        Pawn blackPawn = new Pawn(3, 5, "black", null);
        board.setPiece(3, 5, blackPawn);

        assertTrue(whitePawn.isValidMove(board, 3, 4));
        assertTrue(whitePawn.isValidMove(board, 4, 4));

        assertTrue(blackPawn.isValidMove(board, 5, 5));
        assertTrue(blackPawn.isValidMove(board, 4, 5));
    }

    @Test
    void testCannotMoveTwoAfterFirstMove() {
        ChessBoard board = new ChessBoard();
        Pawn whitePawn = new Pawn(5, 4, "white", null);
        board.setPiece(5, 4, whitePawn);
        Pawn blackPawn = new Pawn(3, 5, "black", null);
        board.setPiece(3, 5, blackPawn);

        whitePawn.incrementMovesMade();
        blackPawn.incrementMovesMade();

        assertFalse(whitePawn.isValidMove(board, 3, 4));
        assertFalse(blackPawn.isValidMove(board, 5, 5));
    }

    @Test
    void testCannotMoveBackwards() {
        ChessBoard board = new ChessBoard();
        Pawn whitePawn = new Pawn(4, 4, "white", null);
        board.setPiece(4, 4, whitePawn);
        Pawn blackPawn = new Pawn(4, 5, "black", null);
        board.setPiece(4, 5, blackPawn);

        assertFalse(whitePawn.isValidMove(board, 5, 4));
        assertFalse(blackPawn.isValidMove(board, 3, 5));
    }

    @Test
    void testCannotMoveSidewards() {
        ChessBoard board = new ChessBoard();
        Pawn whitePawn = new Pawn(4, 4, "white", null);
        board.setPiece(4, 4, whitePawn);
        Pawn blackPawn = new Pawn(4, 5, "black", null);
        board.setPiece(4, 5, blackPawn);

        assertFalse(whitePawn.isValidMove(board, 4, 3));
        assertFalse(blackPawn.isValidMove(board, 4, 6));
    }

    @Test
    void testCannotMoveOntoOccupiedSquare() {
        ChessBoard board = new ChessBoard();
        Pawn whitePawn = new Pawn(4, 4, "white", null);
        board.setPiece(4, 4, whitePawn);
        Pawn blackPawn = new Pawn(3, 4, "black", null);
        board.setPiece(3, 4, blackPawn);

        assertFalse(whitePawn.isValidMove(board, 3, 4));
    }

    @Test
    void testCannotJumpOverPiece() {
        ChessBoard board = new ChessBoard();
        Pawn whitePawn = new Pawn(4, 4, "white", null);
        board.setPiece(4, 4, whitePawn);
        Pawn blackPawn = new Pawn(3, 4, "black", null);
        board.setPiece(3, 4, blackPawn);

        assertFalse(whitePawn.isValidMove(board, 2, 4));
    }

    @Test
    void testCanCapturePiece() {
        ChessBoard board = new ChessBoard();
        Pawn whitePawn = new Pawn(4, 4, "white", null);
        board.setPiece(4, 4, whitePawn);
        Pawn blackPawn = new Pawn(3, 3, "black", null);
        board.setPiece(3, 3, blackPawn);

        assertTrue(whitePawn.isValidMove(board, 3, 3));
    }

    @Test
    void testCannotCaptureOwnPiece() {
        ChessBoard board = new ChessBoard();
        Pawn whitePawn = new Pawn(4, 4, "white", null);
        board.setPiece(4, 4, whitePawn);
        Pawn whitePawn2 = new Pawn(3, 3, "white", null);
        board.setPiece(3, 3, whitePawn2);

        assertFalse(whitePawn.isValidMove(board, 3, 3));
    }

    @Test
    void testCannotCaptureEmptySquare() {
        ChessBoard board = new ChessBoard();
        Pawn whitePawn = new Pawn(4, 4, "white", null);
        board.setPiece(4, 4, whitePawn);

        assertFalse(whitePawn.isValidMove(board, 3, 3));
    }

}
