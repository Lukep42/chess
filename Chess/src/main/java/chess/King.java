package chess;

public class King extends Piece {
    private boolean inCheck;

    public King(int row, int col, String colour, String imageName, boolean inCheck) {
        super(row, col, colour, imageName);
        this.inCheck = inCheck;
    }

    @Override
    // COME BACK FOR CASTLING
    public boolean isValidMove(ChessBoard board, int newRow, int newCol) {
        // can't move onto the same square
        if (newRow == getRow() && newCol == getCol()) {
            return false;
        }

        // Can't move more than one square
        if (Math.abs(newRow - getRow()) > 1 && Math.abs(newCol - getCol()) > 1) {
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
        return "King-" + colour + ".png";

    }

}
