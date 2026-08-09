package chess;

public class Knight extends Piece {
    public Knight(int row, int col, String colour, String imageName) {
        super(row, col, colour, imageName);
    }

    @Override
    public boolean isValidMove(ChessBoard board, int newRow, int newCol) {
        int rowDiff = Math.abs(newRow - getRow());
        int colDiff = Math.abs(newCol - getCol());

        // can't move onto the same square
        if (newRow == getRow() && newCol == getCol()) {
            return false;
        }

        // can only move in L shape
        if (!((rowDiff == 1 && colDiff == 2) || (rowDiff == 2 && colDiff == 1))) {
            return false;
        }

        // target square
        Piece target = board.getPiece(newRow, newCol);

        // can't capture own piece
        if (target != null && target.getColour().equals(getColour())) {
            return false;
        }

        // valid move
        return true;
    }

    @Override
    public String getImageName() {
        String colour = getColour().equals("white") ? "w" : "b";
        return "Knight-" + colour + ".png";

    }

    // can't have a collision
    @Override
    public boolean isCollision(ChessBoard board, int newRow, int newCol, int rowDiff, int colDiff) {
        return false;
    }
}
