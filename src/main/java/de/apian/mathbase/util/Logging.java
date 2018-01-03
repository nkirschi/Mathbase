/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.util;

import de.apian.mathbase.gui.dialog.ErrorAlert;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * Hilfsklasse mit statischen Methoden zum Loggen in verschiedene Medien.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class Logging {

    private Logging() {
    }

    /**
     * Referenz auf den globalen Logger für die Applikation.
     * <p>
     * Die API sieht eigentlich vor, in jeder Klasse, die etwas loggen können soll,
     * ein eigenes {@code Logging} -Objekt zu erstellen, aber für Mathbase reicht uns der hier...
     *
     * @since 1.0
     */
    private static final Logger logger = Logger.getGlobal();

    // Statischer Initialiserungsblock fürs Logging
    static {
        logger.setUseParentHandlers(false); // Deaktivierung des Standard-Konsolenoutputs
        Formatter formatter = new LogFormatter();

        // Ausgabe in eine Logdatei relativ zum Arbeitsverzeichnis
        try {
            FileHandler fileHandler = new FileHandler("log.txt", true);
            fileHandler.setFormatter(formatter);
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            ErrorAlert alert = new ErrorAlert(e);
            alert.showAndWait();
        }

        // Konsolenoutput (falls zu nervig, einfach das Mindestlevel ändern)
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(formatter);
        consoleHandler.setLevel(Level.WARNING); // Level, ab dem geloggt wird
        logger.addHandler(consoleHandler);
    }

    /**
     * Durchschiebemethode zum Loggen.
     *
     * @param lvl Schwere des Ereignisses, z.B. {@code Logging.INFO}
     * @param msg Zu loggende Nachricht
     * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/logging/Logger.html">Logging</a>
     * @since 1.0
     */
    public static void log(Level lvl, String msg) {
        logger.log(lvl, msg);
    }

    /**
     * Durchschiebemethode zum Loggen plus ein Werfbares.
     *
     * @param lvl Schwere des Ereignisses, z.B. {@code Logging.INFO}
     * @param msg Zu loggende Nachricht
     * @param thr {@code Throwable} -Objekt, meist als {@code Exception}
     * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/logging/Logger.html">Logging</a>
     * @since 1.0
     */
    public static void log(Level lvl, String msg, Throwable thr) {
        logger.log(lvl, msg, thr);
    }

    /**
     * Eigener Formatierer fürs Logging.
     */
    private static class LogFormatter extends Formatter {

        /**
         * Exemplar der Datumsformatierung.
         * <p>
         * Wird benötigt, um das Datum eines Erignisses in lesbarer Form zu loggen,
         * da die API nur die seit 1970 vergangenen Millisekunden anbietet.
         *
         * @see SimpleDateFormat
         * @since 1.0
         */
        private SimpleDateFormat dateFormat;

        /**
         * Default-Konstruktor, der das {@code DateFormat} und die Indentation initialisiert.
         *
         * @since 1.0
         */
        LogFormatter() {
            dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
        }

        /**
         * Unerwartete Aufgabe des Formatierers... Formatieren!
         *
         * @param record Von der Logging-API übergebener {@code LogRecord}
         * @return Formatierter String
         * @since 1.0
         */
        @Override
        public String format(LogRecord record) {
            String date = dateFormat.format(new Date(record.getMillis()));
            String level = record.getLevel().toString();
            String message = formatMessage(record);

            String log = String.format("[%s] %s: %s%n", date, level, message);

            // Anfügen eventueller Ausnahmen
            if (record.getThrown() != null) {
                StringWriter writer = new StringWriter();
                record.getThrown().printStackTrace(new PrintWriter(writer));
                log += writer.toString();
            }

            return log;
        }
    }
}