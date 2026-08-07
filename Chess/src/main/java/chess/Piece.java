package chess;

import chess.ChessBoard;
import chess.PieceIcon;

public abstract class Piece {
    private int row;
    private int col;
    private String colour;
    private PieceIcon icon;
    // mainly for pawn first movement
    private int movesMade;

    public Piece(int row, int col, String colour, PieceIcon icon) {
        this.row = row;
        this.col = col;
        this.colour = colour;
        this.icon = icon;
        this.movesMade = 0;
    }

    public abstract boolean isValidMove(ChessBoard board, int newRow, int newCol);

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getColour() {
        return colour;
    }

    public PieceIcon getIcon() {
        return icon;
    }

    public int getMovesMade() {
        return movesMade;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setIcon(PieceIcon icon) {
        this.icon = icon;
    }

    public void incrementMovesMade() {
        movesMade++;
    }

}
