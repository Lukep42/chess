package chess;

public class Pawn extends Piece {
    public Pawn(int row, int col, String colour, String imageName) {
        super(row, col, colour, imageName);
    }

    // COME BACK FOR EN-PASSANT
    @Override
    public boolean isValidMove(ChessBoard board, int newRow, int newCol) {
        int rowDiff = newRow - getRow();
        int colDiff = Math.abs(newCol - getCol());
        int direction = getColour().equals("white") ? -1 : 1;

        // can't move onto the same square
        if (newRow == getRow() && newCol == getCol()) {
            return false;
        }
        // target square
        Piece target = board.getPiece(newRow, newCol);

        // // can only move one
        // if (rowDiff != direction || colDiff != 0) {
        // // can't go forward if blocked
        // if (target != null) {
        // return false;
        // }
        // }

        // can only move one
        if (rowDiff == direction && colDiff == 0) {
            return target == null;
        }

        // can move forward 2 on the first move
        if (rowDiff == 2 * direction && colDiff == 0) {
            if (getMovesMade() != 0) {
                return false;
            }

            if (target != null) {
                return false;
            }

            return isCollision(board, newRow, newCol, rowDiff, colDiff);
        }

        // capture logic
        if (rowDiff == direction && colDiff == 1) {
            return target != null && !target.getColour().equals(getColour());
        }

        // // can't capture own piece
        // if (target != null && target.getColour().equals(getColour())) {
        // return false;
        // }

        // valid move
        // return isCollision(board, newRow, newCol, rowDiff, colDiff);
        return false;
    }

    @Override
    public String getImageName() {
        String colour = getColour().equals("white") ? "w" : "b";
        return "Pawn-" + colour + ".png";

    }

    // can't have a collision
    @Override
    public boolean isCollision(ChessBoard board, int newRow, int newCol, int rowDiff, int colDiff) {

        // if moving vertically
        if (getCol() == newCol) {
            int direction = getColour().equals("white") ? -1 : 1;
            for (int i = 1; i < Math.abs(rowDiff); i++) {
                int row = getRow() + i * direction;
                if (board.getPiece(row, getCol()) != null) {
                    return false;
                }
            }
        }
        return true;
    }
}
