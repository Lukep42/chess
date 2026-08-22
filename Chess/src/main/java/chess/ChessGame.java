package chess;

public class ChessGame {
    private ChessBoard board;
    private String currentTurn;
    private Piece lastMoved;
    private int lastMovedOldRow;
    private int lastMovedOldCol;
    private int lastMovedNewRow;
    private int lastMovedNewCol;

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

    public void reset() {
        board = new ChessBoard();
        currentTurn = "white";
        lastMoved = null;
        lastMovedOldRow = -1;
        lastMovedOldCol = -1;
        lastMovedNewRow = -1;
        lastMovedNewCol = -1;

    }

    public void setCurrentTurn(String currentTurn) {
        this.currentTurn = currentTurn;
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

        if (piece instanceof Pawn && isEnPassant(piece, newRow, newCol)) {
            if (wouldCheck(piece, newRow, newCol)) {
                return false;
            }
            int oldRow = piece.getRow();
            int oldCol = piece.getCol();

            board.setPiece(oldRow, newCol, null);
            boolean moved = board.movePiece(piece, newRow, newCol);
            if (!moved) {
                return false;
            }

            lastMoved = piece;
            lastMovedOldRow = oldRow;
            lastMovedOldCol = oldCol;
            lastMovedNewRow = piece.getRow();
            lastMovedNewCol = piece.getCol();
            switchTurn();
            return true;

        }

        // invalid move
        if (!piece.isValidMove(board, newRow, newCol)) {
            return false;
        }

        if (piece instanceof King && Math.abs(newCol - piece.getCol()) == 2) {
            lastMovedOldRow = piece.getRow();
            lastMovedOldCol = piece.getCol();
            board.castleMove(piece, newRow, newCol);
            lastMoved = piece;
            lastMovedNewRow = piece.getRow();
            lastMovedNewCol = piece.getCol();

            switchTurn();
            return true;
        }

        // king would be in check
        if (wouldCheck(piece, newRow, newCol)) {
            return false;
        }

        lastMovedOldRow = piece.getRow();
        lastMovedOldCol = piece.getCol();
        // make the move
        boolean moved = board.movePiece(piece, newRow, newCol);

        if (!moved) {
            return false;
        }

        lastMoved = piece;
        lastMovedNewRow = piece.getRow();
        lastMovedNewCol = piece.getCol();

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

        // Piece capturedPiece = board.makeTemporaryMove(piece, newRow, newCol);
        Piece capturedPiece = null;
        int capturedRow = -1;
        int capturedCol = -1;

        boolean enPassant = piece instanceof Pawn && isEnPassant(piece, newRow, newCol);
        if (enPassant) {
            capturedRow = oldRow;
            capturedCol = newCol;
            capturedPiece = board.getPiece(capturedRow, capturedCol);

            board.setPiece(capturedRow, capturedCol, null);
        }

        Piece normalCapturedPiece = board.makeTemporaryMove(piece, newRow, newCol);

        boolean inCheck = isChecked();

        board.undoMove(piece, oldRow, oldCol, normalCapturedPiece);

        if (enPassant && capturedPiece != null) {
            board.setPiece(capturedRow, capturedCol, capturedPiece);
        }

        return inCheck;
    }

    public boolean isCheckmated() {
        if (!isChecked()) {
            return false;
        }

        for (Piece piece : board.getPieces(currentTurn)) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    boolean validMove = piece.isValidMove(board, row, col);
                    boolean enPassant = piece instanceof Pawn && isEnPassant(piece, row, col);

                    // if (!piece.isValidMove(board, row, col)) {
                    // continue;
                    // }
                    if (!validMove && !enPassant) {
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

    public boolean isStalemate() {
        if (isChecked()) {
            return false;
        }

        for (Piece piece : board.getPieces(currentTurn)) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    boolean validMove = piece.isValidMove(board, row, col);
                    boolean enPassant = piece instanceof Pawn && isEnPassant(piece, row, col);

                    if (!validMove && !enPassant) {
                        continue;
                    }
                    // if (!piece.isValidMove(board, row, col)) {
                    // continue;
                    // }

                    if (wouldCheck(piece, row, col)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isEnPassant(Piece pawn, int newRow, int newCol) {
        if (!(pawn instanceof Pawn)) {
            return false;
        }
        if (!(lastMoved instanceof Pawn)) {
            return false;
        }

        if (lastMoved.getColour().equals(pawn.getColour())) {
            return false;
        }

        if (board.getPiece(newRow, newCol) != null) {
            return false;
        }

        if (Math.abs(newCol - pawn.getCol()) != 1) {
            return false;
        }
        int direction = pawn.getColour().equals("white") ? -1 : 1;

        if (newRow - pawn.getRow() != direction) {
            return false;
        }
        if (lastMoved.getRow() != pawn.getRow()) {
            return false;
        }

        if (lastMoved.getCol() != newCol) {
            return false;
        }
        if (Math.abs(lastMovedNewRow - lastMovedOldRow) != 2) {
            return false;
        }
        if (lastMovedNewCol != lastMovedOldCol) {
            return false;
        }
        return true;
    }
}
