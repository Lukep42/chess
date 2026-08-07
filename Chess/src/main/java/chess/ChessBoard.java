package chess;

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
}
