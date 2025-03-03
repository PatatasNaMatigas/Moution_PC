package ojt.g1.ui.components;

public class Margin {

    private MarginType marginType;
    private int size;

    public Margin(MarginType marginType, int size) {
        this.marginType = marginType;
        this.size = size;
    }

    public MarginType getMarginType() {
        return marginType;
    }

    public int getSize() {
        return size;
    }
}
