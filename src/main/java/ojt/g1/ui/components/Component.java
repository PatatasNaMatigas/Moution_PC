package ojt.g1.ui.components;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Component {

    private ArrayList<Constraint> constraints = new ArrayList<>();
    private ArrayList<Margin> margins = new ArrayList<>();
    private Rectangle2D bounds;

    private int id;

    protected int x;
    protected int y;
    protected int w;
    protected int h;

    protected int ms;
    protected int me;
    protected int mt;
    protected int mb;

    public Component(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        id = ID.generate();
    }

    public void setLayoutConstraints(ConstraintType constraintType, Component component) {
        constraints.add(new Constraint(constraintType, component));
    }

    public final void setMargin(MarginType marginType, int size) {
        margins.add(new Margin(marginType, size));
    }

    public final Component create() {
        boolean hasStart = false;
        boolean hasEnd = false;
        int startX = 0;
        int endX = 0;
        Constraint start = null;
        Constraint end = null;
        ConstraintType startConstraint = null;
        ConstraintType endConstraint = null;

        boolean hasTop = false;
        boolean hasBottom = false;
        int topY = 0;
        int bottomY = 0;
        Constraint top = null;
        Constraint bottom = null;
        ConstraintType topConstraint = null;
        ConstraintType bottomConstraint = null;

        for (Constraint constraint : constraints) {
            ConstraintType constraintType = constraint.getConstraintType();
            Component component = constraint.getComponent();

            switch (constraintType) {
                case START_TO_START:
                    startX = component.x;
                    startConstraint = ConstraintType.START;
                    start = constraint;
                    hasStart = true;
                    break;
                case START_TO_END:
                    startX = component.x + component.w;
                    startConstraint = ConstraintType.END;
                    start = constraint;
                    hasStart = true;
                    break;
                case END_TO_START:
                    endX = component.x - this.w;
                    endConstraint = ConstraintType.START;
                    end = constraint;
                    hasEnd = true;
                    break;
                case END_TO_END:
                    endX = component.x + component.w - this.w;
                    endConstraint = ConstraintType.END;
                    end = constraint;
                    hasEnd = true;
                    break;
                case TOP_TO_TOP:
                    topY = component.y;
                    topConstraint = ConstraintType.TOP;
                    top = constraint;
                    hasTop = true;
                    break;
                case TOP_TO_BOTTOM:
                    topY = component.y + component.h;
                    topConstraint = ConstraintType.BOTTOM;
                    top = constraint;
                    hasTop = true;
                    break;
                case BOTTOM_TO_TOP:
                    bottomY = component.y - this.h;
                    bottomConstraint = ConstraintType.TOP;
                    bottom = constraint;
                    hasBottom = true;
                    break;
                case BOTTOM_TO_BOTTOM:
                    bottomY = component.y + component.h - this.h;
                    bottomConstraint = ConstraintType.BOTTOM;
                    bottom = constraint;
                    hasBottom = true;
                    break;
            }
        }

        if (hasStart && hasEnd) {
            if (this.w == 0) {
                this.w = Math.abs(endX - startX);
            } else {
                this.x = startX + ((endX - startX) / 2) - (this.w);
            }
        } else if (hasStart) {
            Component component = start.getComponent();
            this.x = (startConstraint == ConstraintType.START)
                    ? component.x
                    : component.x + component.w;
        } else if (hasEnd) {
            Component component = end.getComponent();
            this.x = (endConstraint == ConstraintType.START)
                    ? component.x - this.w
                    : component.x + component.w - this.w;
        }

        if (hasTop && hasBottom) {
            if (this.h == 0) {
                this.h = Math.abs(bottomY - topY);
            } else {
                this.y = topY + ((bottomY - topY) / 2);
            }
        } else if (hasTop) {
            Component component = top.getComponent();
            this.y = (topConstraint == ConstraintType.TOP)
                    ? component.y
                    : component.y + component.h;
        } else if (hasBottom) {
            Component component = bottom.getComponent();
            this.y = (bottomConstraint == ConstraintType.TOP)
                    ? component.y - this.h
                    : component.y + component.h - this.h;
        }

        for (Margin margin : margins) {
            int size = margin.getSize();
            MarginType marginType = margin.getMarginType();
            switch (marginType) {
                case START -> {
                    ms = size;
                    this.x += size;
                }
                case END -> {
                    me = size;
                    this.x -= size;
                }
                case TOP -> mt = size;
                case BOTTOM -> mb = size;
                case HORIZONTAL -> {
                    ms = size;
                    me = size;
                    this.x += size;
                    this.w -= size * 2;
                }
                case VERTICAL -> {
                    mt = size;
                    mb = size;
                }
            }
        }

        bounds = new Rectangle2D.Double(x, y, w, h);

        return this;
    }

    public final Rectangle2D getBounds() {
        return bounds != null ? bounds : new Rectangle2D.Double(x, y, w, h);
    }

    public int getId() {
        return id;
    }

    public void draw(Graphics2D g) {}
    public void mouseReleased(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}

    public static class ID {

        private static int id = 0;

        public static int generate() {
            id += 1;
            return id - 1;
        }
    }
}