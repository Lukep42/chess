package chess;

public class Rook extends Piece {
    public Rook(int row, int col, String colour, String imageName) {
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

        // can only move on the same row or col
        if (newRow != getRow() && newCol != getCol()) {
            return false;
        }

        // target square
        Piece target = board.getPiece(newRow, newCol);

        // can't capture own piece
        if (target != null && target.getColour().equals(getColour())) {
            return false;
        }

        // check for collision
        return isCollision(board, newRow, newCol, rowDiff, colDiff);
    }

    @Override
    public String getImageName() {
        String colour = getColour().equals("white") ? "w" : "b";
        return "Rook-" + colour + ".png";

    }

    @Override
    public boolean isCollision(ChessBoard board, int newRow, int newCol, int rowDiff, int colDiff) {
        // Detect pieces in the way
        // if moving horizontially
        if (getRow() == newRow) {
            int direction = Integer.signum(newCol - getCol());
            for (int i = 1; i < colDiff; i++) {
                int col = getCol() + i * direction;
                if (board.getPiece(getRow(), col) != null) {
                    return false;
                }
            }
        }

        // if moving vertically
        if (getCol() == newCol) {
            int direction = Integer.signum(newRow - getRow());
            for (int i = 1; i < rowDiff; i++) {
                int row = getRow() + i * direction;
                if (board.getPiece(row, getCol()) != null) {
                    return false;
                }
            }
        }
        return true;
    }

}
