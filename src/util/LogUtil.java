package util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Nützliche Klasse für das Logging in eine Datei
 */
public class LogUtil {
    private static BufferedWriter writer;

    // Konstanten für die Angabe eines Logging-Levels
    private static final String INFO = "INFO: ";
    private static final String WARNING = "WARNUNG: ";
    private static final String ERROR = "FEHLER: ";

    /**
     * Statischer Block, der beim Laden der Klasse ausgeführt wird (konstruktorähnlich)
     */
    static {
        try {
            writer = new BufferedWriter(new FileWriter("log.txt", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allgemeine Methode für das Loggen eines Strings
     * @param level Das Level des zu loggenden Ereignisses
     * @param msg Die Nachricht zum entsprechenden Ereignis
     */
    public static void log(String level, String msg) {
        try {
            String timestamp = new SimpleDateFormat("[E, d.MM.yyyy hh:mm:ss.SSS] ").format(new Date());
            writer.write(timestamp);
            writer.write(level);
            writer.write(msg);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Spezialisierung der allgemeinen log-Methode für Exceptions
     * @param level Das Level der zu loggenden Exception
     * @param e Die zu loggende Exception
     */
    public static void log(String level, Exception e) {
        for (StackTraceElement i : e.getStackTrace()) {
            String s = e.toString().concat(" in ").concat(i.getClassName())
                    .concat(" in Methode ").concat(i.getMethodName()).concat("()")
                    .concat(" in Zeile ").concat(Integer.toString(i.getLineNumber()));
            log(level, s);
        }
    }

    /**
     * Methode zu Testzwecken
     * @param args Kommandozeilenparameter
     */
    public static void main(String[] args) {
        log(LogUtil.INFO, "System erfolgreich gestartet");
        try {
            "".concat(null);
        } catch (NullPointerException e) {
            log(LogUtil.WARNING, e);
        }
        try {
            new BufferedReader(new FileReader("abc.txt"));
        } catch (FileNotFoundException e) {
            log(LogUtil.ERROR, e);
        }
        log(LogUtil.ERROR, "Fataler Fehler! Der Benutzer ist hässlich!");
        log(LogUtil.INFO, "System erfolgreich beendet");
    }
}
