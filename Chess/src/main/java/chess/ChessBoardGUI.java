package chess;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.canvas.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
    private Label resignMessage;

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
                    gfx.setFill(Color.WHITE);

                    gfx.fillRect(
                            col * gridSquareSize,
                            row * gridSquareSize,
                            gridSquareSize,
                            gridSquareSize);
                } else {
                    gfx.setFill(Color.GRAY);

                    gfx.fillRect(
                            col * gridSquareSize,
                            row * gridSquareSize,
                            gridSquareSize,
                            gridSquareSize);
                }
            }
        }

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
            PieceIcon capturedPiece = clickedIcon;
            boolean moved = game.makeMove(piece, Y, X);

            // Displays a message saying that they are in check.
            if (game.isChecked()) {
                Label checkMessage = new Label(game.getCurrentTurn() + " is in check");
                checkMessage.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5px;");

                checkMessage.setLayoutX(0);
                checkMessage.setLayoutY(0);
                // Dynamically bind X and Y to the exact center
                checkMessage.layoutXProperty()
                        .bind(widthProperty().divide(2).subtract(checkMessage.widthProperty().divide(2)));
                checkMessage.layoutYProperty()
                        .bind(heightProperty().divide(2).subtract(checkMessage.heightProperty().divide(2)));

                getChildren().add(checkMessage);

                PauseTransition pause = new PauseTransition(Duration.seconds(2));

                FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), checkMessage);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);

                SequentialTransition sequence = new SequentialTransition(pause, fadeOut);
                sequence.setOnFinished(e -> getChildren().remove(checkMessage));
                sequence.play();
            }

            if (game.isStalemate()) {
                Label checkMessage = new Label("STALEMATE " + game.getCurrentTurn().toUpperCase() + " LOSES");
                checkMessage.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5px;");

                checkMessage.setLayoutX(0);
                checkMessage.setLayoutY(0);
                // Dynamically bind X and Y to the exact center
                checkMessage.layoutXProperty()
                        .bind(widthProperty().divide(2).subtract(checkMessage.widthProperty().divide(2)));
                checkMessage.layoutYProperty()
                        .bind(heightProperty().divide(2).subtract(checkMessage.heightProperty().divide(2)));

                getChildren().add(checkMessage);
            }

            if (game.isCheckmated()) {
                Label checkMessage = new Label("CHECKMATE " + game.getCurrentTurn().toUpperCase() + " LOSES");
                checkMessage.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5px;");

                checkMessage.setLayoutX(0);
                checkMessage.setLayoutY(0);
                // Dynamically bind X and Y to the exact center
                checkMessage.layoutXProperty()
                        .bind(widthProperty().divide(2).subtract(checkMessage.widthProperty().divide(2)));
                checkMessage.layoutYProperty()
                        .bind(heightProperty().divide(2).subtract(checkMessage.heightProperty().divide(2)));

                getChildren().add(checkMessage);
            }

            if (moved) {
                if (capturedPiece != null && capturedPiece != selectedPiece) {
                    capturedPiece.setShown(false);
                }
                selectedPiece.setPosition(X, Y);
                destinationPosition = columns[X] + ((int) gridHeight - Y);
                // System.out.println("Piece moved to X: " + X + " and Y: " + Y);
                movesMade.add(selectedPosition + " -> " + destinationPosition);
                textArea.clear();
                textArea.appendText(String.join("\n", movesMade));
            } else {
                System.out.println("Invalid move");
            }

            // board.movePiece(selectedPiece.getPiece(), Y, X);
            // selectedPiece.setPosition(X, Y);
            // System.out.println("Piece moved to X: " + X + " and Y: " + Y);
            selectedPiece = null;
            requestLayout();
        }
    }

    public void resigned() {
        resignMessage = new Label("GAME OVER  " + game.getCurrentTurn().toUpperCase() + " RESIGNS");
        resignMessage.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75); " +
                "-fx-text-fill: white; " +
                "-fx-padding: 10px 20px; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5px;");

        resignMessage.setLayoutX(0);
        resignMessage.setLayoutY(0);
        // Dynamically bind X and Y to the exact center
        resignMessage.layoutXProperty()
                .bind(widthProperty().divide(2).subtract(resignMessage.widthProperty().divide(2)));
        resignMessage.layoutYProperty()
                .bind(heightProperty().divide(2).subtract(resignMessage.heightProperty().divide(2)));

        getChildren().add(resignMessage);
    }

    public void clearResignedMessage() {
        if (resignMessage != null) {
            getChildren().remove(resignMessage);
            resignMessage = null;
        }
    }
}
