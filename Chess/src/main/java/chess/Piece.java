package chess;

public abstract class Piece {
    private int row;
    private int col;
    private String colour;
    private String imageName;
    // mainly for pawn first movement
    private int movesMade;

    public Piece(int row, int col, String colour, String imageName) {
        this.row = row;
        this.col = col;
        this.colour = colour;
        this.imageName = imageName;
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

    public String getImageName() {
        return imageName;
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

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void incrementMovesMade() {
        movesMade++;
    }

}
