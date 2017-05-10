package util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Hilfsklasse für die einfachere Handhabung von Bildern ;)
 */
public class ImageUtil {
    // Der Bilder-Cache; Wenn man hin- und hernavigiert, soll doch nicht jedes mal dasselbe Bild neu geladen werden ;)
    private static final Map<String, BufferedImage> imageCache = new HashMap<>();

    /**
     * Der Konstruktor ist hier privat, da von dieser Klasse nie ein Objekt existieren soll.
     * Somit sind auch sämtliche Methoden hier drin statisch!
     */
    private ImageUtil() {}

    /**
     * Methode für das Laden eines Bildes
     * @param path Der Pfad der Bilddatei, ausgehend vom src root folder
     * @return Das Bild als Objekt der Klasse BufferedImage
     * @throws IOException falls es Probleme mit dem angegebenen Pfad gibt
     */
    public static BufferedImage getImage(String path) throws IOException {
        if (!imageCache.containsKey(path)) {
            BufferedImage image = ImageIO.read(ClassLoader.getSystemResourceAsStream(path));
            if (image == null)
                throw new IOException("File not found."); // wichtig für die Nachvollziehbarkeit von Fehlern
            imageCache.put(path, image);
        }
        return imageCache.get(path);
    }
}
