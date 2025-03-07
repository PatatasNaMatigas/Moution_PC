package ojt.g1.ui.core;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import ojt.g1.ui.components.Component;

public class Page {

    private final ArrayList<Component> components = new ArrayList<>();

    private final String name;
    private int id;

    private Window window;

    public Page(String name, Window window) {
        this.name = name;
        this.window = window;
    }

    protected final void setId(int id) {
        this.id = id;
    }

    public final int getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final void addComponent(Component component) {
        components.add(component.create());
        requestRepaint();
    }

    public final void draw(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setColor(Color.WHITE);
        g.fillRect(
                0,
                0,
                (int) window.getParent().getBounds().getWidth(),
                (int) window.getParent().getBounds().getHeight()
        );
        for (Component component : components)
            component.draw(g);
    }

    public final void requestRepaint() {
        window.requestRepaint();
    }

    public final void mouseReleased(MouseEvent e) {
        for (Component component : components)
            component.mouseReleased(e);
    }

    public final void mousePressed(MouseEvent e) {
        for (Component component : components)
            component.mousePressed(e);
    }

    public <T extends Component> ArrayList<T> getComponents(Class<T> cls) {
        ArrayList<T> comps = new ArrayList<>();
        for (Component component : components) {
            if (cls.isInstance(component)) {
                comps.add(cls.cast(component));
            }
        }
        return comps;
    }

    public final Component getParent() {
        return window.getParent();
    }

    public final void create() {
        onCreate();
    }

    public void onCreate() {}
}
