package chess;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class KnightTest {
    @Test
    void testValidMoves() {
        ChessBoard board = new ChessBoard();
        Knight knight = new Knight(4, 4, "white", null);
        board.setPiece(4, 4, knight);

        assertTrue(knight.isValidMove(board, 2, 3));
        assertTrue(knight.isValidMove(board, 2, 5));
        assertTrue(knight.isValidMove(board, 3, 2));
        assertTrue(knight.isValidMove(board, 3, 6));
    }

    @Test
    void testCannotMoveMoreThanOne() {
        ChessBoard board = new ChessBoard();
        Knight knight = new Knight(4, 4, "white", null);
        board.setPiece(4, 4, knight);

        assertFalse(knight.isValidMove(board, 4, 5));
        assertFalse(knight.isValidMove(board, 4, 3));
        assertFalse(knight.isValidMove(board, 3, 4));
        assertFalse(knight.isValidMove(board, 5, 4));
        assertFalse(knight.isValidMove(board, 2, 2));
    }

    @Test
    void testCannotStayOnSquare() {
        ChessBoard board = new ChessBoard();
        Knight knight = new Knight(4, 4, "white", null);
        board.setPiece(4, 4, knight);
        assertFalse(knight.isValidMove(board, 4, 4));
    }

    @Test
    void testCanCapture() {
        ChessBoard board = new ChessBoard();
        Knight knight = new Knight(4, 4, "white", null);
        Pawn pawn = new Pawn(2, 3, "black", null);
        board.setPiece(4, 4, knight);
        board.setPiece(2, 3, pawn);
        assertTrue(knight.isValidMove(board, 2, 3));
    }

    @Test
    void testCannotCaptureOwnPiece() {
        ChessBoard board = new ChessBoard();
        Knight knight = new Knight(4, 4, "white", null);
        Pawn pawn = new Pawn(2, 3, "white", null);
        board.setPiece(4, 4, knight);
        board.setPiece(2, 3, pawn);
        assertFalse(knight.isValidMove(board, 2, 3));
    }

    @Test
    void testCanJumpOverPiece() {
        ChessBoard board = new ChessBoard();
        Knight knight = new Knight(4, 4, "white", null);
        Pawn pawn = new Pawn(4, 3, "white", null);
        board.setPiece(4, 4, knight);
        board.setPiece(4, 3, pawn);
        assertTrue(knight.isValidMove(board, 2, 3));
    }

}
