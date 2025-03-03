package ojt.g1.ui.core;

import ojt.g1.ui.components.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Mouse extends MouseAdapter {

    Window window;

    public Mouse(Window window) {
        this.window = window;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        window.mousePressed(e);
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        window.mouseReleased(e);
    }
}

public final class Window {

    private JPanel canvas;
    private JFrame window;
    private final PageHandler pageHandler;

    private final int width;
    private final int height;

    private final Component parent;

    public Window(PageHandler pageHandler, int width, int height) {
        this.pageHandler = pageHandler;
        this.width = width;
        this.height = height;
        window = new JFrame();
        parent = new Component(0, Toolkit.getDefaultToolkit().getScreenInsets(window.getGraphicsConfiguration()).top, width, height);
        System.out.println(parent.getBounds());

        canvas = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                pageHandler.getCurrentPage().draw(g2);
                g2.dispose();
            }
        };
        canvas.setFocusable(true);
        canvas.requestFocusInWindow();
        canvas.requestFocus();
        canvas.addMouseListener(new Mouse(this));
    }

    public void create() {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.setPreferredSize(new java.awt.Dimension(width, height));
        window.add(canvas);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);
    }

    public void setWindowParams(WindowParams windowParams) {
        window.setIconImage(windowParams.getImageIcon());
        window.setTitle(windowParams.getTitle());
    }

    public void requestRepaint() {
        canvas.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        pageHandler.getCurrentPage().mouseReleased(e);
    }

    public void mousePressed(MouseEvent e) {
        pageHandler.getCurrentPage().mousePressed(e);
    }

    public Component getParent() {
        return parent;
    }
}
