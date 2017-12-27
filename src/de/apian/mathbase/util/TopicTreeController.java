/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.util;

import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

/**
 * Utility-Klasse zum Laden, Bearbeiten und Speichern der XML-Dateien,
 * welche die Struktur und Inhalte der Themen enthalten.
 *
 * @author Benedikt Mödl
 * @version 1.0
 * @since 1.0
 */
public class TopicTreeController {

    /**
     * {@code XMLFileHandler} der XML-Datei
     *
     * @see <a href="{@docRoot}/de/apian/mathbase/util/XMLFileHandler.html">XMLFileHandler</a>
     * @since 1.0
     */
    private XMLFileHandler xmlHandler;

    /**
     * Pfad der originalen Datei relativ zum Arbeitsverzeichnis
     *
     * @since 1.0
     */
    private static final String ORIGINAL_PATH = "topic_tree.xml";

    /**
     * Pfad der Backup-Datei relativ zum Arbeitsverzeichnis
     *
     * @since 1.0
     */
    private static final String BACKUP_PATH = "topic_tree.xml.bak";

    /**
     * Bezeichner der Wurzel in der XML-Datei
     *
     * @since 1.0
     */
    private static final String TAG_ROOT = "topic_tree";

    /**
     * Bezeichner der Knoten in der XML-Datei
     *
     * @since 1.0
     */
    private final String TAG_NODE = "node";

    /**
     * Bezeichner der Inhalte in der XML-Datei
     *
     * @since 1.0
     */
    private final String TAG_CONTENT = "content";

    /**
     * Bezeichner des Attributs {@code title} in der XML-Datei
     *
     * @since 1.0
     */
    private final String ATTR_TITLE = "title";

    /**
     * Bezeichner des Attributs {@code type} (Typ der Inhalte) in der XML-Datei
     *
     * @since 1.0
     */
    private final String ATTR_TYPE = "type";

    /**
     * Konstruktor
     *
     * @throws IOException wenn die Datei sowie die Backup-Datei nicht geladen werden konnten
     * @since 1.0
     */
    public TopicTreeController() throws IOException {
        try { // Versuche zuerst die Original-Datei zu laden
            xmlHandler = new XMLFileHandler(ORIGINAL_PATH);
            Logger.log(Level.INFO, "Original-Datei \"" + ORIGINAL_PATH + "\" erfolgreich geladen");
        } catch (IOException ex1) {
            Logger.log(Level.WARNING, "Konnte Original-Datei \"" + ORIGINAL_PATH + "\" nicht laden", ex1);

            try { // Versuche im Fehlerfall die Backup-Datei wiederherzustellen
                Files.copy(Paths.get(BACKUP_PATH), Paths.get(ORIGINAL_PATH), StandardCopyOption.REPLACE_EXISTING);
                xmlHandler = new XMLFileHandler(ORIGINAL_PATH);
                Logger.log(Level.INFO, "Original-Datei \"" + ORIGINAL_PATH + "\" erfolgreich aus \""
                        + BACKUP_PATH + "\" wiederhergestellt und geladen");
            } catch (IOException ex2) {
                Logger.log(Level.SEVERE, "Datei \"" + ORIGINAL_PATH + "\" konnte nicht aus Backup-Datei \""
                        + BACKUP_PATH + "\" wiederhergestellt werden", ex2);

                // Schmeiß eine IOException, um den aufrufenden Klassen mitzuteilen,
                // dass die Datei nicht geladen werden konnte; diese sollen dann weiter verfahren!
                // TODO Errorhandling in den anderen Klassen implementieren
                throw new IOException("Keine Datei konnte geladen werden!");
            }
        }
    }

    /**
     * Speichern der Daten als XML-Datei im Pfad {@value #ORIGINAL_PATH} relativ zum Arbeitsverzeichnis
     *
     * @throws IOException wenn das Speichern nicht erfolgreich war
     * @since 1.0
     */
    public void save() throws IOException {
        try {
            xmlHandler.saveDocToXml(ORIGINAL_PATH);
            Logger.log(Level.INFO, "Speichern von \"" + ORIGINAL_PATH + "\" erfolgreich abgeschlossen");
        } catch (IOException ex) {
            Logger.log(Level.WARNING, "Fehler beim Speichern von \"" + ORIGINAL_PATH + "\"", ex);
            // Schmeiß eine IOException, um den aufrufenden Klassen mitzuteilen,
            // dass die Datei nicht geladen werden konnte; diese sollen dann weiter verfahren!
            // TODO Errorhandling in den anderen Klassen implementieren
            throw ex;
        }
    }

    /**
     * Erstellen eines Backups der originalen Datei im Pfad {@code BACKUP_PATH} relativ zum Arbeitsverzeichnis
     *
     * @throws IOException wenn das Erstellen und nicht erfolgreich war
     * @since 1.0
     */
    public void backUp() throws IOException {
        try {
            Files.copy(Paths.get(ORIGINAL_PATH), Paths.get(BACKUP_PATH), StandardCopyOption.REPLACE_EXISTING);
            Logger.log(Level.INFO, "Erstellen eines Backups von \"" + ORIGINAL_PATH + "\" in \"" + BACKUP_PATH + "\" erfolgreich abgeschlossen");
        } catch (IOException ex) {
            Logger.log(Level.WARNING, "Fehler beim Erstellen der Backupdatei \"" + BACKUP_PATH + "\"", ex);
            // Schmeiß eine IOException, um den aufrufenden Klassen mitzuteilen,
            // dass die Datei nicht gesichert werden konnte; diese sollen dann weiter verfahren!
            // TODO Errorhandling in den anderen Klassen implementieren
            throw ex;
        }
    }

