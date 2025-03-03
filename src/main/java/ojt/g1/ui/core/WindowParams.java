package ojt.g1.ui.core;

import java.awt.*;

public class WindowParams {

    protected Image imageIcon;
    private String title;

    private WindowParams(Builder builder) {
        this.imageIcon = builder.imageIcon;
        this.title = builder.title;
    }

    public static class Builder {
        private Image imageIcon;
        private String title;

        public Builder imageIcon(Image imageIcon) {
            this.imageIcon = imageIcon;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public WindowParams build() {
            return new WindowParams(this);
        }
    }

    public Image getImageIcon() {
        return imageIcon;
    }

    public String getTitle() {
        return title;
    }
}