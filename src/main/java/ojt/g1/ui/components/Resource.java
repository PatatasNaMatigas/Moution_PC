package ojt.g1.ui.components;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Resource {

    private static HashMap<String, Font> loadedFonts = new HashMap<>();

    public static Font getFont(String fileName, float size) {
        String key = fileName + "|" + size;
        if (loadedFonts.containsKey(key)) {
            return loadedFonts.get(key); // Return cached font
        }

        try (InputStream is = Resource.class.getClassLoader().getResourceAsStream("font/" + fileName)) {
            if (is == null) {
                throw new IOException("Font file not found: " + fileName);
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
            loadedFonts.put(key, font);
            return font;
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage getImage(String fileName) {
        try (InputStream is = Resource.class.getClassLoader().getResourceAsStream("image/" + fileName)) {
            if (is == null) {
                throw new IOException("Image file not found: " + fileName);
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
