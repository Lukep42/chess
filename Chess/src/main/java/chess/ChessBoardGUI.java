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
    private Label resignMessage;
    private Label stateMessage;

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
                    stalemateMessage(stateMessage);
                }

                if (game.isCheckmated()) {
                    checkmateMessage(stateMessage);
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
                movesMade.add(selectedPosition + " -> " + destinationPosition);
                textArea.clear();
                textArea.appendText(String.join("\n", movesMade));

                if (game.isThreeRepition()) {
                    ThreeRepitionMessage(stateMessage);
                }

                if (game.offerFiftyMoveDraw()) {
                    openDrawChoice();
                }

                if (game.isSeventyFiveMoveDraw()) {
                    drawMessage(stateMessage);
                }
            } else {
                System.out.println("Invalid move");
            }
            selectedPiece = null;
            requestLayout();
        }
    }

    public void stalemateMessage(Label stalemateMessage) {
        stalemateMessage = new Label("STALEMATE " + game.getCurrentTurn().toUpperCase() + " LOSES");
        stalemateMessage.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75); " +
                "-fx-text-fill: white; " +
                "-fx-padding: 10px 20px; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5px;");

        stalemateMessage.setLayoutX(0);
        stalemateMessage.setLayoutY(0);
        // Dynamically bind X and Y to the exact center
        stalemateMessage.layoutXProperty()
                .bind(widthProperty().divide(2).subtract(stalemateMessage.widthProperty().divide(2)));
        stalemateMessage.layoutYProperty()
                .bind(heightProperty().divide(2).subtract(stalemateMessage.heightProperty().divide(2)));

        getChildren().add(stalemateMessage);
    }

    public void checkmateMessage(Label checkmateMessage) {
        checkmateMessage = new Label("CHECKMATE " + game.getCurrentTurn().toUpperCase() + " LOSES");
        checkmateMessage.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75); " +
                "-fx-text-fill: white; " +
                "-fx-padding: 10px 20px; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5px;");

        checkmateMessage.setLayoutX(0);
        checkmateMessage.setLayoutY(0);
        // Dynamically bind X and Y to the exact center
        checkmateMessage.layoutXProperty()
                .bind(widthProperty().divide(2).subtract(checkmateMessage.widthProperty().divide(2)));
        checkmateMessage.layoutYProperty()
                .bind(heightProperty().divide(2).subtract(checkmateMessage.heightProperty().divide(2)));

        getChildren().add(checkmateMessage);

    }

    public void drawMessage(Label drawMessage) {
        drawMessage = new Label("DRAW");
        drawMessage.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75); " +
                "-fx-text-fill: white; " +
                "-fx-padding: 10px 20px; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5px;");

        drawMessage.setLayoutX(0);
        drawMessage.setLayoutY(0);
        // Dynamically bind X and Y to the exact center
        drawMessage.layoutXProperty()
                .bind(widthProperty().divide(2).subtract(drawMessage.widthProperty().divide(2)));
        drawMessage.layoutYProperty()
                .bind(heightProperty().divide(2).subtract(drawMessage.heightProperty().divide(2)));

        getChildren().add(drawMessage);

    }

    public void ThreeRepitionMessage(Label ThreeRepitionMessage) {
        ThreeRepitionMessage = new Label("DRAW - THE POSITION HAS BEEN REPEATED 3 TIMES");
        ThreeRepitionMessage.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75); " +
                "-fx-text-fill: white; " +
                "-fx-padding: 10px 20px; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5px;");

        ThreeRepitionMessage.setLayoutX(0);
        ThreeRepitionMessage.setLayoutY(0);
        // Dynamically bind X and Y to the exact center
        ThreeRepitionMessage.layoutXProperty()
                .bind(widthProperty().divide(2).subtract(ThreeRepitionMessage.widthProperty().divide(2)));
        ThreeRepitionMessage.layoutYProperty()
                .bind(heightProperty().divide(2).subtract(ThreeRepitionMessage.heightProperty().divide(2)));

        getChildren().add(ThreeRepitionMessage);

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

    public void clearStateMessage() {
        if (stateMessage != null) {
            getChildren().remove(stateMessage);
            stateMessage = null;
        }
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

        Button acceptButton = new Button();
        acceptButton.setText("YES");
        Button declineButton = new Button();
        declineButton.setText("NO");

        acceptButton.setOnAction(e -> {
            newWindow.close();
            drawMessage(stateMessage);
        });
        declineButton.setOnAction(e -> {
            game.declineFiftyMoveDraw();
            newWindow.close();
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

}
