/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.xml;

import de.apian.mathbase.gui.dialog.ErrorAlert;
import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Logging;

import java.util.logging.Level;

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
     * Pfad der diesem Inhalt angehörigen Datei relativ zum Arbeitsverzeichnis
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

            /*
             * Nichts gefunden?! Darf und wird nie vorkommen, es sei denn jemand pfuscht in der XML rum ... -> Loggen
             * und Programm schließen
             */
            IllegalArgumentException e = new IllegalArgumentException("No enum constant " +
                    Type.class.getCanonicalName() + "." + name);
            new ErrorAlert(e).showAndWait();
            Logging.log(Level.SEVERE, Constants.FATAL_ERROR_MESSAGE, e);
            throw new InternalError(Constants.FATAL_ERROR_MESSAGE, e);
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