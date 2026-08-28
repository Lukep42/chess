package chess;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class ChessBoardGUI extends Pane {
    private double gridWidth;
    private double gridHeight;
    private double gridSquareSize;
    private List<PieceIcon> icons = new ArrayList<>();
    private Canvas canvas;
    private PieceIcon selectedPiece = null;
    // private ChessBoard board;
    private List<String> movesMade = new ArrayList<>();
    private TextArea textArea;
    private String selectedPosition;
    private ChessGame game;
    // private Label resignMessage;
    // private Label stateMessage;

    // public ChessBoardGUI(double gridWidth, double gridHeight, ChessBoard board,
    // TextArea textArea) {
    // this.gridWidth = gridWidth;
    // this.gridHeight = gridHeight;
    // this.board = board;
    // this.textArea = textArea;
    // }

    public ChessBoardGUI(double gridWidth, double gridHeight, ChessGame game, TextArea textArea) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.game = game;
        this.textArea = textArea;
    }

    // Retrieves a modifiable list of ChessBoardIcons. Used to add new icons
    public List<PieceIcon> getIcons() {
        return icons;
    }

    public List<String> getMoves() {
        return movesMade;
    }

    // Redraws the grid area, either because the user is manipulating the window, OR
    // requestLayout() has been called .
    @Override
    public void layoutChildren() {
        super.layoutChildren();
        if (canvas == null) {
            canvas = new Canvas();
            canvas.widthProperty().bind(widthProperty());
            canvas.heightProperty().bind(heightProperty());
            getChildren().add(canvas);

            canvas.setOnMouseClicked(event -> {
                squareClicked(event.getX(), event.getY());
            });
        }

        GraphicsContext gfx = canvas.getGraphicsContext2D();
        gfx.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());

        // Calculate square size so the board always fits in the availale space
        gridSquareSize = Math.min(getWidth() / gridWidth,
                getHeight() / gridHeight);

        // Create checkerboard pattern
        for (int row = 0; row < gridHeight; row++) {
            for (int col = 0; col < gridWidth; col++) {
                if ((row + col) % 2 == 0) {
                    // gfx.setFill(Color.WHITE);
                    gfx.setFill(Color.rgb(234, 206, 153));

                    gfx.fillRect(
                            col * gridSquareSize,
                            row * gridSquareSize,
                            gridSquareSize,
                            gridSquareSize);
                } else {
                    // gfx.setFill(Color.GRAY);
                    gfx.setFill(Color.rgb(125, 74, 11));

                    gfx.fillRect(
                            col * gridSquareSize,
                            row * gridSquareSize,
                            gridSquareSize,
                            gridSquareSize);
                }
            }
        }

        // highlight last move made
        if (game.getLastMoved() != null) {
            Piece piece = game.getLastMoved();
            gfx.setFill(Color.rgb(255, 215, 0, 0.40));
            gfx.fillRect(
                    game.getLastMovedOldCol() * gridSquareSize,
                    game.getLastMovedOldRow() * gridSquareSize,
                    gridSquareSize,
                    gridSquareSize);

            gfx.setFill(Color.rgb(255, 215, 0, 0.5));
            gfx.fillRect(
                    piece.getCol() * gridSquareSize,
                    piece.getRow() * gridSquareSize,
                    gridSquareSize,
                    gridSquareSize);
        }

        if (game.isChecked()) {
            String currentTurn = game.getCurrentTurn();
            for (PieceIcon icon : icons) {
                Piece piece = icon.getPiece();

                if (icon.isShown() && piece instanceof King && piece.getColour().equals(currentTurn)) {
                    gfx.setFill(Color.rgb(255, 0, 0, 0.5));

                    gfx.fillRect(icon.getX() * gridSquareSize, icon.getY() * gridSquareSize, gridSquareSize,
                            gridSquareSize);

                    break;
                }
            }

        }

        if (selectedPiece != null) {
            Piece piece = selectedPiece.getPiece();
            ChessBoard board = game.getBoard();

            for (int row = 0; row < gridHeight; row++) {
                for (int col = 0; col < gridWidth; col++) {
                    boolean normalMove = piece.isValidMove(board, row, col);

                    boolean enPassantMove = piece instanceof Pawn && game.isEnPassant(piece, row, col);

                    if ((normalMove || enPassantMove) && !game.wouldCheck(piece, row, col)) {
                        double centerX = (col + 0.5) * gridSquareSize;
                        double centerY = (row + 0.5) * gridSquareSize;

                        Piece targetPiece = board.getPiece(row, col);
                        if (targetPiece != null && !targetPiece.getColour().equals(piece.getColour())) {
                            gfx.setStroke(Color.rgb(255, 0, 0, 0.5));
                            gfx.setLineWidth(gridSquareSize * 0.08);

                            double radius = gridSquareSize * 0.35;

                            gfx.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
                        } else if (enPassantMove) {
                            gfx.setStroke(Color.rgb(255, 0, 0, 0.5));
                            gfx.setLineWidth(gridSquareSize * 0.08);
                            double radius = gridSquareSize * 0.35;
                            gfx.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
                        } else {
                            gfx.setFill(Color.rgb(0, 0, 0, 0.5));

                            double radius = gridSquareSize / 7;

                            gfx.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
                        }
                    }

                }
            }
        }

        drawCoordinates(gfx);

        // Draw all the images.
        for (var icon : icons) {
            if (icon.isShown()) {
                drawIcon(gfx, icon);
            }
        }
    }

    // Draw an Icon
    private void drawIcon(GraphicsContext gfx, PieceIcon icon) {
        // Get the pixel coordinates representing the centre of where the image is to be
        // drawn.
        double x = (icon.getX() + 0.5) * gridSquareSize;
        double y = (icon.getY() + 0.5) * gridSquareSize;

        if (icon == selectedPiece) {
            gfx.setFill(Color.rgb(255, 215, 0, 0.45));
            gfx.fillRect(icon.getX() * gridSquareSize, icon.getY() * gridSquareSize, gridSquareSize, gridSquareSize);

        }

        // Keep image's aspect ratio while fitting inside a square
        var image = icon.getImage();
        double fullSizePixelWidth = image.getWidth();
        double fullSizePixelHeight = image.getHeight();

        double displayedPixelWidth, displayedPixelHeight;
        if (fullSizePixelWidth > fullSizePixelHeight) {
            displayedPixelWidth = gridSquareSize;
            displayedPixelHeight = gridSquareSize * fullSizePixelHeight / fullSizePixelWidth;
        } else {
            displayedPixelHeight = gridSquareSize;
            displayedPixelWidth = gridSquareSize * fullSizePixelWidth / fullSizePixelHeight;
        }

        // draw image
        gfx.save();
        gfx.translate(x, y);
        gfx.drawImage(
                image,
                -displayedPixelWidth / 2.0,
                -displayedPixelHeight / 2.0,
                displayedPixelWidth,
                displayedPixelHeight);
        gfx.restore();
    }

    private void drawCoordinates(GraphicsContext gfx) {
        String[] columns = { "a", "b", "c", "d", "e", "f", "g", "h" };

        // gfx.setFont(javafx.scene.text.Font.font());
        gfx.setFont(Font.font("Arial", FontWeight.BOLD, 0.2 * gridSquareSize));
        gfx.setFill(Color.BLACK);

        for (int row = 0; row < 8; row++) {
            double x = 5;
            double y = (row + 0.2) * gridSquareSize;

            gfx.fillText(String.valueOf(8 - row), x, y);

        }

        for (int col = 0; col < 8; col++) {
            double x = (col + .9) * gridSquareSize;
            double y = 8 * gridSquareSize - 5;

            gfx.fillText(columns[col], x - 5, y);
        }

    }

    private void squareClicked(double clickedX, double clickedY) {
        int X = (int) (clickedX / gridSquareSize);
        int Y = (int) (clickedY / gridSquareSize);
        String[] columns = { "a", "b", "c", "d", "e", "f", "g", "h" };
        String destinationPosition = "";

        PieceIcon clickedIcon = null;

        for (PieceIcon icon : icons) {
            if (icon.isShown() && icon.getX() == X && icon.getY() == Y) {
                clickedIcon = icon;
                break;
            }
        }
        if (selectedPiece == null) {
            if (clickedIcon != null) {
                selectedPiece = clickedIcon;
                selectedPosition = columns[X] + ((int) gridHeight - Y);
                System.out.println("Piece selected at X: " + X + " and Y: " + Y);
                requestLayout();
            }
        } else {
            if (clickedIcon != null && clickedIcon != selectedPiece) {
                Piece newPiece = clickedIcon.getPiece();
                if (newPiece.getColour() == game.getCurrentTurn()) {
                    selectedPiece = clickedIcon;
                    selectedPosition = columns[X] + ((int) gridHeight - Y);
                    System.out.println("Piece selection changed to: " + selectedPosition);

                    requestLayout();
                    return;
                }
            }
            // selectedPiece.setPosition(X, Y);
            Piece piece = selectedPiece.getPiece();
            int oldX = (int) selectedPiece.getX();
            int oldY = (int) selectedPiece.getY();
            PieceIcon capturedPiece = clickedIcon;
            PieceIcon enPassantCapture = null;

            if (piece instanceof Pawn && game.isEnPassant(piece, Y, X)) {
                int capturedX = X;
                int capturedY = oldY;

                for (PieceIcon icon : icons) {
                    if (icon.isShown() && icon.getX() == capturedX && icon.getY() == capturedY
                            && icon.getPiece() instanceof Pawn
                            && !icon.getPiece().getColour().equals(piece.getColour())) {
                        enPassantCapture = icon;
                        break;
                    }
                }
            }

            boolean moved = game.makeMove(piece, Y, X);

            if (moved) {

                if (game.isStalemate()) {
                    stalemateMessage();
                    game.setGameOver(true);
                }

                if (game.isCheckmated()) {
                    checkmateMessage();
                    game.setGameOver(true);
                }

                if (capturedPiece != null && capturedPiece != selectedPiece) {
                    capturedPiece.setShown(false);
                }

                if (enPassantCapture != null) {
                    enPassantCapture.setShown(false);
                }

                selectedPiece.setPosition(X, Y);

                if (piece instanceof King && Math.abs(X - oldX) == 2) {
                    updateCastlingIcon(piece, oldX, oldY, X);
                }

                if (piece instanceof Pawn && (Y == 7 || Y == 0)) {
                    openPromotionChoice(piece);
                }

                destinationPosition = columns[X] + ((int) gridHeight - Y);
                movesMade.add(selectedPosition + " \t " + destinationPosition);
                textArea.clear();
                for (int i = 0; i < movesMade.size(); i++) {
                    // textArea.appendText(String.join("\n", movesMade));
                    textArea.appendText((i + 1) + ". " + movesMade.get(i) + "\n");
                }

                if (game.isThreeRepetition()) {
                    ThreeRepetitionMessage();
                    game.setGameOver(true);

                }

                if (game.offerFiftyMoveDraw()) {
                    openDrawChoice();
                }

                if (game.isSeventyFiveMoveDraw()) {
                    drawMessage();
                    game.setGameOver(true);

                }
                if (game.isInsufficientMaterial()) {
                    drawMessage();
                    game.setGameOver(true);
                }
            } else {
                System.out.println("Invalid move");
            }
            selectedPiece = null;
            requestLayout();
        }
    }

    public void stalemateMessage() {
        gameOverLayout("STALEMATE", "DRAW - STALEMATE");

    }

    public void checkmateMessage() {
        gameOverLayout("CHECKMATE", "CHECKMATE, " + game.getCurrentTurn().toUpperCase() + " LOSES");
    }

    public void drawMessage() {
        gameOverLayout("DRAW", "DRAW BY AGREEMENT");

    }

    public void ThreeRepetitionMessage() {
        gameOverLayout("DRAW", "DRAW - POSITION HAS BEEN REPEATED 3 TIMES");
    }

    public void resigned() {
        gameOverLayout("RESIGNATION", game.getCurrentTurn().toUpperCase() + " RESIGNS");
    }

    // public void clearResignedMessage() {
    // if (resignMessage != null) {
    // getChildren().remove(resignMessage);
    // resignMessage = null;
    // }
    // }

    // public void clearStateMessage() {
    // if (stateMessage != null) {
    // getChildren().remove(stateMessage);
    // stateMessage = null;
    // }
    // }

    public void clearMovesMade() {
        movesMade.clear();
    }

    private void updateCastlingIcon(Piece king, int oldX, int oldY, int newX) {
        boolean kingSide = newX > oldX;
        int rookOldX = kingSide ? 7 : 0;
        int rookNewX = kingSide ? 5 : 3;

        for (PieceIcon icon : icons) {
            if (icon.isShown() && icon.getX() == rookOldX && icon.getY() == oldY && icon.getPiece() instanceof Rook
                    && icon.getPiece().getColour().equals(king.getColour())) {
                icon.setPosition(rookNewX, oldY);
                return;
            }
        }
    }

    public void openPromotionChoice(Piece pawn) {
        int row = pawn.getRow();
        int col = pawn.getCol();
        StackPane choice = new StackPane();
        HBox buttonLayout = new HBox();
        Stage newWindow = new Stage();
        buttonLayout.setSpacing(10);
        buttonLayout.setAlignment(Pos.CENTER);

        String colour = "white".equals(pawn.getColour()) ? "white" : "black";
        String colourShortened = "white".equals(pawn.getColour()) ? "-w" : "-b";

        Button queenButton = new Button();
        queenButton.setGraphic(getPromotionIcon("Queen" + colourShortened + ".png"));
        Button rookButton = new Button();
        rookButton.setGraphic(getPromotionIcon("Rook" + colourShortened + ".png"));
        Button knightButton = new Button();
        knightButton.setGraphic(getPromotionIcon("Knight" + colourShortened + ".png"));
        Button bishopButton = new Button();
        bishopButton.setGraphic(getPromotionIcon("Bishop" + colourShortened + ".png"));

        queenButton.setOnAction(e -> {
            promotePawn(pawn, new Queen(row, col, colour, null), newWindow);
        });
        rookButton.setOnAction(e -> {
            promotePawn(pawn, new Rook(row, col, colour, null), newWindow);

        });
        knightButton.setOnAction(e -> {
            promotePawn(pawn, new Knight(row, col, colour, null), newWindow);

        });
        bishopButton.setOnAction(e -> {
            promotePawn(pawn, new Bishop(row, col, colour, null), newWindow);

        });

        buttonLayout.getChildren().addAll(queenButton, rookButton, knightButton, bishopButton);

        choice.getChildren().add(buttonLayout);

        Scene scene = new Scene(choice, 300, 200);

        newWindow.setTitle("Pawn promotion");
        newWindow.setScene(scene);

        newWindow.show();
    }

    private void addPieceIcon(ChessBoardGUI board, Piece piece) {

        var image = Chess.class.getClassLoader().getResourceAsStream(piece.getImageName());

        if (image != null) {
            board.getIcons().add(new PieceIcon(
                    piece.getCol(),
                    piece.getRow(),
                    image,
                    piece));
        } else {
            System.out.println("Could not load image: " + piece.getImageName());
        }

    }

    private ImageView getPromotionIcon(String imageName) {
        var image = Chess.class.getClassLoader().getResourceAsStream(imageName);

        if (image == null) {
            System.out.println("could not load image: " + imageName);
            return null;
        }

        ImageView imageView = new ImageView(new Image(image));
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);

        return imageView;
    }

    private void promotePawn(Piece pawn, Piece newPiece, Stage window) {
        ChessBoard board = game.getBoard();
        board.setPiece(pawn.getRow(), pawn.getCol(), newPiece);
        getIcons().removeIf(icon -> icon.getPiece() == pawn);
        addPieceIcon(this, newPiece);
        requestLayout();
        window.close();
    }

    public void openDrawChoice() {
        StackPane choice = new StackPane();
        VBox windowLayout = new VBox();
        HBox buttonLayout = new HBox();
        Stage newWindow = new Stage();
        Label description = new Label("Will you accept the draw?");
        buttonLayout.setSpacing(10);
        buttonLayout.setAlignment(Pos.CENTER);
        windowLayout.setAlignment(Pos.CENTER);
        windowLayout.setSpacing(10);
        description.setStyle("-fx-font-weight:bold; -fx-font-size: 24px ");

        Button acceptButton = new Button();
        acceptButton.setText("YES");
        Button declineButton = new Button();
        declineButton.setText("NO");

        acceptButton.setOnAction(e -> {
            newWindow.close();
            drawMessage();
            game.setGameOver(true);
        });
        declineButton.setOnAction(e -> {
            game.declineFiftyMoveDraw();
            newWindow.close();
            game.setGameOver(true);
        });

        buttonLayout.getChildren().addAll(acceptButton, declineButton);
        windowLayout.getChildren().addAll(description, buttonLayout);

        // choice.getChildren().add(buttonLayout);
        choice.getChildren().add(windowLayout);

        Scene scene = new Scene(choice, 300, 200);

        newWindow.setTitle("Accept Draw");
        newWindow.setScene(scene);

        newWindow.show();
    }

    public void rematch() {
        game.reset();
        getIcons().clear();
        ChessBoard newChessBoard = game.getBoard();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = newChessBoard.getPiece(row, col);

                if (piece != null) {
                    addPieceIcon(this, piece);
                }

            }

        }
        // clearResignedMessage();
        // clearStateMessage();
        requestLayout();
        game.setCurrentTurn("white");
        clearMovesMade();
        textArea.clear();

    }

    public void gameOverLayout(String title, String inDescription) {
        StackPane choice = new StackPane();
        VBox windowLayout = new VBox();
        HBox buttonLayout = new HBox();
        Stage newWindow = new Stage();
        Label description = new Label(inDescription);
        buttonLayout.setSpacing(10);
        buttonLayout.setAlignment(Pos.CENTER);
        windowLayout.setAlignment(Pos.CENTER);
        windowLayout.setSpacing(10);
        description.setStyle("-fx-font-weight:bold; -fx-font-size: 24px ");

        Button rematchButton = new Button();
        rematchButton.setText("Rematch");
        rematchButton.setOnAction(e -> {
            newWindow.close();
            rematch();
            game.setGameOver(false);
            newWindow.close();
        });

        buttonLayout.getChildren().addAll(rematchButton);
        windowLayout.getChildren().addAll(description, buttonLayout);

        choice.getChildren().add(windowLayout);

        Scene scene = new Scene(choice, 300, 200);

        newWindow.setTitle(title);
        newWindow.setScene(scene);

        newWindow.show();
    }

}
