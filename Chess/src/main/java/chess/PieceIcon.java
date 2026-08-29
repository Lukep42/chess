package chess;

import java.io.InputStream;
import javafx.scene.image.Image;

// Represents an image to be displayed in a GridArea pane.

public class PieceIcon {
    private double x;
    private double y;
    private Image image;
    private boolean shown = true;
    private Piece piece;

    public PieceIcon(double x, double y, Image image, Piece piece) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.piece = piece;
    }

    public PieceIcon(double x, double y, InputStream imageStream, Piece piece) {
        this(x, y, new Image(imageStream), piece);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public Piece getPiece() {
        return piece;
    }

    public boolean isShown() {
        return shown;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public void setPeiece(Piece piece) {
        this.piece = piece;
    }
}
