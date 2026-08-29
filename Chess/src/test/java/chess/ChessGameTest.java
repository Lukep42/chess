package chess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ChessGameTest {
    @Test
    void testInitialGameState() {
        ChessGame game = new ChessGame();
        assertEquals("white", game.getCurrentTurn());
        assertFalse(game.getGameOver());
        assertNull(game.getLastMoved());
    }

    @Test
    void testSetCurrentTuren() {
        ChessGame game = new ChessGame();
        game.setCurrentTurn("black");
        assertEquals("black", game.getCurrentTurn());
    }

    @Test
    void testSetGameover() {
        ChessGame game = new ChessGame();
        game.setGameOver(true);
        assertEquals(true, game.getGameOver());
    }

    @Test
    void testReset() {
        ChessGame game = new ChessGame();
        Piece pawn = game.getBoard().getPiece(6, 0);
        assertTrue(game.makeMove(pawn, 4, 0));
        game.setGameOver(true);
        game.reset();
        assertEquals("white", game.getCurrentTurn());
        assertFalse(game.getGameOver());
        assertTrue(game.getBoard().getPiece(0, 3) instanceof Queen);
        assertTrue(game.getBoard().getPiece(0, 4) instanceof King);
        assertNull(game.getLastMoved());
    }

    @Test
    void testCannontMakeInvalidMove() {
        ChessGame game = new ChessGame();
        Piece pawn = game.getBoard().getPiece(6, 0);
        assertFalse(game.makeMove(pawn, 6, 1));
        assertEquals("white", game.getCurrentTurn());
        assertSame(pawn, game.getBoard().getPiece(6, 0));
    }

    @Test
    void testCannotMoveIntoCheck() {
        ChessGame game = new ChessGame();
        ChessBoard board = game.getBoard();
        Piece queen = board.getPiece(0, 3);
        board.setPiece(0, 0, null);
        board.setPiece(4, 7, queen);
        queen.setRow(4);
        queen.setCol(7);

        Piece pawn = board.getPiece(6, 5);
        assertFalse(game.makeMove(pawn, 4, 5));
        assertEquals("white", game.getCurrentTurn());
    }

    @Test
    void testCannotMoveOppositeColour() {
        ChessGame game = new ChessGame();
        Piece blackPawn = game.getBoard().getPiece(1, 0);
        assertFalse(game.makeMove(blackPawn, 3, 0));
        assertEquals("white", game.getCurrentTurn());
    }

    @Test
    void testTurnChanges() {
        ChessGame game = new ChessGame();
        Piece whitePawn = game.getBoard().getPiece(6, 0);
        assertTrue(game.makeMove(whitePawn, 5, 0));
        assertEquals("black", game.getCurrentTurn());
    }

    @Test
    void testGameOver() {
        ChessGame game = new ChessGame();
        game.setGameOver(true);
        Piece whitePawn = game.getBoard().getPiece(6, 0);
        assertFalse(game.makeMove(whitePawn, 5, 0));
        assertEquals("white", game.getCurrentTurn());
    }

    @Test
    void testLastMoved() {
        ChessGame game = new ChessGame();
        Piece pawn = game.getBoard().getPiece(6, 0);
        assertTrue(game.makeMove(pawn, 5, 0));
        assertSame(pawn, game.getLastMoved());
    }

    @Test
    void testIsCheck() {
        ChessGame game = new ChessGame();
        ChessBoard board = game.getBoard();
        Piece queen = board.getPiece(0, 3);
        board.setPiece(0, 0, null);
        board.setPiece(4, 7, queen);
        queen.setRow(4);
        queen.setCol(7);

        Piece pawn = board.getPiece(6, 5);
        board.setPiece(6, 5, null);
        board.setPiece(5, 5, pawn);
        pawn.setRow(5);
        pawn.setCol(5);
        game.setCurrentTurn("white");
        assertTrue(game.isChecked());
    }

    @Test
    void testEnPassant() {
        ChessGame game = new ChessGame();
        ChessBoard board = game.getBoard();
        Piece whitePawn = board.getPiece(6, 6);
        board.setPiece(6, 6, null);
        board.setPiece(3, 6, whitePawn);
        whitePawn.setRow(3);
        whitePawn.setCol(6);

        Piece blackPawn = board.getPiece(1, 7);
        game.setCurrentTurn("black");
        assertTrue(game.makeMove(blackPawn, 3, 7));
        assertTrue(game.makeMove(whitePawn, 2, 7));
        assertSame(whitePawn, board.getPiece(2, 7));
        assertNull(board.getPiece(3, 7));
    }

    @Test
    void testCheckmate() {
        ChessGame game = new ChessGame();
        assertFalse(game.isCheckmated());
    }

    @Test
    void testStalemate() {
        ChessGame game = new ChessGame();
        assertFalse(game.isStalemate());
    }

    @Test
    void testThreeRepetition() {
        ChessGame game = new ChessGame();
        ChessBoard board = game.getBoard();
        Piece whiteKnight = board.getPiece(7, 1);
        Piece blackKnight = board.getPiece(0, 1);

        for (int i = 0; i < 2; i++) {
            assertTrue(game.makeMove(whiteKnight, 5, 2));
            assertTrue(game.makeMove(blackKnight, 2, 2));

            assertTrue(game.makeMove(whiteKnight, 7, 1));
            assertTrue(game.makeMove(blackKnight, 0, 1));
        }

        assertTrue(game.isThreeRepetition());
    }

    @Test
    void testFiftyMoveDraw() {
        ChessGame game = new ChessGame();
        assertFalse(game.isFiftyMoveDraw());
    }

    @Test
    void testSeventyFiftyMoveDraw() {
        ChessGame game = new ChessGame();
        assertFalse(game.isSeventyFiveMoveDraw());
    }

    @Test
    void testInsufficientMaterial() {
        ChessGame game = new ChessGame();
        ChessBoard board = game.getBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (!(board.getPiece(row, col) instanceof King)) {
                    board.setPiece(row, col, null);
                }
            }
        }
        assertTrue(game.isInsufficientMaterial());
    }

}
