package chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ChessBoardTest {
    @Test
    void testBoard() {
        ChessBoard board = new ChessBoard();

        assertTrue(board.getPiece(0, 0) instanceof Rook);
        assertTrue(board.getPiece(0, 1) instanceof Knight);
        assertTrue(board.getPiece(0, 2) instanceof Bishop);
        assertTrue(board.getPiece(0, 3) instanceof Queen);
        assertTrue(board.getPiece(0, 4) instanceof King);
        assertTrue(board.getPiece(0, 5) instanceof Bishop);
        assertTrue(board.getPiece(0, 6) instanceof Knight);
        assertTrue(board.getPiece(0, 7) instanceof Rook);

        // black panws
        for (int col = 0; col < 8; col++) {
            assertTrue(board.getPiece(1, col) instanceof Pawn);
        }

        // white pawns
        for (int col = 0; col < 8; col++) {
            assertTrue(board.getPiece(6, col) instanceof Pawn);
        }

        assertTrue(board.getPiece(7, 0) instanceof Rook);
        assertTrue(board.getPiece(7, 1) instanceof Knight);
        assertTrue(board.getPiece(7, 2) instanceof Bishop);
        assertTrue(board.getPiece(7, 3) instanceof Queen);
        assertTrue(board.getPiece(7, 4) instanceof King);
        assertTrue(board.getPiece(7, 5) instanceof Bishop);
        assertTrue(board.getPiece(7, 6) instanceof Knight);
        assertTrue(board.getPiece(7, 7) instanceof Rook);
    }

    @Test
    void testPieceColour() {
        ChessBoard board = new ChessBoard();
        assertEquals("white", board.getPiece(6, 3).getColour());
        assertEquals("black", board.getPiece(1, 6).getColour());
    }

    @Test
    void testEmptySquares() {
        ChessBoard board = new ChessBoard();
        assertNull(board.getPiece(4, 4));
        assertNull(board.getPiece(2, 6));
    }

    @Test
    void testSetPiece() {
        ChessBoard board = new ChessBoard();
        Piece newPiece = new Queen(4, 3, "white", null);
        board.setPiece(4, 4, newPiece);
        assertSame(newPiece, board.getPiece(4, 4));
    }

    @Test
    void testGetPieces() {
        ChessBoard board = new ChessBoard();

        List<Piece> whitePieces = board.getPieces("white");
        assertEquals(16, whitePieces.size());

        for (Piece piece : whitePieces) {
            assertEquals("white", piece.getColour());
        }
    }

    @Test
    void testMovePiece() {
        ChessBoard board = new ChessBoard();
        Piece pawn = board.getPiece(6, 0);
        assertNotNull(pawn);
        assertEquals(6, pawn.getRow());
        assertEquals(0, pawn.getCol());

        board.movePiece(pawn, 5, 0);

        assertNull(board.getPiece(6, 0));
        assertSame(pawn, board.getPiece(5, 0));
        assertEquals(5, pawn.getRow());
        assertEquals(0, pawn.getCol());
        assertEquals(1, pawn.getMovesMade());
    }

    @Test
    void testGetKing() {
        ChessBoard board = new ChessBoard();
        Piece king = board.getPiece(0, 4);

        assertEquals(board.getPiece(0, 4), king);
        assertTrue(king instanceof King);
    }

    @Test
    void testMakeTemporaryMove() {
        ChessBoard board = new ChessBoard();

        Piece pawn = board.getPiece(6, 0);
        Piece capturedPiece = board.makeTemporaryMove(pawn, 5, 0);

        assertNull(capturedPiece);
        assertNull(board.getPiece(6, 0));
        assertSame(pawn, board.getPiece(5, 0));

        assertEquals(5, pawn.getRow());
        assertEquals(0, pawn.getCol());

        board.undoMove(pawn, 6, 0, capturedPiece);

        assertSame(pawn, board.getPiece(6, 0));
        assertNull(board.getPiece(5, 0));

        assertEquals(6, pawn.getRow());
        assertEquals(0, pawn.getCol());
    }

    @Test
    void testIsAttacked() {
        ChessBoard board = new ChessBoard();
        board.setPiece(1, 0, null);
        Queen blackQueen = new Queen(4, 3, "black", null);
        board.setPiece(4, 3, blackQueen);
        assertTrue(board.isAttacked(5, 2, "white"));
        assertTrue(board.isAttacked(3, 3, "white"));
        assertTrue(board.isAttacked(4, 4, "white"));
    }

    @Test
    void testCastleBlocked() {
        ChessBoard board = new ChessBoard();
        Piece king = board.getKing("white");
        assertFalse(board.canCastle(king, 7, 6));
        assertFalse(board.canCastle(king, 7, 2));
    }

    @Test
    void testCastleKingSide() {
        ChessBoard board = new ChessBoard();
        Piece king = board.getKing("white");
        board.setPiece(7, 5, null);
        board.setPiece(7, 6, null);

        assertTrue(board.canCastle(king, 7, 6));
    }

    @Test
    void testCastleQueenSide() {
        ChessBoard board = new ChessBoard();
        Piece king = board.getKing("white");
        board.setPiece(7, 3, null);
        board.setPiece(7, 2, null);
        board.setPiece(7, 1, null);

        assertTrue(board.canCastle(king, 7, 2));
    }

    @Test
    void testCastleWhenKingHasMoved() {
        ChessBoard board = new ChessBoard();
        Piece king = board.getKing("white");
        board.setPiece(7, 5, null);
        board.setPiece(7, 6, null);
        king.incrementMovesMade();
        assertFalse(board.canCastle(king, 7, 6));
    }

    @Test
    void testCastleWhenRookHasMoved() {
        ChessBoard board = new ChessBoard();
        Piece king = board.getKing("white");
        Piece rook = board.getPiece(7, 7);
        board.setPiece(7, 5, null);
        board.setPiece(7, 6, null);
        rook.incrementMovesMade();
        assertFalse(board.canCastle(king, 7, 6));
    }

    @Test
    void testCastleWithAttackedSquare() {
        ChessBoard board = new ChessBoard();
        Piece king = board.getKing("white");
        board.setPiece(7, 5, null);
        board.setPiece(7, 6, null);
        Rook blackRook = new Rook(5, 5, "black", null);
        board.setPiece(5, 5, blackRook);
        board.setPiece(6, 5, null);

        assertTrue(board.isAttacked(7, 5, "white"));
        assertFalse(board.canCastle(king, 7, 6));
    }

    @Test
    void testCastleMoveKingSide() {
        ChessBoard board = new ChessBoard();
        Piece king = board.getKing("white");
        Piece rook = board.getPiece(7, 7);

        board.setPiece(7, 5, null);
        board.setPiece(7, 6, null);

        assertTrue(board.canCastle(king, 7, 6));

        board.castleMove(king, 7, 6);
        assertSame(king, board.getPiece(7, 6));
        assertSame(rook, board.getPiece(7, 5));

        assertNull(board.getPiece(7, 4));
        assertNull(board.getPiece(7, 7));

        assertEquals(1, king.getMovesMade());
        assertEquals(1, rook.getMovesMade());
    }

    @Test
    void testCastleMoveQueenSide() {
        ChessBoard board = new ChessBoard();
        Piece king = board.getKing("white");
        Piece rook = board.getPiece(7, 0);

        board.setPiece(7, 3, null);
        board.setPiece(7, 2, null);
        board.setPiece(7, 1, null);

        assertTrue(board.canCastle(king, 7, 2));

        board.castleMove(king, 7, 2);
        assertSame(king, board.getPiece(7, 2));
        assertSame(rook, board.getPiece(7, 3));

        assertNull(board.getPiece(7, 4));
        assertNull(board.getPiece(7, 0));

        assertEquals(1, king.getMovesMade());
        assertEquals(1, rook.getMovesMade());
    }

}
