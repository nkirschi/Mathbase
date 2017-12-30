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
 * Laden, Bearbeiten und Speichern der XML-Datei,
 * welche die Struktur und Inhalte der Themen enthält.
 *
 * @author Benedikt Mödl
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
     * Bezeichner des Attributs {@code path} (Dateipfad einer Datei relativ zum Ordner des Elternknoten) in der
     * XML-Datei
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
    public TopicTreeController() throws IOException {
        loadFile();
    }

    /**
     * Lädt die XML-Datei. Ist dies nicht möglich wird die Backupdatei geladen, ist dies auch nicht möglich bricht die
     * Methode ab
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
        } catch (IOException e) {
            Logger.log(Level.WARNING, "Fehler beim Erstellen der Backupdatei \"" + BACKUP_PATH + "\"", e);
            // Schmeiß eine IOException, um den aufrufenden Klassen mitzuteilen,
            // dass die Datei nicht gesichert werden konnte; diese sollen dann weiter verfahren!
            // TODO Errorhandling in den anderen Klassen implementieren
            throw e;
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
    String getNodePath(String title) throws NodeMissingException {
        try {
            NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title +
                    "']");

            if (nodeList.getLength() == 0) {
                throw new NodeMissingException("Knoten \"" + title + "\" konnte nicht gefunden werden!");
            }
            Node node = nodeList.item(0);
            String path = getNodePath(node);
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
     * @throws IllegalArgumentException wenn ein Knoten kein Element ist bzw. kein Titel-Attribut besitzt und damit
     *                                 eigentlich auch keiner "unserer" Knoten sein kann. Dazu muss schon einiges
     *                                 schieflaufen ...
     */
    private String getNodePath(Node node) throws IllegalArgumentException {
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
            //TODO REMAKE
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
     * @throws IllegalArgumentException wenn ein Knoten kein Element ist bzw. kein Titel-Attribut besitzt und damit
     *                                  eigentlich auch keiner "unserer" Knoten sein kann. Dazu muss schon einiges
     *                                  schieflaufen ...
     * @since 1.0
     */
    public String[] getChildNodes(String title) throws IllegalArgumentException {
        try {
            NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title
                    + "']/" + TAG_NODE);
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
            Logger.log(Level.INFO, "Titel aller Kind-Knoten des Knotens mit dem Titel \"" + title
                    + "\" zurückgegeben");
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
     * @throws IllegalArgumentException wenn ein Knoten kein Element ist bzw. kein Titel-Attribut besitzt und damit
     *                                  eigentlich auch keiner "unserer" Knoten sein kann. Dazu muss schon einiges
     *                                  schieflaufen ...
     * @since 1.0
     */
    public String[] getTopNodes() throws IllegalArgumentException {
        try {
            NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_ROOT + "/" + TAG_NODE);
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

    /**
     * Erstellt einen neuen Knoten (sofern Titel nicht schon vergeben) unter einem als Objekt gegebenen Eltern-Knoten
     *
     * @param parentNode Eltern-Knoten
     * @param title      Titel des Knotens
     * @throws IOException             wenn Speichern der XML-Datei bzw Erstellen des Ordners fehlschlägt
     * @since 1.0
     */
    private void addNode(Node parentNode, String title) throws IOException {

        //Knoten wird erstellt und zum Elternknoten hinzugefügt
        Element element = xmlHandler.getDoc().createElement(TAG_NODE);
        element.setAttribute(ATTR_TITLE, title);
        parentNode.appendChild(element);
        Logger.log(Level.INFO, "Knoten \"" + title + "\" wurde erstellt.");

        //Ordner wird erstellt
        Path path = Paths.get(getNodePath(element));
        Files.createDirectory(path);
        Logger.log(Level.INFO, "Ordner des Knotens \"" + title + "\" wurde erstellt.");

        //XML-Datei wird gespeichert
        saveFile();
    }

    /**
     * Erstellt einen neuen Knoten (sofern Titel nicht schon vergeben) unter einem per Titel gegebenen Eltern-Knoten
     *
     * @param parentTitle Titel des Eltern-Knotens
     * @param title  Titel des Knotens
     * @throws TitleCollisionException wenn bereits ein Knoten mit diesem Titel existiert
     * @throws NodeMissingException    wenn der Elternknoten nicht exisiert
     * @throws IOException             wenn Speichern der XML-Datei bzw Erstellen des Ordners fehlschlägt
     * @since 1.0
     */
    public void addNode(String parentTitle, String title) throws TitleCollisionException, NodeMissingException,
            IOException {
        if (alreadyExists(title)) {
            throw new TitleCollisionException("Knoten \"" + title + "\" existiert bereits!");
        }
        try {
            //Eltern-Knoten wird herrausgesucht
            NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" +
                    parentTitle + "']");
            if (nodeList.getLength() == 0) {
                throw new NodeMissingException("Knoten \"" + parentTitle + "\" konnte nicht gefunden werden!");
            }
            Node parentNode = nodeList.item(0);

            //Knoten wird unter dem gefundenen Elternknoten erzeugt
            addNode(parentNode, title);
            Logger.log(Level.INFO, "Knoten \"" + title + "\" wurde unter dem Knoten \"" + parentTitle +
                    "\" eingefügt");
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
     * Erstellt einen neuen Knoten (sofern Titel nicht schon vergeben) unter der Wurzel {@value TAG_ROOT}
     *
     * @param title Titel des Knotens
     * @throws TitleCollisionException wenn bereits ein Knoten mit diesem Titel existiert
     * @throws NodeMissingException    wenn der Elternknoten nicht exisiert
     * @throws IOException             wenn Speichern der XML-Datei bzw Erstellen des Ordners fehlschlägt
     * @since 1.0
     */
    public void addNode(String title) throws TitleCollisionException, NodeMissingException,
            IOException {
        if (alreadyExists(title)) {
            throw new TitleCollisionException("Knoten \"" + title + "\" existiert bereits!");
        }
        try {
            //Wurzel wird herrausgesucht
            NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_ROOT);
            if (nodeList.getLength() == 0) {
                throw new NodeMissingException("Wurzel \"" + TAG_ROOT + "\" konnte nicht gefunden werden!! *PANIC*");
            }
            Node parentNode = nodeList.item(0);

            //Knoten wird unter dem gefundenen Elternknoten erzeugt
            addNode(parentNode, title);
            Logger.log(Level.INFO, "Knoten \"" + title + "\" wurde unter der Wurzel \"" + TAG_ROOT +
                    "\" eingefügt");
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
     * Verschiebt einen als Objekt gegebenen Knoten unter einen anderen als Objekt gegebenen Knoten
     *
     * @param newParent Der neue Elternknoten (Darf nicht {@code node} sein)
     * @param node      Zu verschiebender Knoten (Darf nicht Wurzel sein)
     * @throws IllegalArgumentException wenn {@code node} die Wurzel {@value TAG_ROOT} ist
     * @throws IOException              wenn Speichern der XML-Datei bzw Verschieben des Ordners fehlschlägt
     * @since 1.0
     */
    private void moveNode(Node newParent, Node node) throws IllegalArgumentException, IOException {
        //Überprüft auf illegale Parameter
        if (node.getNodeName().equals(TAG_ROOT)) {
            throw new IllegalArgumentException("Zu verschiebender Knoten darf nicht die Wurzel \"" + TAG_ROOT
                    + "\" sein!");
        }
        Path oldPath = Paths.get(getNodePath(node));

        //Verschiebt Knoten
        node.getParentNode().removeChild(node);
        newParent.appendChild(node);
        //Logger.log(Level.INFO, "Knoten verschoben!");

        //Verschiebe Ordner
        Path newPath = Paths.get(getNodePath(node));
        FileUtils.move(oldPath, newPath);
        //Logger.log(Level.INFO, "Ordner verschoben!");

        //XML-Datei wird gespeichert
        saveFile();
    }

    /**
     * Verschiebt einen per Titel gegebenen Knoten unter einen anderen per Titel gegebenen Knoten
     *
     * @param newParentTitle Der neue Elternknoten (Darf nicht {@code node} sein)
     * @param nodeTitle Titel des zu verschiebenden Knotens
     * @throws NodeMissingException     wenn einer der beiden Knoten nicht existiert
     * @throws IllegalArgumentException wenn {@code node} die Wurzel {@value TAG_ROOT} ist oder {@code newParent} gleich
     *                                  {@code node} ist
     * @throws IOException              wenn Speichern der XML-Datei bzw Verschieben des Ordners fehlschlägt
     * @since 1.0
     */
    public void moveNode(String newParentTitle, String nodeTitle) throws NodeMissingException, IllegalArgumentException,
            IOException {
        try {
            //Elternknoten wird herrausgesucht
            Node parentNode;
            {
                NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" +
                        newParentTitle + "']");

                if (nodeList.getLength() == 0) {
                    throw new NodeMissingException("Knoten \"" + newParentTitle + "\" konnte nicht gefunden werden!");
                }
                parentNode = nodeList.item(0);
            }

            //Knoten wird herrausgesucht
            Node node;
            {
                NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" +
                        nodeTitle + "']");

                if (nodeList.getLength() == 0) {
                    throw new NodeMissingException("Knoten \"" + nodeTitle + "\" konnte nicht gefunden werden!");
                }
                node = nodeList.item(0);
            }

            //Knoten wird unter den gefundenen Elternknoten geschoben
            moveNode(parentNode, node);
            Logger.log(Level.INFO, "Knoten \"" + nodeTitle + "\" wurde unter den Knoten \"" + newParentTitle +
                    "\" verschoben");
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
     * Verschiebt einen per Titel gegebenen Knoten unter die Wurzel {@value TAG_ROOT}
     *
     * @param nodeTitle Titel des zu verschiebenden Knotens
     * @throws NodeMissingException     wenn einer der beiden Knoten nicht existiert
     * @throws IllegalArgumentException wenn {@code node} die Wurzel {@value TAG_ROOT} ist
     * @throws IOException              wenn Speichern der XML-Datei bzw Verschieben des Ordners fehlschlägt
     * @since 1.0
     */
    public void moveNode(String nodeTitle) throws NodeMissingException, IllegalArgumentException, IOException {
        try {
            //Wurzel wird herrausgesucht
            Node parentNode;
            {
                NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_ROOT);
                if (nodeList.getLength() == 0) {
                    throw new NodeMissingException("Wurzel \"" + TAG_ROOT + "\" konnte nicht gefunden werden!! " +
                            "*PANIC*");
                }
                parentNode = nodeList.item(0);
            }

            //Knoten wird herrausgesucht
            Node node;
            {
                NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" +
                        nodeTitle + "']");

                if (nodeList.getLength() == 0) {
                    throw new NodeMissingException("Knoten \"" + nodeTitle + "\" konnte nicht gefunden werden!");
                }
                node = nodeList.item(0);
            }

            //Knoten wird unter den gefundenen Elternknoten geschoben
            moveNode(parentNode, node);
            Logger.log(Level.INFO, "Knoten \"" + nodeTitle + "\" wurde unter die Wurzel \"" + TAG_ROOT +
                    "\" verschoben");
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
     * Entfernt einen per Titel gegebenen Knoten und seinen Ordner
     *
     * @param nodeTitle Titel des zu entfernenden Knotens
     * @throws NodeMissingException wenn der Knoten nicht existiert
     * @throws IOException          wenn es einen Fehler beim Entfernen gab
     * @since 1.0
     */
    public void removeNode(String nodeTitle) throws NodeMissingException, IOException {
        try {
            //Knoten wird herrausgesucht
            NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + nodeTitle
                    + "']");
            if (nodeList.getLength() == 0) {
                throw new NodeMissingException("Knoten \"" + nodeTitle + "\" konnte nicht gefunden werden!");
            }
            Node node = nodeList.item(0);
            Path path = Paths.get(getNodePath(node));

            //Knoten wird entfernt
            node.getParentNode().removeChild(node);

            //Ordner wird entfernt
            Files.deleteIfExists(path); //TODO gucken ob dat auch für nicht leere Ordner funzt, wenn nein muss Ersatz her!!!
            //TODO Manche sagen nämlich ja, manche nein, Java halt :\

            // TODO Nein, funzt es nicht. Stattdessen kassiere ich eine kolossale DirectoryNotEmptyException o_O

            //XML-Datei wird gespeichert
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