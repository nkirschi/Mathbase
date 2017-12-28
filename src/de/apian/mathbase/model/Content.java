/*
 * Copyright (c) 2017. MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.model;

/**
 * Wird erzeugt, wenn die Contents aus der XML-Datei ausgelesen werden, um die benötigten Informationen an andere
 * Klassen weiterzugeben
 *
 * @author Benedikt Mödl
 * @version 1.0
 * @since 1.0
 */
public class Content {

    /**
     * Titel dieses Inhalts
     *
     *@since 1.0
     */
    private String title;

    /**
     * Typ dieses Inhalts
     *
     * @since 1.0
     */
    private Type type;

    /**
     * Pfad der Datei dieses Inhalts relativ zum Arbeitsverzeichnis
     *
     * @since 1.0
     */
    private String path;

    /**
     * Festgelegte Kontent-Typen
     *
     * @author Benedikt Mödl
     * @version 1.0
     * @since 1.0
     */
    public enum Type {
        IMAGE("image"),
        WORKSHEET("worksheet"),
        VIDEO("video"),
        GEOGEBRA("geogebra"),
        DESCRIPTION("description"),
        OTHER("other");

        /**
         * Wert des Kontent-Typs in der XML-Datei
         *
         * @since 1.0
         */
        public final String ATTR_VALUE;

        /**
         * Konstruktor
         *
         * @since 1.0
         */
        Type(String ATTR_VALUE) {
            this.ATTR_VALUE = ATTR_VALUE;
        }
    }
}