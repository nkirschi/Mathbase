/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.util;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Der Ressourcenlader für Bilder.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class Images {

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
     * Lädt ein Bild aus dem Cache und, falls nicht vorhanden, vom Dateisystem
     *
     * @param path     Pfad des Bildes
     * @param internal Bestimmt, ob sich Bild innerhalb des Klassenpfades befindet oder nicht
     * @return Gewünschtes Bild in Originalgröße als {@code Image} -Objekt
     * @throws IOException Bei fehlender/korrupter Bilddatei bzw. unzureichenden Zugriffsrechten
     *                     oder sonstigen Dateisystemfehlern
     * @since 1.0
     */
    public static Image fetch(String path, boolean internal) throws IOException {
        return internal ? fetchInternal(path) : fetchExternal(path);
    }

    /**
     * Vereinfachung der allgemeinen Methode mit standardmäßig externem Laden
     *
     * @param path Pfad des Bildes
     * @return Gewünschtes Bild in Originalgröße als {@code Image} -Objekt
     * @throws IOException Bei fehlender/korrupter Bilddatei bzw. unzureichenden Zugriffsrechten
     *                     oder sonstigen Dateisystemfehlern
     * @since 1.0
     */
    public static Image fetch(String path) throws IOException {
        return fetch(path, false);
    }

    /**
     * Hilfsmethode für das Laden eines externen Bildes
     *
     * @param path Pfad des Bildes ausgehend vom Arbeitsverzeichnis des Programms
     * @return Gewünschtes Bild in Originalgröße als {@code Image} -Objekt
     * @throws IOException Bei fehlender/korrupter Bilddatei bzw. unzureichenden Zugriffsrechten
     *                     oder sonstigen Dateisystemfehlern
     * @since 1.0
     */
    private static Image fetchExternal(String path) throws IOException {
        if (!externalCache.containsKey(path)) {
            try {
                externalCache.put(path, new Image(path));
            } catch (Exception e) {
                throw new IOException("Fehler beim Laden des externen Bildes \"" + path + "\"");
            }
        }
        return externalCache.get(path);
    }

    /**
     * Hilfsmethode für das Laden eines innerhalb des Klassenpfades befindlichen Bildes
     *
     * @param path Pfad des Bildes ausgehend von der Wurzel des Klassenpfades
     * @return Gewünschtes Bild in Originalgröße als {@code Image} -Objekt
     * @throws IOException Bei fehlender/korrupter Bilddatei bzw. unzureichenden Zugriffsrechten
     *                     oder sonstigen Dateisystemfehlern
     * @since 1.0
     */
    private static Image fetchInternal(String path) throws IOException {
        if (!internalCache.containsKey(path)) {
            InputStream in = ClassLoader.getSystemResourceAsStream(path);
            if (in == null)
                throw new IOException("Fehler beim Laden des internen Bildes \"" + path + "\"");
            internalCache.put(path, new Image(in));
        }
        return internalCache.get(path);
    }
}