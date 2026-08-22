package chess;

import java.util.ArrayList;
import java.util.List;

public class ChessBoard {
    private Piece[][] board = new Piece[8][8];

    public ChessBoard() {
        setupBoard();
    }

    private void setupBoard() {
        // black pieces
        board[0][0] = new Rook(0, 0, "black", null);
        board[0][7] = new Rook(0, 7, "black", null);
        board[0][1] = new Knight(0, 1, "black", null);
        board[0][6] = new Knight(0, 6, "black", null);
        board[0][2] = new Bishop(0, 2, "black", null);
        board[0][5] = new Bishop(0, 5, "black", null);
        board[0][3] = new Queen(0, 3, "black", null);
        board[0][4] = new King(0, 4, "black", null, false);

        // pawn setup
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(1, i, "black", null);
            board[6][i] = new Pawn(6, i, "white", null);
        }

        // white pieces
        board[7][0] = new Rook(7, 0, "white", null);
        board[7][7] = new Rook(7, 7, "white", null);
        board[7][1] = new Knight(7, 1, "white", null);
        board[7][6] = new Knight(7, 6, "white", null);
        board[7][2] = new Bishop(7, 2, "white", null);
        board[7][5] = new Bishop(7, 5, "white", null);
        board[7][3] = new Queen(7, 3, "white", null);
        board[7][4] = new King(7, 4, "white", null, false);

    }

    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    public void setPiece(int row, int col, Piece piece) {
        this.board[row][col] = piece;
    }

    public List<Piece> getPieces(String colour) {
        List<Piece> pieces = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.getColour().equals(colour)) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    public boolean movePiece(Piece piece, int newRow, int newCol) {
        int oldRow = piece.getRow();
        int oldCol = piece.getCol();
        // if (piece.isValidMove(this, newRow, newCol)) {
        board[newRow][newCol] = piece;
        board[oldRow][oldCol] = null;
        piece.setRow(newRow);
        piece.setCol(newCol);
        piece.incrementMovesMade();
        return true;
        // }
        // return false;

    }

    public Piece getKing(String colour) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece instanceof King && piece.getColour().equals(colour)) {
                    return piece;
                }
            }
        }
        return null;
    }

    // determines if a square is attacked used for check
    public boolean isAttacked(int rowAttacked, int colAttacked, String kingColour) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece != null && !piece.getColour().equals(kingColour)) {
                    if (piece.isValidMove(this, rowAttacked, colAttacked)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Piece makeTemporaryMove(Piece piece, int newRow, int newCol) {
        int oldRow = piece.getRow();
        int oldCol = piece.getCol();

        Piece caputedPiece = board[newRow][newCol];

        board[newRow][newCol] = piece;
        board[oldRow][oldCol] = null;

        piece.setRow(newRow);
        piece.setCol(newCol);

        return caputedPiece;
    }

    public void undoMove(Piece piece, int oldRow, int oldCol, Piece capturedPiece) {
        int newRow = piece.getRow();
        int newCol = piece.getCol();

        board[oldRow][oldCol] = piece;
        board[newRow][newCol] = capturedPiece;

        piece.setRow(oldRow);
        piece.setCol(oldCol);
    }

    public boolean canCastle(Piece king, int newRow, int newCol) {
        int row = king.getRow();
        Piece rook;

        if (newRow != row) {
            return false;
        }

        if (isAttacked(row, king.getCol(), king.getColour())) {
            return false;
        }

        boolean kingSide = newCol > king.getCol();
        int rookCol = kingSide ? 7 : 0;
        rook = board[row][rookCol];

        if (!(rook instanceof Rook) || !rook.getColour().equals(king.getColour())) {
            return false;
        }

        if (king.getMovesMade() != 0 || rook.getMovesMade() != 0) {
            return false;
        }
        int direction = kingSide ? 1 : -1;

        for (int col = king.getCol() + direction; col != rookCol; col += direction) {
            if (board[row][col] != null) {
                return false;
            }
        }

        int middleCol = king.getCol() + direction;

        if (isAttacked(row, middleCol, king.getColour())) {
            return false;
        }

        if (isAttacked(row, newCol, king.getColour())) {
            return false;
        }
        return true;
    }

    public boolean castleMove(Piece piece, int newRow, int newCol) {
        boolean kingSide = newCol > piece.getCol();
        int rookOldCol = kingSide ? 7 : 0;
        int rookNewCol = kingSide ? 5 : 3;

        Piece rook = getPiece(piece.getRow(), rookOldCol);

        movePiece(piece, newRow, newCol);

        board[newRow][rookNewCol] = rook;
        board[newRow][rookOldCol] = null;
        rook.setRow(newRow);
        rook.setCol(rookNewCol);
        rook.incrementMovesMade();
        return true;
    }
}
