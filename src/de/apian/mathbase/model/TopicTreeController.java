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
import org.w3c.dom.NamedNodeMap;
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
 * Laden, Bearbeiten und Speichern der XML-Datei,
 * welche die Struktur und Inhalte der Themen enthält.
 *
 * @author Benedikt Mödl
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class TopicTreeController {

    /**
     * Pfad des Topic-Ordners relativ zum Arbeitsverzeichnis. Hier werden die eigentlichen Dateien gespeichert
     *
     * @since 1.0
     */
    private static final String TOPICS_PATH = "topics";

    /**
     * {@code XMLFileHandler} der XML-Datei
     *
     * @see <a href="{@docRoot}/de/apian/mathbase/util/XMLFileHandler.html">XMLFileHandler</a>
     * @since 1.0
     */
    private XMLFileHandler xmlHandler;

    /**
     * Pfad der Originaldatei relativ zum Arbeitsverzeichnis
     *
     * @since 1.0
     */
    private static final String ORIGINAL_PATH = "topic_tree.xml";

    /**
     * Pfad des Backups relativ zum Arbeitsverzeichnis
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
     * Bezeichner eines Knotens in der XML-Datei
     *
     * @since 1.0
     */
    private final String TAG_NODE = "node";

    /**
     * Bezeichner eines Inhalts in der XML-Datei
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
     * Bezeichner des Attributs {@code path} (Dateipfad  relativ zum Ordner des Elternknoten) in der XML-Datei
     *
     * @since 1.0
     */
    private final String ATTR_PATH = "path";

    /**
     * Konstruktion des Kontrolleurs
     *
     * @throws IOException wenn das Laden der XML-Datei fehlschlägt oder der Topics-Ordner nicht existiert
     * @since 1.0
     */
    public TopicTreeController() throws IOException {
        loadFile();
    }

    /**
     * Laden der XML-Datei; bei Fehlschlag wird die Backupdatei geladen.
     * Ist dies auch nicht möglich, so bricht die Methode ab.
     *
     * @throws IOException wenn die Datei sowie die Backup-Datei nicht geladen werden konnten
     * @since 1.0
     */
    private void loadFile() throws IOException {
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
                throw new IOException("Keine Datei konnte geladen werden! Kontaktieren Sie Ihren Systemadministrator!");
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
    private void saveFile() throws IOException {
        try {
            xmlHandler.saveDocToXml(ORIGINAL_PATH);
            Logger.log(Level.INFO, "Speichern von \"" + ORIGINAL_PATH + "\" erfolgreich abgeschlossen");
        } catch (IOException e) {
            Logger.log(Level.WARNING, "Fehler beim Speichern von \"" + ORIGINAL_PATH + "\"", e);
            // Schmeiß eine IOException, um den aufrufenden Methoden mitzuteilen,
            // dass die Datei nicht gespeichert werden konnte
            throw e;
        }
    }

    /**
     * Erstellen eines Backups der originalen Datei im Pfad {@code BACKUP_PATH} relativ zum Arbeitsverzeichnis
     *
     * @throws IOException wenn das Erstellen und nicht erfolgreich war
     * @since 1.0
     */
    public void backupFile() throws IOException {
        try {
            Files.copy(Paths.get(ORIGINAL_PATH), Paths.get(BACKUP_PATH), StandardCopyOption.REPLACE_EXISTING);
            Logger.log(Level.INFO, "Erstellen eines Backups von \"" + ORIGINAL_PATH + "\" in \"" + BACKUP_PATH
                    + "\" erfolgreich abgeschlossen");
        } catch (IOException e) {
            Logger.log(Level.WARNING, "Fehler beim Erstellen der Backupdatei \"" + BACKUP_PATH + "\"", e);
            // Schmeiß eine IOException, um den aufrufenden Klassen mitzuteilen,
            // dass die Datei nicht gesichert werden konnte; diese sollen dann weiter verfahren!
            // TODO Errorhandling in den anderen Klassen implementieren
            throw e;
        }
    }

    /**
     * Neuerstellung der XML-Datei im Pfad {@value #ORIGINAL_PATH} und des Topic-Ordners im Pfad {@value #TOPICS_PATH}
     * relativ zum Arbeitsverzeichnis. Bereits existierende Dateien/Ordner werden mit der Endung .old erweitert.
     * <p>
     * Kann auch aufgerufen werden, wenn der {@code TopicTreeController} noch nicht instanziert wurde,
     * damit das Programm trotz Fehlen der XML-Datei + Backup funktionstüchtig bleibt.
     * </p>
     *
     * @throws IOException wenn das Erstellen nicht erfolgreich war
     * @since 1.0
     */
    public static void recreateFile() throws IOException {
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

        } catch (IOException e) {
            Logger.log(Level.SEVERE, "Datei \"" + ORIGINAL_PATH + "\" konnte nicht neu erstellt werden");
            // Schmeiß eine IOException, um den aufrufenden Klassen mitzuteilen,
            // dass die Datei nicht erstellt werden konnte; diese sollen dann weiter verfahren!
            // TODO Errorhandling in den anderen Klassen implementieren
            throw e;
        }
    }

    /**
     * Überprüfung auf Existenz eines Knoten mit bestimmtem Titel, da jeder Titel global einzigartig ist.
     * Daher sprechen wir von global uniqueness als fundamentale Bedingung, die nicht verletzt werden darf.
     *
     * @param title Zu prüfender Titel
     * @return ob ein Knoten mit diesem Titel existiert
     * @since 1.0
     */
    private boolean doesExist(String title) {
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
     * Ermitteln des Pfads des Ordners eines bestimmten Knotens relativ zum Arbeitsverzeichnis
     *
     * @param title Titel des Knotens
     * @return Pfad des Ordners des Knotens relativ zum Arbeitsverzeichnis
     * @throws NodeMissingException wenn der Knoten nicht exisiert
     * @since 1.0
     */
    public String localizeFolder(String title) throws NodeMissingException {
        try {
            NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title +
                    "']");

            if (nodeList.getLength() == 0) {
                throw new NodeMissingException("Knoten \"" + title + "\" konnte nicht gefunden werden!");
            }
            Node node = nodeList.item(0);
            String path = localizeFolder(node);
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
     * Rekursives Finden von Pfaden des Ordners eines Knoten
     *
     * @param node Der Ausgangsknoten
     * @return Pfad zum Ordner des Knotens ausgehend vom {@value TOPICS_PATH}-Ordner
     * @throws IllegalArgumentException wenn ein Knoten kein Element ist bzw. kein Titel-Attribut besitzt und damit
     *                                  eigentlich auch keiner "unserer" Knoten sein kann. Dazu muss schon einiges
     *                                  schieflaufen ...
     */
    private String localizeFolder(Node node) throws IllegalArgumentException {
        if (node == null)
            throw new IllegalArgumentException("Knoten ist null!");

        if (node.getNodeName().equals(TAG_ROOT))
            return TOPICS_PATH;

        //Bringe Titel des Knoten in Erfahrung
        String nodeTitle = "";
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            nodeTitle = ((Element) node).getAttribute(ATTR_TITLE);
        }
        if (nodeTitle.equals("")) {
            throw new IllegalArgumentException("Knoten, welcher kein Element ist bzw keinen Titel hat, gefunden! " +
                    "*PANIK*");
        }

        String path = "/" + FileUtils.normalize(nodeTitle);
        return localizeFolder(node.getParentNode()) + path;
    }

    /**
     * Ermitteln der Titel aller direkten Kind-Knoten eines bestimmten Knotens
     *
     * @param title Titel des Knotens
     * @return {@code String}-Array der Titel aller Kinder
     * @throws IllegalArgumentException wenn ein Knoten kein Element ist bzw. kein Titel-Attribut besitzt und damit
     *                                  eigentlich auch keiner "unserer" Knoten sein kann. Dazu muss schon einiges
     *                                  schieflaufen ...
     * @since 1.0
     */
    public String[] getChildren(String title) throws IllegalArgumentException {
        try {
            String expr = title != null ? "//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title + "']/" + TAG_NODE
                    : "//" + TAG_ROOT + "/" + TAG_NODE;
            NodeList nodeList = xmlHandler.getNodeListXPath(expr);
            String[] result = new String[nodeList.getLength()];
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                //Bringe Titel des Knoten in Erfahrung
                String nodeTitle = "";
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    nodeTitle = ((Element) node).getAttribute(ATTR_TITLE);
                }
                if (nodeTitle.equals("")) {
                    throw new IllegalArgumentException("Knoten, welcher kein Element ist bzw keinen Titel hat, " +
                            "gefunden! *PANIK*");
                }

                result[i] = nodeTitle;
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

    //TODO ALLE Inhalts-Operationen anpassen + impelmentieren
    /**
     * Ermitteln aller Inhalte eines bestimmten Knotens
     *
     * @param title Titel des betreffenden Knotens
     * @return {@code Content}-Array aller Inhalte des Knotens
     * @since 1.0
     */
    public Content[] getContents(String title) {
        try {
            String expr = "//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title + "']/" + TAG_CONTENT;
            NodeList nodeList = xmlHandler.getNodeListXPath(expr);

            Content[] contents = new Content[nodeList.getLength()];
            for (int i = 0; i < contents.length; i++) {
                NamedNodeMap attributes = nodeList.item(i).getAttributes();
                contents[i] = new Content(
                        Content.Type.forName(attributes.getNamedItem(ATTR_TYPE).getNodeValue()),
                        attributes.getNamedItem(ATTR_PATH).getNodeValue(),
                        attributes.getNamedItem(ATTR_PATH).getNodeValue()
                );
            }

            Logger.log(Level.INFO, "Inhalte des Knotens mit dem Titel \"" + title + "\" ermittelt");
            return contents;
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
     * Einfügen eines neuen Knotens (sofern Titel nicht schon vergeben) unter einem bestimmten Eltern-Knoten
     *
     * @param title  Titel des einzufügenden Knotens
     * @param parent Titel des gewünschten Elternknotens. Wenn {@code NULL}, dann wird die Wurzel verwendet.
     * @throws TitleCollisionException wenn bereits ein Knoten mit diesem Titel existiert
     * @throws NodeMissingException    wenn der Elternknoten nicht exisiert
     * @throws IOException             wenn Speichern der XML-Datei bzw Erstellen des Ordners fehlschlägt
     * @since 1.0
     */
    public void addNode(String title, String parent) throws TitleCollisionException, NodeMissingException, IOException {
        if (doesExist(title)) {
            throw new TitleCollisionException("Knoten \"" + title + "\" existiert bereits!");
        }

        try {
            //Eltern-Knoten wird herrausgesucht
            String expr = parent != null ? "//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + parent + "']" : "//" + TAG_ROOT;
            NodeList nodeList = xmlHandler.getNodeListXPath(expr);
            if (nodeList.getLength() == 0) {
                throw new NodeMissingException("Knoten / Wurzel konnte nicht gefunden werden!");
            }
            Node parentNode = nodeList.item(0);

            //Knoten wird unter dem gefundenen Elternknoten erzeugt
            addNode(title, parentNode);
            Logger.log(Level.INFO, "Knoten \"" + title + "\" wurde unter dem Knoten \"" + parent + "\" eingefügt");
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
     * Erstellt einen neuen Knoten (sofern Titel nicht schon vergeben) unter einem als Objekt gegebenen Eltern-Knoten
     *
     * @param title  Titel des Knotens
     * @param parent Eltern-Knoten
     * @throws IOException wenn Speichern der XML-Datei bzw Erstellen des Ordners fehlschlägt
     * @since 1.0
     */
    private void addNode(String title, Node parent) throws IOException {

        // Erstellung des Knotens und Hinzufügen zum Elternknoten
        Element element = xmlHandler.getDoc().createElement(TAG_NODE);
        element.setAttribute(ATTR_TITLE, title);
        parent.appendChild(element);
        Logger.log(Level.INFO, "Knoten \"" + title + "\" wurde erstellt.");

        // Erstellung des Ordners
        Path path = Paths.get(localizeFolder(element));
        Files.createDirectory(path);
        Logger.log(Level.INFO, "Ordner des Knotens \"" + title + "\" wurde erstellt.");

        // Speichern der XML-Datei
        saveFile();
    }

    /**
     * Verschieben eines Knotens unter einen anderen
     *
     * @param from Titel des zu verschiebenden Knotens
     * @param to   Titel des neuen Elternknotens (Darf nicht {@code from} sein). Wenn {@code NULL}, dann wird die Wurzel
     *             verwendet.
     * @throws NodeMissingException     wenn einer der beiden Knoten nicht existiert
     * @throws IllegalArgumentException wenn {@code from} die Wurzel {@value TAG_ROOT} ist
     *                                  oder {@code from} gleich {@code to} ist
     * @throws IOException              wenn Speichern der XML-Datei bzw Verschieben des Ordners fehlschlägt
     * @since 1.0
     */
    public void moveNode(String from, String to) throws NodeMissingException, IllegalArgumentException, IOException {
        if (from.equals(to)) {
            throw new IllegalArgumentException("from und to dürfen nicht gleich sein!");
        }
        try {
            // Ermitteln des Elternknotens
            Node parentNode;
            {
                String expr = to != null ? "//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + to + "']" : "//" + TAG_ROOT;
                NodeList nodeList = xmlHandler.getNodeListXPath(expr);
                if (nodeList.getLength() == 0) {
                    throw new NodeMissingException("Knoten / Wurzel konnte nicht gefunden werden!");
                }
                parentNode = nodeList.item(0);
            }

            // Ermitteln des zu verschiebenden Knotens
            Node node;
            {
                String expr = "//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + from + "']";
                NodeList nodeList = xmlHandler.getNodeListXPath(expr);
                if (nodeList.getLength() == 0) {
                    throw new NodeMissingException("Knoten \"" + from + "\" konnte nicht gefunden werden!");
                }
                node = nodeList.item(0);
            }

            // Verschieben des Knotens unter den Elternknoten
            moveNode(node, parentNode);
            Logger.log(Level.INFO, "Knoten \"" + from + "\" wurde unter den Knoten \"" + to + "\" verschoben");
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
     * Verschieben einen als Objekt gegebenen Knoten unter einen anderen als Objekt gegebenen Knoten
     *
     * @param from Zu verschiebender Knoten (Darf nicht Wurzel sein)
     * @param to   Neuer Elternknoten (Darf nicht {@code node} sein)
     * @throws IllegalArgumentException wenn {@code node} die Wurzel {@value TAG_ROOT} ist
     * @throws IOException              wenn Speichern der XML-Datei bzw Verschieben des Ordners fehlschlägt
     * @since 1.0
     */
    private void moveNode(Node from, Node to) throws IllegalArgumentException, IOException {
        // Überprüfung auf illegale Parameter
        if (from.getNodeName().equals(TAG_ROOT)) {
            throw new IllegalArgumentException("Zu verschiebender Knoten darf nicht die Wurzel \"" + TAG_ROOT
                    + "\" sein!");
        }
        Path oldPath = Paths.get(localizeFolder(from));

        // Verschieben des Knotens
        from.getParentNode().removeChild(from);
        to.appendChild(from);
        //Logger.log(Level.INFO, "Knoten verschoben!");

        // Verschieben des Ordners
        Path newPath = Paths.get(localizeFolder(from));
        FileUtils.move(oldPath, newPath);
        //Logger.log(Level.INFO, "Ordner verschoben!");

        // Speichern der XML-Datei
        saveFile();
    }

    /**
     * Entfernen eines bestimmten Knotens mitsamt seinem Ordner
     *
     * @param nodeTitle Titel des zu entfernenden Knotens
     * @throws NodeMissingException wenn der Knoten nicht existiert
     * @throws IOException          wenn es einen Fehler beim Entfernen gab
     * @since 1.0
     */
    public void removeNode(String nodeTitle) throws NodeMissingException, IOException {
        try {
            // Ermitteln des Knotens
            NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + nodeTitle
                    + "']");
            if (nodeList.getLength() == 0) {
                throw new NodeMissingException("Knoten \"" + nodeTitle + "\" konnte nicht gefunden werden!");
            }
            Node node = nodeList.item(0);
            Path path = Paths.get(localizeFolder(node));

            // Entfernen des Knotens
            node.getParentNode().removeChild(node);

            // Entfernen des Ordners
            FileUtils.delete(path);

            // Speichern der XML-Datei
            saveFile();
            Logger.log(Level.INFO, "Knoten \"" + nodeTitle + "\" wurde entfernt");
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