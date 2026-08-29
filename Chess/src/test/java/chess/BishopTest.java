package chess;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BishopTest {
    @Test
    void testDiagonal() {
        ChessBoard board = new ChessBoard();
        Bishop bishop = new Bishop(4, 4, "white", null);
        board.setPiece(4, 4, bishop);

        assertTrue(bishop.isValidMove(board, 3, 3));
        assertTrue(bishop.isValidMove(board, 5, 5));
    }

    @Test
    void testNonDiagonal() {
        ChessBoard board = new ChessBoard();
        Bishop bishop = new Bishop(4, 4, "white", null);
        board.setPiece(4, 4, bishop);

        assertFalse(bishop.isValidMove(board, 4, 5));
        assertFalse(bishop.isValidMove(board, 4, 3));
        assertFalse(bishop.isValidMove(board, 5, 4));
        assertFalse(bishop.isValidMove(board, 3, 4));
        assertFalse(bishop.isValidMove(board, 2, 5));
    }

    @Test
    void testCannotStayOnSquare() {
        ChessBoard board = new ChessBoard();
        Bishop bishop = new Bishop(4, 4, "white", null);
        board.setPiece(4, 4, bishop);
        assertFalse(bishop.isValidMove(board, 4, 4));
    }

    @Test
    void testCanCapture() {
        ChessBoard board = new ChessBoard();
        Bishop bishop = new Bishop(4, 4, "white", null);
        Pawn pawn = new Pawn(2, 2, "black", null);
        board.setPiece(4, 4, bishop);
        board.setPiece(2, 2, pawn);
        assertTrue(bishop.isValidMove(board, 2, 2));
    }

    @Test
    void testCannotCaptureOwnPiece() {
        ChessBoard board = new ChessBoard();
        Bishop bishop = new Bishop(4, 4, "white", null);
        Pawn pawn = new Pawn(2, 2, "white", null);
        board.setPiece(4, 4, bishop);
        board.setPiece(2, 2, pawn);
        assertFalse(bishop.isValidMove(board, 2, 2));
    }

    @Test
    void testCannotJumpOverPiece() {
        ChessBoard board = new ChessBoard();
        Bishop bishop = new Bishop(4, 4, "white", null);
        Pawn pawn = new Pawn(2, 2, "white", null);
        board.setPiece(4, 4, bishop);
        board.setPiece(3, 3, pawn);
        assertFalse(bishop.isValidMove(board, 2, 2));
    }

}
