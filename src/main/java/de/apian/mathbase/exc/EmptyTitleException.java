package de.apian.mathbase.exc;

/**
 * Titel eines bestimmten Knotens ist leer (Nicht {@code NULL}!)
 *
 * @author Benedikt Mödl
 * @version 1.0
 * @since 1.0
 */
public class EmptyTitleException extends Exception {

    /**
     * Standard-Konstruktor
     *
     * @since 1.0
     */
    public EmptyTitleException() {
        super();
    }

    /**
     * Standard-Konstruktor mit Nachricht
     *
     * @param message Nachricht
     * @since 1.0
     */
    public EmptyTitleException(String message) {
        super(message);
    }

    /**
     * Standard-Konstruktor mit Nachricht und Grund für diese Ausnahme
     *
     * @param message Nachricht
     * @param cause   Grund für Ausnahme
     * @since 1.0
     */
    public EmptyTitleException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Standard-Konstruktor mit Grund für diese Ausnahme
     *
     * @param cause Grund für Ausnahme
     * @since 1.0
     */
    public EmptyTitleException(Throwable cause) {
        super(cause);
    }
}
