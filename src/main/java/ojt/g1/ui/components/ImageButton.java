package ojt.g1.ui.components;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class ImageButton extends Button {

    private BufferedImage image;

    public ImageButton(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void draw(Graphics2D g) {
        Rectangle2D bounds = getBounds();
        g.drawImage(
                image,
                (int) bounds.getX(),
                (int) bounds.getY(),
                (int) bounds.getWidth(),
                (int) bounds.getHeight(),
                null
        );
    }
}
