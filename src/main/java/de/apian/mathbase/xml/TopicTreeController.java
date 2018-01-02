/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.xml;

import de.apian.mathbase.util.FileUtils;
import de.apian.mathbase.util.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
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
     * {@code XmlFileHandler} der XML-Datei
     *
     * @see <a href="{@docRoot}/de/apian/mathbase/util/XmlFileHandler.html">XmlFileHandler</a>
     * @since 1.0
     */
    private XmlFileHandler xmlHandler;

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
    private static final String TAG_NODE = "node";

    /**
     * Bezeichner eines Inhalts in der XML-Datei
     *
     * @since 1.0
     */
    private static final String TAG_CONTENT = "content";

    /**
     * Bezeichner des Attributs {@code title} in der XML-Datei
     *
     * @since 1.0
     */
    private static final String ATTR_TITLE = "title";

    /**
     * Bezeichner des Attributs {@code type} (Typ der Inhalte) in der XML-Datei
     *
     * @since 1.0
     */
    private static final String ATTR_TYPE = "type";

    /**
     * Bezeichner des Attributs {@code path} (Dateipfad  relativ zum Ordner des Elternknoten) in der XML-Datei
     *
     * @since 1.0
     */
    private static final String ATTR_PATH = "path";

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
            xmlHandler = new XmlFileHandler(ORIGINAL_PATH);
            Logger.log(Level.INFO, "Original-Datei \"" + ORIGINAL_PATH + "\" erfolgreich geladen");
        } catch (IOException ex1) {
            Logger.log(Level.WARNING, "Original-Datei \"" + ORIGINAL_PATH + "\" konnte nicht geladen werden", ex1);
            if (!Files.exists(Paths.get(ORIGINAL_PATH))) {
                try {
                    recreateFile();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try { // Versuche im Fehlerfall die Backup-Datei wiederherzustellen
                Files.copy(Paths.get(BACKUP_PATH), Paths.get(ORIGINAL_PATH), StandardCopyOption.REPLACE_EXISTING);
                xmlHandler = new XmlFileHandler(ORIGINAL_PATH);
                Logger.log(Level.INFO, "Original-Datei \"" + ORIGINAL_PATH + "\" erfolgreich aus \""
                        + BACKUP_PATH + "\" wiederhergestellt und geladen");
            } catch (IOException ex2) {
                Logger.log(Level.SEVERE, "Datei \"" + ORIGINAL_PATH + "\" konnte nicht aus Backup-Datei \""
                        + BACKUP_PATH + "\" wiederhergestellt werden", ex2);

                // Schmeißt eine IOException, um den aufrufenden Klassen mitzuteilen,
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
            // Schmeißt eine IOException, um den aufrufenden Methoden mitzuteilen,
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
            // Schmeißt eine IOException, um den aufrufenden Klassen mitzuteilen,
            // dass die Datei nicht gesichert werden konnte; diese sollen dann weiter verfahren!
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
    public static void recreateFile() throws IOException { // TODO Exceptions hübsch machen und try-with-resources benutzen!!

        // Erstellen der XML-Datei
        Path xmlPath = Paths.get(ORIGINAL_PATH);
        if (Files.exists(xmlPath)) {
            FileUtils.move(xmlPath, Paths.get(ORIGINAL_PATH + ".old"));
            Logger.log(Level.WARNING, "Existierende " + ORIGINAL_PATH + "-Datei gefunden " +
                    "und vor Neuerstellung umbenannt");
        }
        Files.createFile(xmlPath);
        try (BufferedWriter writer = Files.newBufferedWriter(xmlPath, Charset.forName("UTF-8"))) {
            writer.write(String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n\n" +
                    "<!--\n  ~ Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.\n" +
                    "  ~ This product is licensed under the GNU General Public License v3.0.\n" +
                    "  ~ See LICENSE file for further information.\n  -->\n\n" +
                    "<%s></%s>", TAG_ROOT, TAG_ROOT));
            Logger.log(Level.INFO, "Datei \"" + ORIGINAL_PATH + "\" erfolgreich neu erstellt");
        } catch (IOException e) {
            Logger.log(Level.SEVERE, "Datei \"" + ORIGINAL_PATH + "\" konnte nicht neu erstellt werden");
            // Schmeißt eine IOException, um den aufrufenden Klassen mitzuteilen,
            // dass die Datei nicht erstellt werden konnte; diese sollen dann weiter verfahren!
            throw e;
        }

        // Erstellen des Themenordners
        Path topicsPath = Paths.get(TOPICS_PATH);
        if (Files.exists(topicsPath)) {
            FileUtils.move(topicsPath, Paths.get(TOPICS_PATH + ".old"));
            Logger.log(Level.WARNING, "Existierende " + TOPICS_PATH + "-Ordner gefunden " +
                    "und vor Neuerstellung umbenannt");
        }
        Files.createDirectory(topicsPath);
    }

    /**
     * Überprüfung auf Existenz eines Knoten mit bestimmtem Titel, da jeder Titel global einzigartig ist.
     * Daher sprechen wir von global uniqueness als fundamentale Bedingung, die nicht verletzt werden darf.
     *
     * @param title Zu prüfender Titel
     * @return ob ein Knoten mit diesem Titel existiert
     * @since 1.0
     */
    public boolean doesExist(String title) {
        boolean exists = false;
        String expr = "//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title + "']";
        NodeList nodeList = xmlHandler.generateNodeListFrom(expr);

        for (int i = 0; i < nodeList.getLength(); i++) {
            //Eigentlich sollte ja nur ein Knoten mit dem Titel da sein, aber Meckern ist nicht Sinn der Methode

            // Titel des Knotens
            String anotherTitle = "";
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
                anotherTitle = ((Element) node).getAttribute(ATTR_TITLE);
            // Leerer Titel (kann gar nicht sein, da wir davon ausgehen, dass alle Knoten durch addNode erzeugt wurden)
            if (title.isEmpty())
                continue;
            if (anotherTitle.equals(title))
                exists = true;
        }
        Logger.log(Level.INFO, "Existenz von Knoten mit Titel \"" + title + "\" überprüft: " + exists);
        return exists;
    }

    /**
     * Ermitteln des Pfads des Ordners eines bestimmten Knotens relativ zum Arbeitsverzeichnis
     *
     * @param title Titel des Knotens
     * @return Pfad des Ordners des Knotens relativ zum Arbeitsverzeichnis
     * @throws NodeNotFoundException wenn der Knoten nicht exisiert
     * @since 1.0
     */
    public String localizeFolder(String title) throws NodeNotFoundException {
        String expr = "//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title + "']";
        NodeList nodeList = xmlHandler.generateNodeListFrom(expr);
        if (nodeList.getLength() == 0)
            throw new NodeNotFoundException(String.format("Knoten \"%s\" konnte nicht gefunden werden!", title));
        Node node = nodeList.item(0);
        String path = localizeFolder(node) + "/";

        Logger.log(Level.INFO, "Pfad zum Ordner des Knotens \"" + title + "\" gefunden.");
        return path;
    }

    /**
     * Rekursives Finden von Pfaden des Ordners eines Knoten
     *
     * @param node Der Ausgangsknoten
     * @return Pfad zum Ordner des Knotens ausgehend vom {@value TOPICS_PATH}-Ordner
     */
    private String localizeFolder(Node node) {
        if (node == null)
            return "";

        if (node.getNodeName().equals(TAG_ROOT))
            return TOPICS_PATH;

        // Titel des Knotens
        String title = "";
        if (node.getNodeType() == Node.ELEMENT_NODE)
            title = ((Element) node).getAttribute(ATTR_TITLE);

        // Leerer Titel (kann gar nicht sein, da wir davon ausgehen, dass alle Knoten durch addNode erzeugt wurden)
        if (title.isEmpty())
            return "";

        String path = FileSystems.getDefault().getSeparator() + FileUtils.normalize(title);
        return localizeFolder(node.getParentNode()) + path;
    }

    /**
     * Ermitteln der Titel aller direkten Kind-Knoten eines bestimmten Knotens
     *
     * @param title Titel des Knotens
     * @return {@code String}-Array der Titel aller Kinder
     * @since 1.0
     */
    public String[] getChildren(String title) {

        String expr = title != null ? "//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title + "']/" + TAG_NODE
                : "//" + TAG_ROOT + "/" + TAG_NODE;
        NodeList nodeList = xmlHandler.generateNodeListFrom(expr);
        String[] result = new String[nodeList.getLength()];
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            // Titel des Knotens
            String nodeTitle = "";
            if (node.getNodeType() == Node.ELEMENT_NODE)
                nodeTitle = ((Element) node).getAttribute(ATTR_TITLE);


            // Leerer Titel (kann gar nicht sein, da wir davon ausgehen, dass alle Knoten durch addNode erzeugt wurden)
            if (nodeTitle.isEmpty())
                continue;

            result[i] = nodeTitle;
        }
        return result;

    }

    /**
     * Einfügen eines neuen Knotens (sofern Titel nicht schon vergeben) unter einem bestimmten Eltern-Knoten
     *
     * @param title  Titel des einzufügenden Knotens
     * @param parent Titel des gewünschten Elternknotens. Wenn {@code NULL}, dann wird die Wurzel verwendet.
     * @throws TitleCollisionException wenn bereits ein Knoten mit diesem Titel existiert
     * @throws NodeNotFoundException   wenn der Elternknoten nicht exisiert
     * @throws IOException             wenn Speichern der XML-Datei bzw Erstellen des Ordners fehlschlägt
     * @since 1.0
     */
    public void addNode(String title, String parent) throws TitleCollisionException, NodeNotFoundException, IOException {
        if (doesExist(title))
            throw new TitleCollisionException("Knoten \"" + title + "\" existiert bereits!");

        // Heraussuchen des Elternknotens
        String expr = parent != null ? "//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + parent + "']" : "//" + TAG_ROOT;
        NodeList nodeList = xmlHandler.generateNodeListFrom(expr);

        // Unmöglicher Fall
        if (nodeList.getLength() == 0)
            throw new NodeNotFoundException("Elternknoten konnte nicht gefunden werden");

        Node parentNode = nodeList.item(0);

        //Knoten wird unter dem gefundenen Elternknoten erzeugt
        addNode(title, parentNode);
        Logger.log(Level.INFO, String.format("Knoten \"%s\" unter %s eingefügt", title,
                parent == null ? "der Wurzel" : "\"" + parent + "\""));

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
        Element element = xmlHandler.getDocument().createElement(TAG_NODE);
        element.setAttribute(ATTR_TITLE, title);
        parent.appendChild(element);
        Logger.log(Level.INFO, "Knoten \"" + title + "\" erfolgreich erstellt");

        // Erstellung des Ordners
        Path path = Paths.get(localizeFolder(element));
        Files.createDirectory(path);
        Logger.log(Level.INFO, "Ordner des Knotens \"" + title + "\" erfolgreich erstellt");

        // Speichern der XML-Datei
        saveFile();
    }

    /**
     * Verschieben eines Knotens unter einen anderen
     *
     * @param from Titel des zu verschiebenden Knotens
     * @param to   Titel des neuen Elternknotens (Darf nicht {@code from} sein). Wenn {@code NULL}, dann wird die Wurzel
     *             verwendet.
     * @throws NodeNotFoundException   wenn einer der beiden Knoten nicht existiert
     * @throws TitleCollisionException wenn {@code from} die Wurzel {@value TAG_ROOT} ist
     *                                 oder {@code from} gleich {@code to} ist
     * @throws IOException             wenn Speichern der XML-Datei bzw Verschieben des Ordners fehlschlägt
     * @since 1.0
     */
    public void moveNode(String from, String to) throws NodeNotFoundException, TitleCollisionException, IOException {
        if (from.equals(to)) {
            throw new TitleCollisionException("from und to dürfen nicht gleich sein!");
        }

        // Ermitteln des Elternknotens
        Node parentNode;
        {
            String expr = to != null ? "//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + to + "']" : "//" + TAG_ROOT;
            NodeList nodeList = xmlHandler.generateNodeListFrom(expr);
            if (nodeList.getLength() == 0) {
                throw new NodeNotFoundException("Knoten / Wurzel konnte nicht gefunden werden!");
            }
            parentNode = nodeList.item(0);
        }

        // Ermitteln des zu verschiebenden Knotens
        Node node;
        {
            String expr = "//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + from + "']";
            NodeList nodeList = xmlHandler.generateNodeListFrom(expr);
            if (nodeList.getLength() == 0) {
                throw new NodeNotFoundException("Knoten \"" + from + "\" konnte nicht gefunden werden!");
            }
            node = nodeList.item(0);
        }

        // Verschieben des Knotens unter den Elternknoten
        moveNode(node, parentNode);
        Logger.log(Level.INFO, String.format("Knoten \"%s\" unter %s verschoben", from,
                to == null ? "die Wurzel" : "\"" + to + "\""));

    }

    /**
     * Verschieben einen als Objekt gegebenen Knoten unter einen anderen als Objekt gegebenen Knoten
     *
     * @param from Zu verschiebender Knoten (Darf nicht Wurzel sein)
     * @param to   Neuer Elternknoten (Darf nicht {@code node} sein)
     * @throws TitleCollisionException wenn {@code node} die Wurzel {@value TAG_ROOT} ist
     * @throws IOException             wenn Speichern der XML-Datei bzw Verschieben des Ordners fehlschlägt
     * @since 1.0
     */
    private void moveNode(Node from, Node to) throws TitleCollisionException, IOException {
        // Überprüfung auf invalide Parameter
        if (from.getNodeName().equals(TAG_ROOT))
            throw new TitleCollisionException("Knoten darf nicht die Wurzel \"" + TAG_ROOT + "\" sein!");

        Path oldPath = Paths.get(localizeFolder(from));

        // Verschieben des Knotens
        from.getParentNode().removeChild(from);
        to.appendChild(from);
        //Logger.log(Level.INFO, "Knoten verschoben");

        // Verschieben des Ordners
        Path newPath = Paths.get(localizeFolder(from));
        FileUtils.move(oldPath, newPath);
        //Logger.log(Level.INFO, "Ordner verschoben");

        // Speichern der XML-Datei
        saveFile();

    }

    /**
     * Entfernen eines bestimmten Knotens mitsamt seinem Ordner
     *
     * @param nodeTitle Titel des zu entfernenden Knotens
     * @throws NodeNotFoundException wenn der Knoten nicht existiert
     * @throws IOException           wenn es einen Fehler beim Entfernen gab
     * @since 1.0
     */
    public void removeNode(String nodeTitle) throws NodeNotFoundException, IOException {

        // Ermitteln des Knotens
        NodeList nodeList = xmlHandler.generateNodeListFrom("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + nodeTitle
                + "']");
        if (nodeList.getLength() == 0) {
            throw new NodeNotFoundException("Knoten \"" + nodeTitle + "\" konnte nicht gefunden werden!");
        }

        Node node = nodeList.item(0);
        Path path = Paths.get(localizeFolder(node));

        // Entfernen des Knotens
        node.getParentNode().removeChild(node);

        // Entfernen des Ordners
        FileUtils.delete(path);

        // Speichern der XML-Datei
        saveFile();
        Logger.log(Level.INFO, "Knoten \"" + nodeTitle + "\" erfolgreich entfernt");
    }

    /**
     * Umbenennen eines Knotens
     *
     * @param from Ursprünglicher Titel
     * @param to   neuer Titel
     * @throws NodeNotFoundException wenn kein Knoten mit diesem Titel existiert
     * @since 1.0
     */
    public void renameNode(String from, String to) throws NodeNotFoundException, IOException {
        // Ermitteln des Knotens
        String expr = "//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + from + "']";
        NodeList nodeList = xmlHandler.generateNodeListFrom(expr);

        if (nodeList.getLength() == 0) {
            throw new NodeNotFoundException("Knoten \"" + from + "\" konnte nicht gefunden werden!");
        }

        Node node = nodeList.item(0);
        //Ändern des Titels
        if (node.getNodeType() == Node.ELEMENT_NODE) //Anderer Fall kann normalerweise nicht eintreten ...
            ((Element) node).setAttribute(ATTR_TITLE, to);

        saveFile();
    }

    //TODO ALLE Inhalts-Operationen anpassen + implementieren

    /**
     * Ermitteln aller Inhalte eines bestimmten Knotens
     *
     * @param title Titel des betreffenden Knotens
     * @return {@code Content}-Array aller Inhalte des Knotens
     * @since 1.0
     */
    public Content[] getContents(String title) {

        String expr = "//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title + "']/" + TAG_CONTENT;
        NodeList nodeList = xmlHandler.generateNodeListFrom(expr);

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

    }
}