package ojt.g1.ui.components;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Text extends Component {

    // shimay
    private int id;

    // Text Stuff
    protected Color textColor = Color.BLACK;
    protected Color color = Color.WHITE;
    protected String text;
    protected Font font = new Font("Arial", Font.PLAIN, 10);
    protected TextAlignment textAlignment = TextAlignment.LEFT;

    public Text(int x, int y, int w, int h) {
        super(x, y, w, h);
        id = ID.generate();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFont(Font font, Color textColor) {
        this.font = font;
        this.textColor = textColor;
    }

    public void setAlignment(TextAlignment textAlignment) {
        this.textAlignment = textAlignment;
    }

    public int getId() {
        return id;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fill(getBounds());

        if (text != null && !text.isEmpty()) {
            g.setColor(textColor);
            g.setFont(font);

            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();

            int textX = 0;
            switch (textAlignment) {
                case LEFT -> textX = x + 10;
                case CENTER -> textX = x + (w - textWidth) / 2;
                case RIGHT -> textX = x + w - textWidth - 10;

            }
            int textY = y + (h + textHeight) / 2 - fm.getDescent();

            g.drawString(text, textX, textY);
        }
    }
}
