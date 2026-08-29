package chess;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class KingTest {
    @Test
    void testValidMoves() {
        ChessBoard board = new ChessBoard();
        King king = new King(4, 4, "white", null);
        board.setPiece(4, 4, king);

        assertTrue(king.isValidMove(board, 3, 4));
        assertTrue(king.isValidMove(board, 5, 4));
        assertTrue(king.isValidMove(board, 4, 3));
        assertTrue(king.isValidMove(board, 4, 5));
        assertTrue(king.isValidMove(board, 5, 5));
        assertTrue(king.isValidMove(board, 3, 3));
    }

    @Test
    void testCannotMoveMoreThanOne() {
        ChessBoard board = new ChessBoard();
        King king = new King(4, 4, "white", null);
        board.setPiece(4, 4, king);

        assertFalse(king.isValidMove(board, 2, 4));
        assertFalse(king.isValidMove(board, 6, 4));
        assertFalse(king.isValidMove(board, 4, 2));
        assertFalse(king.isValidMove(board, 4, 6));
        assertFalse(king.isValidMove(board, 6, 6));
        assertFalse(king.isValidMove(board, 2, 2));
    }

    @Test
    void testCannotStayOnSquare() {
        ChessBoard board = new ChessBoard();
        King king = new King(4, 4, "white", null);
        board.setPiece(4, 4, king);
        assertFalse(king.isValidMove(board, 4, 4));
    }

    @Test
    void testCanCapture() {
        ChessBoard board = new ChessBoard();
        King king = new King(4, 4, "white", null);
        Pawn pawn = new Pawn(4, 3, "black", null);
        board.setPiece(4, 4, king);
        board.setPiece(4, 3, pawn);
        assertTrue(king.isValidMove(board, 4, 3));
    }

    @Test
    void testCannotCaptureOwnPiece() {
        ChessBoard board = new ChessBoard();
        King king = new King(4, 4, "white", null);
        Pawn pawn = new Pawn(3, 3, "white", null);
        board.setPiece(4, 4, king);
        board.setPiece(3, 3, pawn);
        assertFalse(king.isValidMove(board, 3, 3));
    }
}
