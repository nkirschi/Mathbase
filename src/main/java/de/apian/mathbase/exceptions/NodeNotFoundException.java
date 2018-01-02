/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.exceptions;

/**
 * Nichtvorhandensein eines bestimmten Knotens.
 *
 * @author Benedikt Mödl
 * @version 1.0
 * @since 1.0
 */
public class NodeNotFoundException extends Exception {

    /**
     * Standard-Konstruktor mit Nachricht
     *
     * @param title Titel des Knotens
     * @since 1.0
     */
    public NodeNotFoundException(String title) {
        super("Knoten " + title + " nicht gefunden!");
    }

    /**
     * Standard-Konstruktor mit Nachricht und Grund für diese Ausnahme
     *
     * @param title Titel des Knotens
     * @param cause   Grund für Ausnahme
     * @since 1.0
     */
    public NodeNotFoundException(String title, Throwable cause) {
        super("Knoten " + title + " nicht gefunden!", cause);
    }
}
