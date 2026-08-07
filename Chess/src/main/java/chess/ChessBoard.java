package chess;

import javafx.geometry.VPos;
import javafx.scene.canvas.*;
import javafx.scene.transform.Affine;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import java.util.*;

public class ChessBoard extends Pane {
    private double gridWidth;
    private double gridHeight;
    private double gridSquareSize;
    private List<PieceIcon> icons = new ArrayList<>();
    private Canvas canvas;

    public ChessBoard(double gridWidth, double gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
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
}
