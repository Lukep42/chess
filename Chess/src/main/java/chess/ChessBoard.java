package chess;

import main.java.chess.Piece;

public class ChessBoard {
    private Piece[][] board = new Piece[8][8];

    public ChessBoard() {
        setupBoard();
    }

    private setupBoard(){
         String[] backRank = { "Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook" };
        for (int i = 0; i < 8; i++) {
            //black back rank
            board[i][0] = backRank[i];
            // black pawns
            board[i][1] = backRank[i];

            // white pawns
            addPieceIcon(board, i, 6, "pawn-w.png");
            // white back rank
            addPieceIcon(board, i, 7, backRank[i] + "-w.png");

        }
    }

    public Piece getPiece(int row, int col) {
        return board[row][col];
    }
}
