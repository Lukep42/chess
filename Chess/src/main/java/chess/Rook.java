package chess;

public class Rook extends Piece {
    public Rook(int row, int col, String colour, String imageName) {
        super(row, col, colour, imageName);
    }

    @Override
    public boolean isValidMove(ChessBoard board, int newRow, int newCol) {

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

        // valid move
        return true;
    }

    @Override
    public String getImageName() {
        String colour = getColour().equals("white") ? "w" : "b";
        return "Rook-" + colour + ".png";

    }

}
