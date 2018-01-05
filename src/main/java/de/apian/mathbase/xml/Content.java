/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.xml;

/**
 * Repräsentation eines Themeninhalts.
 *
 * @author Benedikt Mödl
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class Content {

    /**
     * Typ dieses Inhalts als {@code Type}-Objekt
     *
     * @since 1.0
     */
    private Type type;

    /**
     * Pfad der diesem Inhalt angehörigen Datei relativ zum Ordner des Elternknotens
     * <p>
     * Wenn ein neuer Inhalt zur XML-Datei hinzugefügt werden soll, so wird ein {@code Content}-Objekt erzeugt, welches
     * als Pfad den ursprünglichen und vollständigen Pfad der Datei hat, damit diese dann kopiert werden kann.
     *
     * @since 1.0
     */
    private String path;

    /**
     * Optionaler Titel des Inhalts
     *
     * @since 1.0
     */
    private String title;

    /**
     * Konstruktion eines neuen Inhaltes
     *
     * @param type  Typ des Inhalts
     * @param path  Pfad des Inhalts
     * @param title Titel des Inhalts
     */
    public Content(Type type, String path, String title) {
        this.type = type;
        this.path = path;
        this.title = title;
    }

    /**
     * @return Typ des Inhalts
     */
    public Type getType() {
        return type;
    }

    /**
     * @return Pfad der diesem Inhalt angehörigen Datei relativ zum Arbeitsverzeichnis
     */
    public String getPath() {
        return path;
    }

    /**
     * @return Titel des Inhalts
     */
    public String getTitle() {
        return title;
    }

    /**
     * Konvertierung des Inhalts in eine Zeichenkette
     *
     * @return Den Inhalt beschreibende Zeichenkette
     */
    @Override
    public String toString() {
        return "Content: \"" + title + "\" | \"" + type.toString() + "\" | \"" + path + "\"";
    }

    /**
     * Wohldefinierte Typen
     *
     * @author Benedikt Mödl
     * @author Nikolas Kirschstein
     * @version 1.0
     * @since 1.0
     */
    public enum Type {
        IMAGE, WORKSHEET, VIDEO, GEOGEBRA, DESCRIPTION, OTHER;

        /**
         * Ermitteln des Typs für einen vorgegebenen Namen
         *
         * @param name Typbezeichner
         * @return Gesuchte Enum-Typkonstante
         */
        public static Type forName(String name) {
            for (Type type : values()) {
                if (type.name().equalsIgnoreCase(name))
                    return type;
            }
            throw new IllegalArgumentException("No enum constant " + Type.class.getCanonicalName() + "." + name);
        }

        /**
         * Konvertierung des Typs in eine Zeichenkette
         *
         * @return Typbezeichner in Kleinbuchstaben
         */
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}