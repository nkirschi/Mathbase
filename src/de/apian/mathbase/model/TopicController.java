/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.model;

import de.apian.mathbase.exceptions.NodeMissingException;
import de.apian.mathbase.exceptions.TitleCollisionException;
import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.FileUtils;
import de.apian.mathbase.util.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
public class TopicController {

    /**
     * Pfad des Topic-Ordners relativ zum Arbeitsverzeichnis. Hier werden die eigentlichen Dateien gespeichert
     *
     * @since 1.0
     */
    static final String TOPICS_PATH = "topics";

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
     * Bezeichner des Attributs {@code path} (Dateipfad einer Datei relativ zum Ordner des Elternknoten) in der XML-Datei
     *
     * @since 1.0
     */
    private final String ATTR_PATH = "path";

    /**
     * Konstruktor
     *
     * @throws IOException wenn das Laden der XML-Datei fehlschlägt oder der Topics-Ordner nicht existiert
     * @since 1.0
     */
    public TopicController() throws IOException {
        load();
    }

    /**
     * Lädt die XML-Datei. Ist dies nicht möglich wird die Backupdatei geladen, ist dies auch nicht möglich bricht die
     * Methode ab
     *
     * @throws IOException wenn die Datei sowie die Backup-Datei nicht geladen werden konnten
     * @since 1.0
     */
    private void load() throws IOException {
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
                // dass die Datei nicht geladen werden konnte
                throw new IOException("Keine Datei konnte geladen werden!");
            }
        }
    }

    /**
     * Speichern der Daten als XML-Datei im Pfad {@value #ORIGINAL_PATH} relativ zum Arbeitsverzeichnis.
     * Wird von den die XML-Datei bearbeitenden Methoden selbst aufgerufen.
     *
     * @throws IOException wenn das Speichern nicht erfolgreich war
     * @since 1.0
     */
    private void save() throws IOException {
        try {
            xmlHandler.saveDocToXml(ORIGINAL_PATH);
            Logger.log(Level.INFO, "Speichern von \"" + ORIGINAL_PATH + "\" erfolgreich abgeschlossen");
        } catch (IOException ex) {
            Logger.log(Level.WARNING, "Fehler beim Speichern von \"" + ORIGINAL_PATH + "\"", ex);
            // Schmeiß eine IOException, um den aufrufenden Methoden mitzuteilen,
            // dass die Datei nicht gespeichert werden konnte
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
            Logger.log(Level.INFO, "Erstellen eines Backups von \"" + ORIGINAL_PATH + "\" in \"" + BACKUP_PATH
                    + "\" erfolgreich abgeschlossen");
        } catch (IOException ex) {
            Logger.log(Level.WARNING, "Fehler beim Erstellen der Backupdatei \"" + BACKUP_PATH + "\"", ex);
            // Schmeiß eine IOException, um den aufrufenden Klassen mitzuteilen,
            // dass die Datei nicht gesichert werden konnte; diese sollen dann weiter verfahren!
            // TODO Errorhandling in den anderen Klassen implementieren
            throw ex;
        }
    }

    /**
     * Neuerstellung der XML-Datei im Pfad {@value #ORIGINAL_PATH} und des Topic-Ordners im Pfad {@value #TOPICS_PATH}
     * relativ zum Arbeitsverzeichnis. Bereits existierende Dateien/Ordner werden mit der Endung .old erweitert.
     * <p>
     * Kann auch aufgerufen werden, wenn der {@code TopicController} noch nicht instanziert wurde,
     * damit das Programm trotz Fehlen der XML-Datei + Backup funktionstüchtig bleibt.
     * </p>
     *
     * @throws IOException wenn das Erstellen nicht erfolgreich war
     * @since 1.0
     */
    public void recreateFile() throws IOException {
        try {
            //Erstellt XML-Datei
            {
                Path path = Paths.get(ORIGINAL_PATH);
                if (Files.exists(path)) {
                    FileUtils.move(path, Paths.get(ORIGINAL_PATH + ".old"));
                    Logger.log(Level.WARNING, "Existierende " + ORIGINAL_PATH + "-Datei gefunden. Wurde vor " +
                            "Neuerstellung umbenannt!");
                }
                Files.createFile(path);
                BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"));
                writer.write(String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n\n" +
                        "<!--\n  ~ Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.\n" +
                        "  ~ This product is licensed under the GNU General Public License v3.0.\n" +
                        "  ~ See LICENSE file for further information.\n  -->\n\n" +
                        "<%s></%s>", TAG_ROOT, TAG_ROOT));
                writer.close();
                Logger.log(Level.INFO, "Datei \"" + ORIGINAL_PATH + "\" erfolgreich neu erstellt");
            }

            //Erstellt Topics-Ordner
            {
                Path path = Paths.get(TOPICS_PATH);
                if (Files.exists(path)) {
                    FileUtils.move(path, Paths.get(TOPICS_PATH + ".old"));
                    Logger.log(Level.WARNING, "Existierende " + TOPICS_PATH + "-Ordner gefunden. Wurde vor " +
                            "Neuerstellung umbenannt!");
                }
                Files.createDirectory(path);
            }

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
    private boolean alreadyExists(String title) {
        boolean exists = false;
        try {
            NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title
                    + "']");
            if (nodeList.getLength() > 0)
                exists = true;
            Logger.log(Level.INFO, "Existenz von Knoten mit Titel \"" + title + "\" überprüft: " + exists);
        } catch (XPathExpressionException e) {
            /*
             * Dieser Fall kann eigentlich niemals eintreten, da die XPathExpression hardgecoded ist.
             * Sollte unwahrscheinlicherweise doch einmal etwas an der XPath-API geändert werden,
             * wäre das gesamte Programm sowieso erstmal unbrauchbar!
             */
            Logger.log(Level.WARNING, Constants.FATAL_ERROR_MESSAGE, e);
            throw new InternalError(Constants.FATAL_ERROR_MESSAGE);
        }
        return exists;
    }

    /**
     * Gibt den Pfad des Ordners des Knotens mit einem bestimmten Titel relativ zum Arbeitsverzeichnis zurück
     *
     * @param title Titel des Knotens
     * @return Pfad des Ordners des Knotens relativ zum Arbeitsverzeichnis
     * @throws NodeMissingException wenn der Knoten nicht exisiert
     * @since 1.0
     */
    public String getNodePath(String title) throws NodeMissingException {
        try {
            NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title +
                    "']");

            if (nodeList.getLength() == 0) {
                Logger.log(Level.WARNING, "Knoten \"" + title + "\" konnte nicht gefunden werden!");
                throw new NodeMissingException();
            }
            Node node = nodeList.item(0);
            String path = TOPICS_PATH + getNodePath(node);
            Logger.log(Level.INFO, "Pfad zum Ordner des Knotens \"" + title + "\" gefunden.");
            return path;
        } catch (XPathExpressionException e) {
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
     * Rekursive Hilfmethode zum Finden von Pfaden des Ordners eines Knoten
     *
     * @param node Der Ausgangsknoten
     * @return Pfad zum Ordner des Knotens ausgehend vom {@value TOPICS_PATH}-Ordner
     */
    private String getNodePath(Node node) {
        String path = "/" + FileUtils.normalize(node.getAttributes().getNamedItem(ATTR_TITLE).getTextContent());
        if (node.getParentNode().getNodeName().equals(TAG_ROOT))
            return path;
        return getNodePath(node.getParentNode()) + path;
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
            NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title
                    + "']/" + TAG_CONTENT);
            Logger.log(Level.INFO, "Inhalte des Knotens mit dem Titel \"" + title + "\" zurückgegeben");
            return nodeList;
        } catch (XPathExpressionException e) {
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
     * Gibt eine {@code String}-Array der Titel aller direkten Kind-Knoten eines bestimmten Knotens zurück
     *
     * @param title Der Titel des Knotens
     * @return Ein {@code String}-Array der Titel aller direkten Kind-Knoten des Knotens
     * @since 1.0
     */
    public String[] getChildNodes(String title) {
        try {
            NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title
                    + "']/" + TAG_NODE);
            String[] result = new String[nodeList.getLength()];
            for (int i = 0; i < nodeList.getLength(); i++) {
                result[i] = nodeList.item(i).getAttributes().getNamedItem(ATTR_TITLE).getTextContent();
            }
            Logger.log(Level.INFO, "Titel aller Kind-Knoten des Knotens mit dem Titel \"" + title + "\" zurückgegeben");
            return result;
        } catch (XPathExpressionException e) {
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
     * Gibt alle Titel der Knoten der ersten Ebene der XML-Datei zurück
     *
     * @return Ein {@code String}-Array, welches alle Titel der Knoten in der ersten Ebene der XML-Datei
     * @since 1.0
     */
    public String[] getTopNodes() {
        try {
            NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_ROOT + "/" + TAG_NODE);
            String[] result = new String[nodeList.getLength()];
            for (int i = 0; i < nodeList.getLength(); i++) {
                result[i] = nodeList.item(i).getAttributes().getNamedItem(ATTR_TITLE).getTextContent();
            }
            Logger.log(Level.INFO, "Titel der Top-Level-Knoten zurückgegeben");
            return result;
        } catch (XPathExpressionException e) {
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
     * Erstellt einen neuen Knoten (sofern Titel nicht schon vergeben) unter einem gegebenen Eltern-Knoten
     *
     * @param parent Titel des Eltern-Knotens
     * @param title  Titel des Knotens
     * @throws TitleCollisionException wenn bereits ein Knoten mit diesem Titel existiert
     * @throws NodeMissingException    wenn der Elternknoten nicht exisiert
     * @throws IOException             wenn speichern der XML-Datei fehlschlägt, bzw wenn erstellen des Ordners
     *                                 fehlschlägt
     * @since 1.0
     */
    public void createNode(String parent, String title) throws TitleCollisionException, NodeMissingException,
            IOException {
        if (alreadyExists(title))
            throw new TitleCollisionException();
        if (!alreadyExists(parent))
            throw new NodeMissingException();

        try {
            //Knoten wird erstellt und zum Elternknoten hinzugefügt
            NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + parent
                    + "']");
            Node parentNode = nodeList.item(0);
            Element element = xmlHandler.getDoc().createElement(TAG_NODE);
            element.setAttribute(ATTR_TITLE, title);
            parentNode.appendChild(element);

            //Ordner wird erstellt
            String sPath = ""; //TODO IMPL + LOGGING
            Path path = Paths.get(sPath);
            Files.createDirectory(path);

            //XML-Datei wird gespeichert
            save();
        } catch (XPathExpressionException e) {
            /*
             * Dieser Fall kann eigentlich niemals eintreten, da die XPathExpression hardgecoded ist.
             * Sollte unwahrscheinlicherweise doch einmal etwas an der XPath-API geändert werden,
             * wäre das gesamte Programm sowieso erstmal unbrauchbar!
             */
            Logger.log(Level.WARNING, Constants.FATAL_ERROR_MESSAGE, e);
            throw new InternalError(Constants.FATAL_ERROR_MESSAGE);
        }
    }
}