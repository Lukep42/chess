package chess;

public class Pawn extends Piece {
    public Pawn(int row, int col, String colour, String imageName) {
        super(row, col, colour, imageName);
    }

    // COME BACK FOR CAPTURING & EN-PASSANT
    @Override
    public boolean isValidMove(ChessBoard board, int newRow, int newCol) {
        int rowDiff = newRow - getRow();
        int colDiff = Math.abs(newCol - getCol());

        // can't move onto the same square
        if (newRow == getRow() && newCol == getCol()) {
            return false;
        }
        // can move forward 2 on the first move
        if (getMovesMade() == 0 && rowDiff == 2) {
            return true;
        }

        // can only move one
        if (rowDiff != 1 && colDiff != 0) {
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
        return "Pawn-" + colour + ".png";

    }

    // can't have a collision
    @Override
    public boolean isCollision(ChessBoard board, int newRow, int newCol, int rowDiff, int colDiff) {
        return false;
    }

}