    /**
     * Neuerstellung der XML-Datei im Pfad {@value #ORIGINAL_PATH} relativ zum Arbeitsverzeichnis.
     * <p>
     * Kann auch aufgerufen werden, wenn der {@code TopicTreeController} noch nicht instanziert wurde,
     * damit das Programm trotz Fehlen der XML-Datei + Backup funktionstüchtig bleibt.
     * </p>
     *
     * @throws IOException wenn das Erstellen nicht erfolgreich war
     * @since 1.0
     */
    public static void createNewFile() throws IOException {
        try {
            Path path = Paths.get(ORIGINAL_PATH);
            Files.createFile(path);
            BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"));
            writer.write(String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n\n" +
                    "<!--\n  ~ Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.\n" +
                    "  ~ This product is licensed under the GNU General Public License v3.0.\n" +
                    "  ~ See LICENSE file for further information.\n  -->\n\n" +
                    "<%s></%s>", TAG_ROOT, TAG_ROOT));
            writer.close();
            Logger.log(Level.INFO, "Datei \"" + ORIGINAL_PATH + "\" erfolgreich neu erstellt");
        } catch (IOException ex) {
            Logger.log(Level.SEVERE, "Datei \"" + ORIGINAL_PATH + "\" konnte nicht neu erstellt werden");
            // Schmeiß eine IOException, um den aufrufenden Klassen mitzuteilen,
            // dass die Datei nicht erstellt werden konnte; diese sollen dann weiter verfahren!
            // TODO Errorhandling in den anderen Klassen implementieren
            throw ex;
        }
    }

    /**
     * Überprüft, ob bereits ein Knoten mit diesem Titel vorhanden ist. Titel sind global einzigartig.
     *
     * @param title Der zu überprüfende Titel
     * @return Ob ein Knoten mit diesem Titel existiert
     * @since 1.0
     */
    public boolean alreadyExists(String title) {
        boolean exists = false;
        try {
            NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title + "']");
            if (nodeList.getLength() > 0)
                exists = true;
            Logger.log(Level.INFO, "Existenz von Knoten mit Titel \"" + title + "\" überprüft: " + exists);
        } catch (XPathExpressionException ex) { // Kann eigentlich niemals vorkommen
            Logger.log(Level.WARNING, Constants.FATAL_ERROR_MESSAGE, ex);
        }
        return exists;
    }

    /**
     * Gibt eine {@code NodeList} aller Inhalte eines bestimmten Knotens zurück
     *
     * @param title Der Titel des Knotens
     * @return Eine {@code NodeList} aller Inhalte des Knotens
     * @since 1.0
     */
    public NodeList getContents(String title) {
        try {
            NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title + "']/" + TAG_CONTENT);
            Logger.log(Level.INFO, "Inhalte des Knotens mit dem Titel \"" + title + "\" zurückgegeben");
            return nodeList;
        } catch (XPathExpressionException e) { // Kann eigentlich niemals vorkommen
            /*
             * Dieser Fall kann eigentlich niemals eintreten, da die XPathExpression hardgecoded ist.
             * Sollte unwahrscheinlicherweise doch einmal etwas an der XPath-API geändert werden,
             * wäre das gesamte Programm sowieso erstmal unbrauchbar!
             */
            Logger.log(Level.WARNING, Constants.FATAL_ERROR_MESSAGE, e);
            throw new InternalError(Constants.FATAL_ERROR_MESSAGE);
        }
    }

    /**
     * Gibt eine {@code NodeList} aller direkten Kind-Knoten eines bestimmten Knotens zurück
     *
     * @param title Der Titel des Knotens
     * @return Eine {@code Nodelist} aller direkten Kind-Knoten des Knotens
     * @since 1.0
     */
    public NodeList getChildNodes(String title) {
        try {
            NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title + "']/" + TAG_NODE);
            Logger.log(Level.INFO, "Kind-Knoten des Knotens mit dem Titel \"" + title + "\" zurückgegeben");
            return nodeList;
        } catch (XPathExpressionException e) { // Kann eigentlich niemals vorkommen
            Logger.log(Level.WARNING, Constants.FATAL_ERROR_MESSAGE, e);
            throw new InternalError(Constants.FATAL_ERROR_MESSAGE);
        }
    }

    /**
     * Erzeugt aus einem beliebigen {@code String} einen unseren Standards entsprechenden Dateinamen
     *
     * @param fileName Ausgangs-{@code String}
     * @return Einen unseren Standards entsprechenden Dateinamen als {@code String}
     * @since 1.0
     */
    private String normalizeFilename(String fileName) {
        return fileName.toLowerCase().replaceAll("[^a-zäöüß0-9\\.\\-]", "_");
    }

    public static void main(String[] args) throws Exception {
        TopicTreeController controller = new TopicTreeController();
        NodeList nodeList = controller.getContents("Häufigkeitsanalyse");
        System.out.println(nodeList.item(3).getAttributes().getNamedItem("type"));
    }
}