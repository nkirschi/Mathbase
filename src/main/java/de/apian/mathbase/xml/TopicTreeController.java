/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.xml;

import de.apian.mathbase.gui.dialog.ErrorAlert;
import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.FileUtils;
import de.apian.mathbase.util.Logging;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.TransformerException;
import java.io.BufferedWriter;
import java.io.File;
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
            Logging.log(Level.INFO, "Original-Datei \"" + ORIGINAL_PATH + "\" erfolgreich geladen");
        } catch (IOException e1) {
            Logging.log(Level.WARNING, "Original-Datei \"" + ORIGINAL_PATH + "\" konnte nicht geladen werden", e1);
            try { // Versuche im Fehlerfall die Backup-Datei wiederherzustellen
                Files.copy(Paths.get(BACKUP_PATH), Paths.get(ORIGINAL_PATH), StandardCopyOption.REPLACE_EXISTING);
                xmlHandler = new XmlFileHandler(ORIGINAL_PATH);
                Logging.log(Level.INFO, "Original-Datei \"" + ORIGINAL_PATH + "\" erfolgreich aus \""
                        + BACKUP_PATH + "\" wiederhergestellt und geladen");
            } catch (IOException e2) {
                Logging.log(Level.SEVERE, "Datei \"" + ORIGINAL_PATH + "\" konnte nicht aus Backup-Datei \""
                        + BACKUP_PATH + "\" wiederhergestellt werden", e2);

                // Schmeißt eine IOException, um den aufrufenden Klassen mitzuteilen,
                // dass die Datei nicht geladen werden konnte
                e2.initCause(e1);
                throw new IOException("Daten konnten nicht geladen werden! Kontaktieren Sie umgehend Ihren Systemadministrator!", e2);
            }
        }
    }

    /**
     * Speichern der Daten als XML-Datei im Pfad {@value #ORIGINAL_PATH} relativ zum Arbeitsverzeichnis.
     * Wird von den die XML-Datei bearbeitenden Methoden selbst aufgerufen.
     *
     * @throws TransformerException wenn das Speichern nicht erfolgreich war
     * @since 1.0
     */
    private void saveFile() throws IOException, TransformerException {
        try {
            xmlHandler.saveDocTo(ORIGINAL_PATH);
            Logging.log(Level.INFO, "Speichern von \"" + ORIGINAL_PATH + "\" erfolgreich abgeschlossen");
        } catch (IOException | TransformerException e) {
            Logging.log(Level.WARNING, "Fehler beim Speichern von \"" + ORIGINAL_PATH + "\"", e);
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
            Logging.log(Level.INFO, "Erstellen eines Backups von \"" + ORIGINAL_PATH + "\" in \"" + BACKUP_PATH
                    + "\" erfolgreich abgeschlossen");
        } catch (IOException e) {
            Logging.log(Level.WARNING, "Fehler beim Erstellen der Backupdatei \"" + BACKUP_PATH + "\"", e);
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
    public static void recreateFile() throws IOException {

        // Erstellen der XML-Datei
        Path xmlPath = Paths.get(ORIGINAL_PATH);
        if (xmlPath.toFile().exists()) {
            FileUtils.move(xmlPath, Paths.get(ORIGINAL_PATH + ".old"));
            Logging.log(Level.WARNING, "Existierende " + ORIGINAL_PATH + "-Datei gefunden " +
                    "und vor Neuerstellung umbenannt");
        }
        Files.createFile(xmlPath);
        try (BufferedWriter writer = Files.newBufferedWriter(xmlPath, Charset.forName("UTF-8"))) {
            writer.write(String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n\n" +
                    "<%s></%s>", TAG_ROOT, TAG_ROOT));
            Logging.log(Level.INFO, "Datei \"" + ORIGINAL_PATH + "\" erfolgreich neu erstellt");
        } catch (IOException e) {
            Logging.log(Level.SEVERE, "Datei \"" + ORIGINAL_PATH + "\" konnte nicht neu erstellt werden");
            // Schmeißt eine IOException, um den aufrufenden Klassen mitzuteilen,
            // dass die Datei nicht erstellt werden konnte; diese sollen dann weiter verfahren!
            throw e;
        }

        // Erstellen des Themenordners
        Path topicsPath = Paths.get(TOPICS_PATH);
        if (topicsPath.toFile().exists()) {
            FileUtils.move(topicsPath, Paths.get(TOPICS_PATH + ".old"));
            Logging.log(Level.WARNING, "Existierende " + TOPICS_PATH + "-Ordner gefunden " +
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
        String expr = "//" + TAG_NODE;
        NodeList nodeList = xmlHandler.getNodeList(expr);
        for (int i = 0; i < nodeList.getLength(); i++) {
            // Titel des Knotens
            String anotherTitle = "";
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
                anotherTitle = ((Element) node).getAttribute(ATTR_TITLE);
            // Leerer Titel (kann gar nicht sein, da wir davon ausgehen, dass alle Knoten durch addNode erzeugt wurden)
            if (title.isEmpty())
                continue;
            if (FileUtils.normalize(anotherTitle).equals(FileUtils.normalize(title)))
                exists = true;
        }
        Logging.log(Level.INFO, "Existenz von Knoten mit Titel \"" + title + "\" überprüft: " + exists);
        return exists;
    }

    /**
     * Suchen eines Knoten anhand seines Titels. Ist der Titel {@code NULL}, dann wird die Wurzel zurückgegeben.
     *
     * @param title Titel des Knoten
     * @return Der Knoten
     */
    private Node getNode(String title) {
        String expr = title != null ? "//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title + "']" : "//" + TAG_ROOT;
        Node node = xmlHandler.getNode(expr);

        if (node == null) {
            //Darf und wird nicht vorkommen. Sollte es doch -> Loggen und das Programm schließen
            NodeNotFoundException e = new NodeNotFoundException("Es wurde nach einem nicht vorhanden Knoten " +
                    "gefordert: \"" + title + "\"");
            new ErrorAlert(e).showAndWait();
            Logging.log(Level.SEVERE, Constants.FATAL_ERROR_MESSAGE, e);
            throw new InternalError(Constants.FATAL_ERROR_MESSAGE, e);
        }
        return node;
    }

    /**
     * Ermitteln des Pfads des Ordners eines bestimmten Knotens relativ zum Arbeitsverzeichnis
     *
     * @param title Titel des Knotens
     * @return Pfad des Ordners des Knotens relativ zum Arbeitsverzeichnis
     * @since 1.0
     */
    public String localizeFolder(String title) {
        String path = localizeFolder(getNode(title)) + File.pathSeparator;
        Logging.log(Level.INFO, "Pfad zum Ordner des Knotens \"" + title + "\" gefunden.");
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
        NodeList nodeList = xmlHandler.getNodeList(expr);
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
     * @throws IOException             wenn Erstellen des Ordners fehlschlägt
     * @throws TransformerException    wenn es einen Fehler beim Speichern der XML gab
     * @since 1.0
     */
    public void addNode(String title, String parent) throws TitleCollisionException, IOException, TransformerException {
        if (doesExist(title))
            throw new TitleCollisionException("Knoten \"" + title + "\" existiert bereits!");

        //Knoten wird unter dem gefundenen Elternknoten erzeugt
        addNode(title, getNode(parent));
        Logging.log(Level.INFO, String.format("Knoten \"%s\" unter %s eingefügt", title,
                parent == null ? "der Wurzel" : "\"" + parent + "\""));
    }

    /**
     * Erstellt einen neuen Knoten (sofern Titel nicht schon vergeben) unter einem als Objekt gegebenen Eltern-Knoten
     *
     * @param title  Titel des Knotens
     * @param parent Eltern-Knoten
     * @throws IOException          wenn Erstellen des Ordners fehlschlägt
     * @throws TransformerException wenn es einen Fehler beim Speichern der XML gab
     * @since 1.0
     */
    private void addNode(String title, Node parent) throws IOException, TransformerException {
        // Erstellung des Knotens
        Element element = xmlHandler.getDocument().createElement(TAG_NODE);
        element.setAttribute(ATTR_TITLE, title);

        //Fügt Knoten unter Beachtung der Alphabetischen Sortierung zum Elternknoten hinzu
        insertNodeAlphabetically(parent, element);
        Logging.log(Level.INFO, "Knoten \"" + title + "\" erfolgreich erstellt");

        // Erstellung des Ordners
        Path path = Paths.get(localizeFolder(element));
        try {
            Files.createDirectory(path);
            Logging.log(Level.INFO, "Ordner des Knotens \"" + title + "\" erfolgreich erstellt");
        } catch (IOException e) {
            // Fehlgeschlagen, ändere XML im Speicher zurück, dann brich ab
            parent.removeChild(element);
            throw e;
        }

        // Speichern der XML-Datei
        try {
            saveFile();
        } catch (TransformerException e) {
            // Fehlgeschlagen, ändere XML im Speicher zurück, lösche Ordner, dann brich ab
            parent.removeChild(element);
            try {
                FileUtils.delete(path);
            } catch (IOException e1) {
                //Fehlgeschlagen, lass (jetzt nicht mehr benötigten) Ordner in Ruhe und logge das Ganze
                e1.addSuppressed(e);
                Logging.log(Level.WARNING, "Ordner \"" + path.toString() + "\" ist nun unnötig, Löschen schlug " +
                        "allerdings fehl!", e1);
            }
            throw e;
        }
    }

    /**
     * Einfügen eines Knoten alphabetisch (nach den Titeln) unter einen bestimmten Elternknoten
     *
     * @param parent   Elternknoten
     * @param newChild Einzufügender Knoten
     */
    private void insertNodeAlphabetically(Node parent, Node newChild) {
        NodeList siblings = parent.getChildNodes(); //Zukünftige Geschwisterknoten des einzufügenden Knotens
        for (int i = 0; i < siblings.getLength(); i++) {
            Node sibling = siblings.item(i);
            if (sibling.getNodeType() == Node.ELEMENT_NODE && newChild.getNodeType() == Node.ELEMENT_NODE) {
                //Anderer Fall kann eigenlich nicht vorkommen
                String siblingTitle = ((Element) sibling).getAttribute(ATTR_TITLE);
                String newChildTitle = ((Element) newChild).getAttribute(ATTR_TITLE);
                if (siblingTitle.compareToIgnoreCase(newChildTitle) >= 0) {
                    parent.insertBefore(newChild, sibling);
                    break;
                }
            }
        }
    }

    /**
     * Verschieben eines Knotens unter einen anderen
     *
     * @param from Titel des zu verschiebenden Knotens
     * @param to   Titel des neuen Elternknotens (Darf nicht {@code from} sein). Wenn {@code NULL}, dann wird die Wurzel
     *             verwendet.
     * @throws TitleCollisionException wenn {@code from} die Wurzel {@value TAG_ROOT} ist
     *                                 oder {@code from} gleich {@code to} ist
     * @throws IOException             wenn Verschieben des Ordners fehlschlägt
     * @throws TransformerException    wenn es einen Fehler beim Speichern der XML gab
     * @since 1.0
     */
    public void moveNode(String from, String to) throws TitleCollisionException, IOException, TransformerException {
        if (from.equals(to))
            throw new TitleCollisionException("from und to dürfen nicht gleich sein!");

        // Verschieben des Knotens unter den Elternknoten
        moveNode(getNode(from), getNode(to));
        Logging.log(Level.INFO, String.format("Knoten \"%s\" unter %s verschoben", from,
                to == null ? "die Wurzel" : "\"" + to + "\""));
    }

    /**
     * Verschieben einen als Objekt gegebenen Knoten unter einen anderen als Objekt gegebenen Knoten
     *
     * @param node Zu verschiebender Knoten (Darf nicht Wurzel sein)
     * @param to   Neuer Elternknoten (Darf nicht {@code node} sein)
     * @throws TitleCollisionException wenn {@code node} die Wurzel {@value TAG_ROOT} ist
     * @throws IOException             wenn Verschieben des Ordners fehlschlägt
     * @throws TransformerException    wenn es einen Fehler beim Speichern der XML gab
     * @since 1.0
     */
    private void moveNode(Node node, Node to) throws TitleCollisionException, IOException, TransformerException {
        // Überprüfung auf invalide Parameter
        if (node.getNodeName().equals(TAG_ROOT))
            throw new TitleCollisionException("Knoten darf nicht die Wurzel \"" + TAG_ROOT + "\" sein!");

        Path oldPath = Paths.get(localizeFolder(node));
        Node from = node.getParentNode();
        from.removeChild(node);
        insertNodeAlphabetically(to, node);

        // Kopieren des Ordners
        Path newPath = Paths.get(localizeFolder(node));
        try {
            FileUtils.copy(oldPath, newPath);
        } catch (IOException e) {
            //Fehlgeschlagen, änder XML im Speicher zurück, dann brich ab
            to.removeChild(node);
            insertNodeAlphabetically(from, node);
            throw e;
        }

        // Speichern der XML-Datei
        try {
            saveFile();
        } catch (TransformerException e) {
            //Fehlgeschlagen, ändere XML im Speicher zurück, lösche kopierten Ordner, dann brich ab
            to.removeChild(node);
            insertNodeAlphabetically(from, node);
            try {
                FileUtils.delete(newPath);
            } catch (IOException e1) {
                //Fehlgeschlagen, lass (jetzt nicht mehr benötigten) Ordner in Ruhe und logge das Ganze
                e1.addSuppressed(e);
                Logging.log(Level.WARNING, "Ordner \"" + newPath.toString() + "\" ist nun unnötig, Löschen schlug " +
                        "allerdings fehl!", e1);
            }
            throw e;
        }

        // Lösche Originalen Ordner
        try {
            FileUtils.delete(oldPath);
        } catch (IOException e) {
            //Fehlgeschlagen, lass (jetzt nicht mehr benötigten) Ordner in Ruhe und logge das Ganze
            Logging.log(Level.WARNING, "Ordner \"" + newPath.toString() + "\" ist nun unnötig, Löschen schlug " +
                    "allerdings fehl!", e);
        }
    }

    /**
     * Entfernen eines bestimmten Knotens mitsamt seinem Ordner
     *
     * @param title Titel des zu entfernenden Knotens
     * @throws TransformerException  wenn es einen Fehler beim Speichern der XML gab
     * @throws IOException wenn Entfernen des Ordners fehlschlägt
     * @since 1.0
     */
    public void removeNode(String title) throws IOException, TransformerException {

        // Ermitteln des Knotens
        Node node = getNode(title);
        Node parentNode = node.getParentNode();
        Path path = Paths.get(localizeFolder(node));

        // Entfernen des Knotens
        parentNode.removeChild(node);

        // Speichern der XML-Datei
        try {
            saveFile();
        } catch (IOException | TransformerException e) {
            //Fehlgeschlagen, ändere XML im Speicher zurück, dann brich ab
            insertNodeAlphabetically(parentNode, node);
            throw e;
        }

        // Entfernen des Ordners
        try {
            FileUtils.delete(path);
            Logging.log(Level.INFO, "Knoten \"" + title + "\" erfolgreich entfernt");
        } catch (IOException e) {
            // Fehlgeschlagen, lass (jetzt nicht mehr benötigten) Ordner in Ruhe und Logge das Ganze
            Logging.log(Level.WARNING, "Ordner \"" + path.toString() + "\" ist nun unnötig, Löschen schlug " +
                    "allerdings fehl!", e);
        }
    }

    /**
     * Umbenennen eines Knotens.
     *
     * @param from Ursprünglicher Titel
     * @param to   neuer Titel
     * @throws IOException           wenn Umbennenen des Ordners fehlschlägt
     * @throws TransformerException  wenn es einen Fehler beim Speichern der XML gab
     * @since 1.0
     */
    public void renameNode(String from, String to) throws IOException, TransformerException {
        // Ermitteln des Knotens
        Node node = getNode(from);

        Path oldPath = Paths.get(localizeFolder(node));

        if (node.getNodeType() == Node.ELEMENT_NODE) //Anderer Fall kann normalerweise nicht eintreten ...
            ((Element) node).setAttribute(ATTR_TITLE, to);

        Path newPath = Paths.get(localizeFolder(node));
        // Kopieren des Ordners
        try {
            FileUtils.copy(oldPath, newPath);
        } catch (IOException e) {
            //Fehlgeschlagen, änder XML im Speicher zurück, dann brich ab
            if (node.getNodeType() == Node.ELEMENT_NODE) //Anderer Fall kann normalerweise nicht eintreten ...
                ((Element) node).setAttribute(ATTR_TITLE, to);
            throw e;
        }

        // Speichern der XML-Datei
        try {
            saveFile();
        } catch (TransformerException e) {
            //Fehlgeschlagen, ändere XML im Speicher zurück, lösche kopierten Ordner, dann brich ab
            if (node.getNodeType() == Node.ELEMENT_NODE) //Anderer Fall kann normalerweise nicht eintreten ...
                ((Element) node).setAttribute(ATTR_TITLE, to);
            try {
                FileUtils.delete(newPath);
            } catch (IOException e1) {
                //Fehlgeschlagen, lass (jetzt nicht mehr benötigten) Ordner in Ruhe und logge das Ganze
                e1.addSuppressed(e);
                Logging.log(Level.WARNING, "Ordner \"" + newPath.toString() + "\" ist nun unnötig, Löschen schlug "
                        + "allerdings fehl!", e1);
            }
            throw e;
        }

        // Lösche Originalen Ordner
        try {
            FileUtils.delete(oldPath);
        } catch (IOException e) {
            //Fehlgeschlagen, lass (jetzt nicht mehr benötigten) Ordner in Ruhe und logge das Ganze
            Logging.log(Level.WARNING, "Ordner \"" + newPath.toString() + "\" ist nun unnötig, Löschen schlug " +
                    "allerdings fehl!", e);
        }

        Logging.log(Level.INFO, "Titel des Knotens \"" + from + "\" zu \"" + to + "\" geändert");
    }

    /**
     * Ermitteln aller Inhalte eines bestimmten Knotens
     *
     * @param title Titel des betreffenden Knotens
     * @return {@code Content}-Array aller Inhalte des Knotens
     * @since 1.0
     */
    public Content[] getContents(String title) {
        String expr = "//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title + "']/" + TAG_CONTENT;
        NodeList nodeList = xmlHandler.getNodeList(expr);

        Content[] contents = new Content[nodeList.getLength()];
        for (int i = 0; i < contents.length; i++) {
            Node contentNode = nodeList.item(i);
            if (contentNode.getNodeType() == Node.ELEMENT_NODE) { //Anderer Fall kann normalerweise nicht eintreten ...
                Element contentElement = (Element) contentNode;
                contents[i] = new Content(Content.Type.forName(contentElement.getAttribute(ATTR_TYPE)),
                        contentElement.getAttribute(ATTR_PATH),
                        contentElement.getAttribute(ATTR_TITLE)
                );
            }
        }
        return contents;
    }

    /**
     * Hinzufügen eines Inhalts zu einem bestimmten Knoten.
     * Inhalte sind eindeutig identifizierbar über ihren Dateipfad.
     *
     * @param content Hinzuzufügender Inhalt mit ursprünglichem Dateipfad
     * @param parent  Titel des betreffenden Knotens
     * @throws IOException           wenn das Kopieren der Datei fehlschlägt
     * @throws TransformerException  wenn das Speichern der XML-Datei fehlschlägt
     * @since 1.0
     */
    public void addContent(Content content, String parent) throws IOException, TransformerException {
        Node parentNode = getNode(parent);

        //Finde benötigte Pfade from und to
        Path from = Paths.get(content.getPath());
        String fileExstension = FileUtils.getFileExtension(from); //Finde Dateiendung
        String newFileName = content.getTitle().isEmpty() ? content.getType().toString() : content.getTitle();
        String parentPath = localizeFolder(parentNode);
        Path to = Paths.get(parentPath, newFileName + fileExstension);
        for (int i = 0; to.toFile().exists(); i++) { //Finde iterativ einen geeigneten Dateinamen
            to = Paths.get(parentPath, newFileName + i + fileExstension);
        }

        //Erstelle Element-Objekt aus dem Content-Objekt und Hinzufügen zum Elternknoten
        Element contentElement = xmlHandler.getDocument().createElement(TAG_CONTENT);
        contentElement.setAttribute(ATTR_TYPE, content.getType().toString());
        contentElement.setAttribute(ATTR_PATH, to.getFileName().toString());
        contentElement.setAttribute(ATTR_TITLE, content.getTitle());
        parentNode.appendChild(contentElement);
        Logging.log(Level.INFO, content.toString() + " erfolgreich erstellt");

        //Kopieren der Datei
        try {
            FileUtils.copy(from, to);
            Logging.log(Level.INFO, "Datei von " + content.toString() + " erfolgreich kopiert");
        } catch (IOException e) {
            // Fehlgeschlagen, ändere XML im Speicher zurück, dann brich ab
            parentNode.removeChild(contentElement);
            throw e;
        }

        //Speichern der XML-Datei
        try {
            saveFile();
        } catch (TransformerException e) {
            // Fehlgeschlagen, ändere XML im Speicher zurück, lösche Datei, dann brich ab
            parentNode.removeChild(contentElement);
            try {
                FileUtils.delete(to);
            } catch (IOException e1) {
                //Fehlgeschlagen, lass (jetzt nicht mehr benötigten) Datei in Ruhe und logge das Ganze
                e1.addSuppressed(e);
                Logging.log(Level.WARNING, "Datei \"" + to.toString() + "\" ist nun unnötig, Löschen schlug " +
                        "allerdings fehl!", e1);
            }
            throw e;
        }
        Logging.log(Level.INFO, content.toString() + " unter dem Knoten \"" + parent + "\" eingefügt");
    }

    /**
     * Löschenn eines Inhalts
     *
     * @param parent  Titel des Elternknotens
     * @param content Zu entfernender Inhalt
     * @throws TransformerException wenn das Speichern der XML-Datei fehlschlägt
     * @throws IOException wenn Löschen der Datei fehlschlägt
     * @since 1.0
     */
    public void removeContent(Content content, String parent) throws TransformerException, IOException {
        //Oder möchtest du lieber den Pfad mitgeben? Denk halt du hast des Content-Objekt noch rumliegen
        Node parentNode = getNode(parent);
        Path filePath = Paths.get(localizeFolder(parentNode), content.getPath());
        String expr = "//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + parent + "']/" + TAG_CONTENT + "[@" + ATTR_PATH +
                "='" + content.getPath() + "']";
        Node contentNode = xmlHandler.getNode(expr);

        //Entferne Inhalt aus der XML
        parentNode.removeChild(contentNode);

        //Speichern der XML-Datei
        try {
            saveFile();
        } catch (TransformerException e) {
            //Fehlgeschlagen, ändere XML im Speicher zurück und brich ab
            parentNode.appendChild(contentNode);
            throw e;
        }

        //Löschen der Datei
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            //Fehlgeschlagen, lass (jetzt nicht mehr benötigten) Datei in Ruhe und logge das Ganze
            Logging.log(Level.WARNING, "Datei \"" + filePath.toString() + "\" ist nun unnötig, Löschen schlug " +
                    "allerdings fehl!", e);
        }

        Logging.log(Level.INFO, content.toString() + " wurde vom Knoten \"" + parent + "\" entfernt");
    }
}