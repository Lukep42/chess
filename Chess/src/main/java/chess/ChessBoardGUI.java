package chess;

import javafx.scene.canvas.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.*;

public class ChessBoardGUI extends Pane {
    private double gridWidth;
    private double gridHeight;
    private double gridSquareSize;
    private List<PieceIcon> icons = new ArrayList<>();
    private Canvas canvas;
    private PieceIcon selectedPiece = null;
    private ChessBoard board;

    public ChessBoardGUI(double gridWidth, double gridHeight, ChessBoard board) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.board = board;
    }

    // Retrieves a modifiable list of ChessBoardIcons. Used to add new icons
    public List<PieceIcon> getIcons() {
        return icons;
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
                System.out.println("Piece selected at X: " + X + " and Y: " + Y);
            }
        } else {
            // selectedPiece.setPosition(X, Y);
            Piece piece = selectedPiece.getPiece();
            PieceIcon capturedPiece = clickedIcon;
            boolean moved = board.movePiece(piece, Y, X);

            if (moved) {
                if (capturedPiece != null && capturedPiece != selectedPiece) {
                    capturedPiece.setShown(false);
                }
                selectedPiece.setPosition(X, Y);
                System.out.println("Piece moved to X: " + X + " and Y: " + Y);
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
}
