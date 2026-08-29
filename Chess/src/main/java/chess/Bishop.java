package chess;

public class Bishop extends Piece {
    public Bishop(int row, int col, String colour, String imageName) {
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

        // can only move diagonal
        if (rowDiff != colDiff) {
            return false;
        }

        // target square
        Piece target = board.getPiece(newRow, newCol);

        // can't capture own piece
        if (target != null && target.getColour().equals(getColour())) {
            return false;
        }

        // Check if there is a collision
        return isCollision(board, newRow, newCol, rowDiff, colDiff);
    }

    @Override
    public String getImageName() {
        String colour = getColour().equals("white") ? "w" : "b";
        return "Bishop-" + colour + ".png";

    }

    // detect pieces in the way
    @Override
    public boolean isCollision(ChessBoard board, int newRow, int newCol, int rowDiff, int colDiff) {
        if (rowDiff == colDiff) {
            int colDirection = Integer.signum(newCol - getCol());
            int rowDirection = Integer.signum(newRow - getRow());
            for (int i = 1; i < colDiff; i++) {
                int col = getCol() + i * colDirection;
                int row = getRow() + i * rowDirection;

                if (board.getPiece(row, col) != null) {
                    return false;
                }
            }
        }
        return true;
    }
}