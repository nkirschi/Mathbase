/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * Hilfsklasse mit statischen Methoden zum Loggen in verschiedene Medien
 */
public class Logger {
    /**
     * Referenz auf den globalen Logger für die Applikation
     * <p>
     * Die API sieht eigentlich vor, in jeder Klasse, die etwas loggen können soll,
     * ein eigenes <tt>Logger</tt>-Objekt zu erstellen, aber für Mathbase reicht uns der hier...
     */
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getGlobal();

    // Statischer Initialiserungsblock für den Logger
    static {
        logger.setUseParentHandlers(false); // Deaktivierung des Standard-Konsolenoutputs
        Formatter formatter = new LogFormatter();

        // Ausgabe in eine Logdatei relativ zum Arbeitsverzeichnis
        try {
            FileHandler fileHandler = new FileHandler("log.txt", true);
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Konsolenoutput (falls zu nervig, einfach die letzte Zeile auskommentieren)
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(formatter);
        logger.addHandler(consoleHandler);
    }

    /**
     * Durchschiebemethode zum Loggen
     *
     * @param lvl Schwere des Ereignisses, z.B. <tt>Logger.INFO</tt>
     * @param msg Zu loggende Nachricht
     * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/logging/Logger.html">Logger</a>
     */
    public static void log(Level lvl, String msg) {
        logger.log(lvl, msg);
    }

    /**
     * Durchschiebemethode zum Loggen plus ein Werfbares
     *
     * @param lvl Schwere des Ereignisses, z.B. <tt>Logger.INFO</tt>
     * @param msg Zu loggende Nachricht
     * @param thr <tt>Throwable</tt>-Objekt, meist als <tt>Exception</tt>
     * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/logging/Logger.html">Logger</a>
     */
    public static void log(Level lvl, String msg, Throwable thr) {
        logger.log(lvl, msg, thr);
    }

    /**
     * Eigener Formatierer fürs Logging
     */
    private static class LogFormatter extends Formatter {
        private SimpleDateFormat dateFormat; // Exemplar der Datumsformatierung

        /**
         * Default-Konstruktor, der das <tt>DateFormat</tt> initialisiert
         */
        LogFormatter() {
            dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
        }

        /**
         * Einzige Aufgabe des Formatierers... Formatieren!
         *
         * @param record Von der Logging-API übergebener <tt>LogRecord</tt>
         * @return Formatierter String
         */
        @Override
        public String format(LogRecord record) {
            // Datum in lesbarer Form, da die API nur die seit 1970 vergangenen Millisekunden anbietet
            String date = dateFormat.format(new Date(record.getMillis()));
            // Ermitteln des den LogRecord verursachenden Threads
            String thread = "";
            for (Thread t : Thread.getAllStackTraces().keySet())
                if (t != null && t.getId() == record.getThreadID())
                    thread = t.getName();

            String level = "";
            if (record.getLevel() != null)
                level = record.getLevel().toString();

            String message = "";
            if (record.getMessage() != null)
                message = formatMessage(record);

            String format = "%s [%s] %s: %s%n";
            String log = String.format(format, date, thread, level, message);

            if (record.getThrown() != null) {
                String thrown = String.format("%s%n", record.getThrown());
                for (StackTraceElement s : record.getThrown().getStackTrace()) {
                    thrown += String.format("%c%c%c%c%s%n", ' ', ' ', ' ', ' ',
                            "at " + s.getClassName() + "." + s.getMethodName() +
                                    "(" + s.getFileName() + ":" + s.getLineNumber() + ")");
                }
                log += thrown;
            }

            return log;
        }
    }
}