package chess;

import java.util.HashMap;

// RULES TO DO
// 3 REPITION DRAW -> maybe come back
// window pop pup -> if no capture or pawn in 75 moves -> auto draw pop up
// insufficient material -> if king & king, or king & bishop/knight vs king
// offer draw

public class ChessGame {
    private ChessBoard board;
    private String currentTurn;
    private Piece lastMoved;
    private int lastMovedOldRow;
    private int lastMovedOldCol;
    private int lastMovedNewRow;
    private int lastMovedNewCol;
    private HashMap<String, Integer> positionHistory = new HashMap<>();
    private int moveCounter = 0;
    private boolean whiteFiftyMoveDraw;
    private boolean blackFiftyMoveDraw;

    public ChessGame() {
        this.board = new ChessBoard();
        this.currentTurn = "white";
        positionHistory.put(getPositionKey(), 1);
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
        positionHistory.clear();
        positionHistory.put(getPositionKey(), 1);
        moveCounter = 0;
        whiteFiftyMoveDraw = false;
        blackFiftyMoveDraw = false;
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
            moveCounter = 0;
            whiteFiftyMoveDraw = false;
            blackFiftyMoveDraw = false;

            switchTurn();
            recordPosition();

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
            moveCounter++;

            switchTurn();
            recordPosition();
            return true;
        }

        // king would be in check
        if (wouldCheck(piece, newRow, newCol)) {
            return false;
        }

        lastMovedOldRow = piece.getRow();
        lastMovedOldCol = piece.getCol();
        // make the move
        boolean capture = board.getPiece(newRow, newCol) != null;
        boolean pawnMove = piece instanceof Pawn;
        boolean moved = board.movePiece(piece, newRow, newCol);

        if (!moved) {
            return false;
        }

        if (capture || pawnMove) {
            moveCounter = 0;
            whiteFiftyMoveDraw = false;
            blackFiftyMoveDraw = false;
        } else {
            moveCounter++;
        }

        lastMoved = piece;
        lastMovedNewRow = piece.getRow();
        lastMovedNewCol = piece.getCol();

        switchTurn();
        recordPosition();

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

    private String getPositionKey() {
        StringBuilder key = new StringBuilder();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece == null) {
                    key.append(".");
                } else {
                    key.append(piece.getColour().charAt(0));

                    if (piece instanceof King) {
                        key.append("K");
                    } else if (piece instanceof Queen) {
                        key.append("Q");
                    } else if (piece instanceof Rook) {
                        key.append("R");
                    } else if (piece instanceof Bishop) {
                        key.append("B");
                    } else if (piece instanceof Knight) {
                        key.append("KN");
                    } else {
                        key.append("P");
                    }
                }
            }
        }
        key.append("-").append(currentTurn);
        return key.toString();
    }

    private void recordPosition() {
        String key = getPositionKey();
        positionHistory.put(key, positionHistory.getOrDefault(key, 0) + 1);
    }

    public boolean isThreeRepition() {
        String key = getPositionKey();

        return positionHistory.getOrDefault(key, 0) >= 3;
    }

    public boolean isFiftyMoveDraw() {
        return moveCounter >= 100;
    }

    public boolean offerFiftyMoveDraw() {
        if (!isFiftyMoveDraw()) {
            return false;
        }
        if (currentTurn.equals("white")) {
            return !whiteFiftyMoveDraw;
        }
        return !blackFiftyMoveDraw;
    }

    public void declineFiftyMoveDraw() {
        if (currentTurn.equals("white")) {
            whiteFiftyMoveDraw = true;
        } else {
            blackFiftyMoveDraw = true;
        }
    }

    public boolean isSeventyFiveMoveDraw() {
        return moveCounter >= 2;
    }
}
