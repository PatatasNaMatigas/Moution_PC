package ojt.g1.ui.components;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Button extends Component {

    // shimay
    private ActionType actionType;
    private Runnable onClickEvent;
    private Runnable onMousePressedEvent;
    private Runnable onMouseReleasedEvent;

    // Button Stuff
    protected Color textColor = Color.BLACK;
    protected Color color = Color.WHITE;
    protected String text;
    protected Font font = new Font("Arial", Font.PLAIN, 10);
    protected TextAlignment textAlignment = TextAlignment.LEFT;

    public Button(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    public void setBackground(Color color) {
        this.color = color;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOnClickListener(Runnable onCLickEvent) {
        this.onClickEvent = onCLickEvent;
    }

    public void setOnClickListener(Runnable onCLickEvent, ActionType actionType) {
        if (actionType == ActionType.MOUSE_PRESSED) {
            onMousePressedEvent = onCLickEvent;
        } else {
            onMouseReleasedEvent = onCLickEvent;
        }
    }

    public void performActionOn(ActionType actionType) {
        this.actionType = actionType;
    }

    public void setFont(Font font, Color textColor) {
        this.font = font;
        this.textColor = textColor;
    }

    public void setTextAlignment(TextAlignment textAlignment) {
        this.textAlignment = textAlignment;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!getBounds().contains(e.getX(), e.getY()))
            return;

        if (onClickEvent != null && actionType == ActionType.MOUSE_PRESSED)
            onClickEvent.run();

        if (onMousePressedEvent != null)
            onMousePressedEvent.run();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!getBounds().contains(e.getX(), e.getY()))
            return;

        if (onClickEvent != null && actionType == ActionType.MOUSE_RELEASED)
            onClickEvent.run();

        if (onMouseReleasedEvent != null)
            onMouseReleasedEvent.run();
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
