package chess;

public class ChessGame {
    private ChessBoard board;
    private String currentTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.currentTurn = "white";
    }

    public ChessBoard getBoard() {
        return board;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }

    // check logic
    public boolean isChecked() {
        Piece checkKing = board.getKing(currentTurn);
        if (checkKing == null) {
            return false;
        }
        return board.isAttacked(checkKing.getRow(), checkKing.getCol(), currentTurn);
    }

    public boolean makeMove(Piece piece, int newRow, int newCol) {
        // wrong persons turn
        if (!piece.getColour().equals(currentTurn)) {
            return false;
        }

        // invalid move
        if (!piece.isValidMove(board, newRow, newCol)) {
            return false;
        }

        // king would be in check
        if (wouldCheck(piece, newRow, newCol)) {
            return false;
        }

        // make the move
        boolean moved = board.movePiece(piece, newRow, newCol);

        if (!moved) {
            return false;
        }

        switchTurn();

        if (isChecked()) {
            System.out.println(currentTurn + " is in check");
        }

        return true;
    }

    public void switchTurn() {
        if (currentTurn.equals("white")) {
            currentTurn = "black";
        } else {
            currentTurn = "white";
        }
    }

    public boolean wouldCheck(Piece piece, int newRow, int newCol) {
        int oldRow = piece.getRow();
        int oldCol = piece.getCol();

        Piece capturedPiece = board.makeTemporaryMove(piece, newRow, newCol);

        boolean inCheck = isChecked();

        board.undoMove(piece, oldRow, oldCol, capturedPiece);

        return inCheck;
    }

    public boolean isCheckmated() {
        if (isChecked()) {
            Piece checkedKing = board.getKing(currentTurn);
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (!checkedKing.isValidMove(board, row, col)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isStalemate() {
        if (isChecked()) {
            return false;
        }

        for (Piece piece : board.getPieces(currentTurn)) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (!piece.isValidMove(board, row, col)) {
                        continue;
                    }

                    if (wouldCheck(piece, row, col)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        return true;
    }
}
