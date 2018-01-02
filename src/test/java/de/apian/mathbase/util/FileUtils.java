/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.Comparator;

/**
 * Nützlichkeiten für Dateioperationen
 *
 * @author Nikolas Kirschstein.
 * @version 1.0
 * @since 1.0
 */
public class FileUtils {

    private FileUtils() {
    }

    /**
     * Normalisierung eines beliebigen {@code String} zu einem unseren Standards entsprechenden Datei-/Ordnernamen
     *
     * @param s Ausgangs-{@code String}
     * @return Normalisierter {@code String}
     * @since 1.0
     */
    public static String normalize(String s) {
        // Ersetzung der Leerzeichen und Umlaute
        s = s.toLowerCase().replace(" ", "_")
                .replace("ä", "ae")
                .replace("ö", "oe")
                .replace("ü", "ue")
                .replace("ß", "ss");
        // Entfernung aller Sonderzeichen, Akzente etc.
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[^a-z_0-9.\\-]", "");
    }

    /**
     * Verschieben eines kompletten Verzeichnisses
     *
     * @param from Urpfad
     * @param to   Zielpfad
     * @throws IOException wenn das Verschieben fehlschlägt
     * @since 1.0
     */
    public static void move(Path from, Path to) throws IOException {
        if (Files.exists(to, LinkOption.NOFOLLOW_LINKS))
            delete(to);
        Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Löschen eines kompletten Verzeichnises samt Inhalt
     *
     * @param path Pfad
     * @throws IOException wenn das Löschen fehlschlägt
     * @since 1.0
     */
    public static void delete(Path path) throws IOException {
        Files.walk(path).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
    }
}
