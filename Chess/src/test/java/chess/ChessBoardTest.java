package chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChessBoardTest {
    @Test
    void testBoard() {
        ChessBoard board = new ChessBoard();

        assertTrue(board.getPiece(0, 0) instanceof Rook);
        assertTrue(board.getPiece(0, 4) instanceof King);
        assertTrue(board.getPiece(1, 0) instanceof Pawn);

        assertTrue(board.getPiece(7, 7) instanceof Rook);
        assertTrue(board.getPiece(7, 4) instanceof King);
        assertTrue(board.getPiece(6, 3) instanceof Pawn);
    }

    @Test
    void testPieceColour() {
        ChessBoard board = new ChessBoard();
        assertEquals("white", board.getPiece(6, 3).getColour());
        assertEquals("black", board.getPiece(1, 6).getColour());
    }

}
