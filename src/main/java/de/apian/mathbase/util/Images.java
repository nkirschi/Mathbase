/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Der Ressourcenlader für Bilder.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class Images {

    private Images() {
    }

    /**
     * Cache für externe Bilder im Dateisystem
     *
     * @since 1.0
     */
    private static final Map<String, Image> externalCache = new HashMap<>();

    /**
     * Cache für Bilder innerhalb des Klassenpfads
     *
     * @since 1.0
     */
    private static final Map<String, Image> internalCache = new HashMap<>();

    /**
     * Hilfsmethode für das Laden eines externen Bildes
     *
     * @param path Pfad des Bildes ausgehend vom Arbeitsverzeichnis des Programms
     * @return Gewünschtes Bild in Originalgröße als {@code Image} -Objekt
     * @throws IOException Bei fehlender/korrupter Bilddatei bzw. unzureichenden Zugriffsrechten
     *                     oder sonstigen Dateisystemfehlern
     * @since 1.0
     */
    public static Image getExternal(String path) throws IOException {
        if (!externalCache.containsKey(path)) {
            try {
                externalCache.put(path, new Image(Paths.get(path).toUri().toString()));
            } catch (Exception e) {
                throw new IOException("Fehler beim Laden des externen Bildes \"" + path + "\"");
            }
        }
        return externalCache.get(path);
    }

    /**
     * Hilfsmethode für das Laden eines innerhalb des Klassenpfades befindlichen Bildes
     *
     * @param fileName Pfad des Bildes ausgehend von der Wurzel des Klassenpfades
     * @return Gewünschtes Bild in Originalgröße als {@code Image} -Objekt
     * @throws IOException Bei fehlender/korrupter Bilddatei bzw. unzureichenden Zugriffsrechten
     *                     oder sonstigen Dateisystemfehlern
     * @since 1.0
     */
    public static Image getInternal(String fileName) {
        String path = Constants.IMAGE_ROOT + fileName;

        if (!internalCache.containsKey(path)) {
            InputStream in = Images.class.getResourceAsStream(path);
            if (in == null) {
                Logging.log(Level.SEVERE, Constants.FATAL_ERROR_MESSAGE);
                throw new InternalError(Constants.FATAL_ERROR_MESSAGE);
            }
            internalCache.put(path, new Image(in));
        }
        return internalCache.get(path);
    }

    /**
     * Größenänderung eines Bildes
     *
     * @param image  betreffendes Bild
     * @param width  gewünschte Breite
     * @param height gewünschte Höhe
     * @return skaliertes Bild
     */
    public static Image resize(Image image, int width, int height) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = scaledImage.getGraphics();
        g.drawImage(bufferedImage.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        g.dispose();
        return SwingFXUtils.toFXImage(scaledImage, null);
    }
}