package chess;

import java.io.InputStream;
import javafx.scene.image.Image;

// Represents an image to be displayed in a GridArea pane. If you change any of
// the properties, be sure to call 'requestLayout()' on the GridArea after you're done.

public class PieceIcon {
    private double x;
    private double y;
    private Image image;
    private boolean shown = true;

    public PieceIcon(double x, double y, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public PieceIcon(double x, double y, InputStream imageStream) {
        this(x, y, new Image(imageStream));
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
}
