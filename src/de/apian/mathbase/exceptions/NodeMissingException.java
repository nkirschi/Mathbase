/*
 * Copyright (c) 2017. MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.exceptions;

/**
 * Wird geworfen, falls es zu einer Kollision der Titel zweier Knoten kommen würde
 *
 * @since 1.0
 */
public class NodeMissingException extends Exception {

    /**
     * Standard-Konstruktor
     *
     * @since 1.0
     */
    public NodeMissingException() {
        super();
    }

    /**
     * Standard-Konstruktor mit Nachricht
     *
     * @param message Nachricht
     * @since 1.0
     */
    public NodeMissingException(String message) {
        super(message);
    }

    /**
     * Standard-Konstruktor mit Nachricht und Grund für diese Ausnahme
     *
     * @param message Nachricht
     * @param cause   Grund für Ausnahme
     * @since 1.0
     */
    public NodeMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Standard-Konstruktor mit Grund für diese Ausnahme
     *
     * @param cause Grund für Ausnahme
     * @since 1.0
     */
    public NodeMissingException(Throwable cause) {
        super(cause);
    }
}
