package chess;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RookTest {
    @Test
    void testValidMoves() {
        ChessBoard board = new ChessBoard();
        Rook rook = new Rook(4, 4, "white", null);
        board.setPiece(4, 4, rook);

        assertTrue(rook.isValidMove(board, 3, 4));
        assertTrue(rook.isValidMove(board, 2, 4));
        assertTrue(rook.isValidMove(board, 4, 3));
        assertTrue(rook.isValidMove(board, 4, 2));

    }

    @Test
    void testInvalidMoves() {
        ChessBoard board = new ChessBoard();
        Rook rook = new Rook(4, 4, "white", null);
        board.setPiece(4, 4, rook);

        assertFalse(rook.isValidMove(board, 3, 3));
        assertFalse(rook.isValidMove(board, 2, 2));
    }

    @Test
    void testCannotStayOnSquare() {
        ChessBoard board = new ChessBoard();
        Rook rook = new Rook(4, 4, "white", null);
        board.setPiece(4, 4, rook);

        assertFalse(rook.isValidMove(board, 4, 4));
    }

    @Test
    void testCannotJumpOverPieceVertically() {
        ChessBoard board = new ChessBoard();
        Rook rook = new Rook(4, 4, "white", null);
        board.setPiece(4, 4, rook);
        Pawn blackPawn = new Pawn(3, 4, "black", null);
        board.setPiece(3, 4, blackPawn);

        assertFalse(rook.isValidMove(board, 2, 4));
    }

    @Test
    void testCannotJumpOverPieceHorizontally() {
        ChessBoard board = new ChessBoard();
        Rook rook = new Rook(4, 4, "white", null);
        board.setPiece(4, 4, rook);
        Pawn blackPawn = new Pawn(4, 3, "black", null);
        board.setPiece(4, 3, blackPawn);

        assertFalse(rook.isValidMove(board, 4, 2));
    }

    @Test
    void testCanCapturePiece() {
        ChessBoard board = new ChessBoard();
        Rook rook = new Rook(4, 4, "white", null);
        board.setPiece(4, 4, rook);
        Pawn blackPawn = new Pawn(3, 4, "black", null);
        board.setPiece(3, 4, blackPawn);

        assertTrue(rook.isValidMove(board, 3, 4));
    }

    @Test
    void testCannotCaptureOwnPiece() {
        ChessBoard board = new ChessBoard();
        Rook rook = new Rook(4, 4, "white", null);
        board.setPiece(4, 4, rook);
        Pawn whitePawn = new Pawn(3, 4, "white", null);
        board.setPiece(3, 4, whitePawn);

        assertFalse(rook.isValidMove(board, 3, 4));
    }

}
