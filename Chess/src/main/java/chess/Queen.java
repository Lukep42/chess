package chess;

public class Queen extends Piece {
    public Queen(int row, int col, String colour, String imageName) {
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

        boolean isDiagonal = rowDiff == colDiff;
        boolean isStraight = newRow == getRow() || newCol == getCol();

        // combine bishop and rook movement logic
        if (!isDiagonal && !isStraight) {
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
        return "Queen-" + colour + ".png";

    }

    // combine bishop + rook
    @Override
    public boolean isCollision(ChessBoard board, int newRow, int newCol, int rowDiff, int colDiff) {
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
