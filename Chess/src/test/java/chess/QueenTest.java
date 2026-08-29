package chess;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class QueenTest {
    @Test
    void testValidMoves() {
        ChessBoard board = new ChessBoard();
        Queen queen = new Queen(4, 4, "white", null);
        board.setPiece(4, 4, queen);

        assertTrue(queen.isValidMove(board, 3, 4));
        assertTrue(queen.isValidMove(board, 2, 4));
        assertTrue(queen.isValidMove(board, 4, 3));
        assertTrue(queen.isValidMove(board, 4, 2));

        assertTrue(queen.isValidMove(board, 3, 3));
        assertTrue(queen.isValidMove(board, 2, 2));
        assertTrue(queen.isValidMove(board, 5, 5));
    }

    @Test
    void testInvalidMoves() {
        ChessBoard board = new ChessBoard();
        Queen queen = new Queen(4, 4, "white", null);
        board.setPiece(4, 4, queen);

        assertFalse(queen.isValidMove(board, 3, 2));
        assertFalse(queen.isValidMove(board, 6, 5));
    }

    @Test
    void testCannotStayOnSquare() {
        ChessBoard board = new ChessBoard();
        Queen queen = new Queen(4, 4, "white", null);
        board.setPiece(4, 4, queen);

        assertFalse(queen.isValidMove(board, 4, 4));
    }

    @Test
    void testCannotJumpOverPieceVertically() {
        ChessBoard board = new ChessBoard();
        Queen queen = new Queen(4, 4, "white", null);
        board.setPiece(4, 4, queen);
        Pawn blackPawn = new Pawn(3, 4, "black", null);
        board.setPiece(3, 4, blackPawn);

        assertFalse(queen.isValidMove(board, 2, 4));
    }

    @Test
    void testCannotJumpOverPieceHorizontally() {
        ChessBoard board = new ChessBoard();
        Queen queen = new Queen(4, 4, "white", null);
        board.setPiece(4, 4, queen);
        Pawn blackPawn = new Pawn(4, 3, "black", null);
        board.setPiece(4, 3, blackPawn);

        assertFalse(queen.isValidMove(board, 4, 2));
    }

    @Test
    void testCannotJumpOverPieceDiagonally() {
        ChessBoard board = new ChessBoard();
        Queen queen = new Queen(4, 4, "white", null);
        board.setPiece(4, 4, queen);
        Pawn blackPawn = new Pawn(3, 3, "black", null);
        board.setPiece(3, 3, blackPawn);

        assertFalse(queen.isValidMove(board, 2, 2));
    }

    @Test
    void testCanCapturePiece() {
        ChessBoard board = new ChessBoard();
        Queen queen = new Queen(4, 4, "white", null);
        board.setPiece(4, 4, queen);
        Pawn blackPawn = new Pawn(3, 4, "black", null);
        board.setPiece(3, 4, blackPawn);

        assertTrue(queen.isValidMove(board, 3, 4));
    }

    @Test
    void testCanCapturePieceDiagonally() {
        ChessBoard board = new ChessBoard();
        Queen queen = new Queen(4, 4, "white", null);
        board.setPiece(4, 4, queen);
        Pawn blackPawn = new Pawn(3, 3, "black", null);
        board.setPiece(3, 3, blackPawn);

        assertTrue(queen.isValidMove(board, 3, 3));
    }

    @Test
    void testCannotCaptureOwnPiece() {
        ChessBoard board = new ChessBoard();
        Queen queen = new Queen(4, 4, "white", null);
        board.setPiece(4, 4, queen);
        Pawn whitePawn = new Pawn(3, 4, "white", null);
        board.setPiece(3, 4, whitePawn);

        assertFalse(queen.isValidMove(board, 3, 4));
    }

    @Test
    void testCannotCaptureOwnPieceDiagonally() {
        ChessBoard board = new ChessBoard();
        Queen queen = new Queen(4, 4, "white", null);
        board.setPiece(4, 4, queen);
        Pawn whitePawn = new Pawn(3, 3, "white", null);
        board.setPiece(3, 3, whitePawn);

        assertFalse(queen.isValidMove(board, 3, 3));
    }

}
