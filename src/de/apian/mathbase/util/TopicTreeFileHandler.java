/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.util;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

import static de.apian.mathbase.util.Logger.log;

/**
 * Utility-Klasse zum Laden, Bearbeiten und Speichern der XML-Dateien, welche die Struktur und Inhalte der Themen enthalten.
 *
 * @author Benedikt Mödl
 * @version 1.0
 * @since 1.0
 */
public class TopicTreeFileHandler {

    /**
     * Statische Instanzreferenz auf das Singleton {@code TopicTreeFileHandler}
     *
     * @since 1.0
     */
    private static TopicTreeFileHandler instance;

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
    private static final String ORIGINAL_FILEPATH = "topictree.xml";

    /**
     * Pfad der Backup-Datei relativ zum Arbeitsverzeichnis
     *
     * @since 1.0
     */
    private static final String BACKUP_FILEPATH = "topictree.xml.bak";

    /**
     * Bezeichner der Wurzel in der XML-Datei
     *
     * @since 1.0
     */
    private static final String TAG_ROOT = "topictree";

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
     * Privater Singleton-Konstruktor
     *
     * @throws IOException wenn die Datei sowie die Backup-Datei nicht geladen werden konnten
     * @since 1.0
     */
    private TopicTreeFileHandler() throws IOException {
        //Versucht zuerst die Original-Datei zu laden
        try {
            xmlHandler = new XMLFileHandler(ORIGINAL_FILEPATH);
            log(Level.INFO, "Original-Datei \"" + ORIGINAL_FILEPATH + "\" erfolgreich geladen");
        } catch (IOException e1) {
            log(Level.WARNING, "Konnte Original-Datei \"" + ORIGINAL_FILEPATH + "\" nicht laden", e1);
            try {
                //Versucht die Backup-Datei wiederherzustellen
                Files.copy(Paths.get(BACKUP_FILEPATH), Paths.get(ORIGINAL_FILEPATH), StandardCopyOption.REPLACE_EXISTING);
                xmlHandler = new XMLFileHandler(ORIGINAL_FILEPATH);
                log(Level.INFO, "Original-Datei \"" + ORIGINAL_FILEPATH + "\" erfolgreich aus \""
                        + BACKUP_FILEPATH + "\" wiederhergestellt und geladen");
            } catch (IOException e2) {
                log(Level.SEVERE, "Datei \"" + ORIGINAL_FILEPATH + "\" konnte nicht aus Backup-Datei \""
                        + BACKUP_FILEPATH + "\" wiederhergestellt werden", e2);

                //Wirf IOException, um den aufrufenden Klassen mitzuteilen, dass die Datei nicht geladen werden konnte.
                //Diese sollten dann weiter verfahren! TODO Errorhandling in den anderen Klassen implementieren
                throw new IOException("Laden der Datei nicht Möglich!");
            }
        }
    }

    /**
     * Singleton-Instanzoperation
     *
     * @return einzige Instanz von {@code TopicTreeFileHandler}
     * @throws IOException wenn die Datei sowie die Backup-Datei nicht geladen werden konnten und
     *                     deswegen keine Instanz von {@code TopicTreeFileHandler} erzeugt werden konnte
     * @since 1.0
     */
    public static TopicTreeFileHandler getInstance() throws IOException {
        if (instance == null)
            instance = new TopicTreeFileHandler();
        return instance;
    }

    /**
     * Speichert die Datei als XML-Datei im Pfad {@code ORIGINAL_FILEPATH} relativ zum Arbeitsverzeichnis
     *
     * @throws IOException wenn das Speichern nicht erfolgreich war
     * @since 1.0
     */
    public void save() throws IOException {
        try {
            xmlHandler.saveDocToXml(ORIGINAL_FILEPATH);
            log(Level.INFO, "Speichern von \"" + ORIGINAL_FILEPATH + "\" erfolgreich abgeschlossen");
        } catch (IOException e) {
            log(Level.WARNING, "Fehler beim speichern von \"" + ORIGINAL_FILEPATH + "\"", e);
            //Wirft IOException, um den aufrufenden Klassen mitzuteilen, dass die Datei nicht gespeichert werden konnte
            //Diese sollten dann weiter verfahren! TODO Errorhandling in den anderen Klassen implementieren
            throw e;
        }
    }

    /**
     * Erstellt ein Backup der originalen Datei im Pfad {@code BACKUP_FILEPATH} relativ zum Arbeitsverzeichnis
     *
     * @throws IOException wenn das Erstellen und nicht erfolgreich war
     * @since 1.0
     */
    public void createBackup() throws IOException {
        try {
            Files.copy(Paths.get(ORIGINAL_FILEPATH), Paths.get(BACKUP_FILEPATH), StandardCopyOption.REPLACE_EXISTING);
            log(Level.INFO, "Erstellen eines Backups von \"" + ORIGINAL_FILEPATH + "\" in \"" + BACKUP_FILEPATH + "\" erfolgreich abgeschlossen");
        } catch (IOException e) {
            log(Level.WARNING, "Fehler beim Erstellen der Backupdatei \"" + BACKUP_FILEPATH + "\"", e);
            //Wirft IOException, um den aufrufenden Klassen mitzuteilen, dass das Backup nicht erstellt werden konnte
            //Diese sollten dann weiter verfahren! TODO Errorhandling in den anderen Klassen implementieren
            throw e;
        }
    }

    /**
     * Erstellt die XML-Datei neu im Pfad {@code ORIGINAL_FILEPATH} relativ zum Arbeitsverzeichnis.
     * <p>
     * Kann auch aufgerufen werden, wenn der {@code TopicTreeFileHandler} noch nicht instanziert wurde,
     * damit das Programm trotz Fehlen der XML-Datei + Backup funktionstüchtig bleibt.
     * </p>
     *
     * @throws IOException wenn das Erstellen und nicht erfolgreich war
     * @since 1.0
     */
    public static void createNewFile() throws IOException {
        try {
            File file = new File(ORIGINAL_FILEPATH);
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                    "<" + TAG_ROOT + "></" + TAG_ROOT + ">");
            writer.close();
            log(Level.INFO, "Datei \"" + ORIGINAL_FILEPATH + "\" wurde erfolgreich neu erstellt");
        } catch (IOException e) {
            log(Level.SEVERE, "Konnte Datei \"" + ORIGINAL_FILEPATH + "\" nicht neu erstellen");
            //Wirft IOException, um den aufrufenden Klassen mitzuteilen, dass die Datei nicht erstellt werden konnte
            //Diese sollten dann weiter verfahren! TODO Errorhandling in den anderen Klassen implementieren
            throw e;
        }
    }

    /**
     * Überprüft, ob bereits ein Knoten mit diesem Titel vorhanden ist.
     *
     * @param title Der zu überprüfende Titel
     * @return Ob ein Knoten mit diesem Titel existiert
     * @since 1.0
     */
    public boolean checkNodeTitle(String title) {
        boolean exists = false;
        try {
            NodeList nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title + "']");
            if (nodeList.getLength() > 0) exists = true;
            log(Level.INFO, "Existenz von Knoten mit Titel \"" + title + "\" überprüft: " + exists);
        } catch (XPathExpressionException e) {
            log(Level.WARNING, "Konnte Knoten-Titel \"" + title + "\" nicht überprüfen", e);
            //Sollte eigentlich nie vorkommen, da die Expression hier ja von uns definiert wurde
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
    public NodeList getContentList(String title) {
        NodeList nodeList = new EmptyNodeList();
        try {
            nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title + "']/" + TAG_CONTENT);
            log(Level.INFO, "Inhalte des Knotens mit dem Titel \"" + title + "\" zurückgegeben");
        } catch (XPathExpressionException e) {
            log(Level.WARNING, "Konnte Inhalte des Knotens mit Titel \"" + title + "\" nicht zurückgeben", e);
            //Sollte eigentlich nie vorkommen, da die Expression hier ja von uns definiert wurde
        }
        return nodeList;
    }

    /**
     * Gibt eine {@code NodeList} aller direkten Kind-Knoten eines bestimmten Knotens zurück
     *
     * @param title Der Titel des Knotens
     * @return Eine {@code Nodelist} aller direkten Kind-Knoten des Knotens
     * @since 1.0
     */
    public NodeList getNodeChildren(String title) {
        NodeList nodeList = new EmptyNodeList();
        try {
            nodeList = xmlHandler.getNodeListXPath("//" + TAG_NODE + "[@" + ATTR_TITLE + "='" + title + "']/" + TAG_NODE);
            log(Level.INFO, "Kind-Knoten des Knotens mit dem Titel \"" + title + "\" zurückgegeben");
        } catch (XPathExpressionException e) {
            log(Level.WARNING, "Konnte Kind-Knoten des Knotens mit Titel \"" + title + "\" nicht zurückgeben", e);
            //Sollte eigentlich nie vorkommen, da die Expression hier ja von uns definiert wurde
        }
        return nodeList;
    }
}

/**
 * Stellt ein leeres {@code NodeList}-Objekt dar.
 * <p>
 * Kann als standardmäßiges {@code NodeList}-Rückgabe-Objekt verwendet werden,
 * um eine {@code NullPointerException} zu vermeiden.
 * Dies setzt allerdings vorraus, dass alle Methoden, die evtl. dieses Objekt verarbeiten,
 * immer die Länge der Nodelist überprüfen.
 * </p>
 *
 * @author Benedikt Mödl
 * @version 1.0
 * @since 1.0
 */
class EmptyNodeList implements NodeList {
    /**
     * Leerer Konstruktor, damit überhaupt eine {@code EmptyNodeList} erzeugt werden kann
     *
     * @since 1.0
     */
    EmptyNodeList() {

    }

    /**
     * Implementierung der {@code item}-Methode des Interfaces {@code NodeList}.
     * Gibt immer {@code NULL} zurück, da dies ja eine leere {@code NodeList} sein soll.
     *
     * @return Immer null
     * @since 1.0
     */
    @Override
    public Node item(int index) {
        return null;
    }

    /**
     * Implementierung der {@code getLength}-Methode des Interfaces {@code NodeList}.
     * Gibt immer '0' zurück, da dies ja eine leere {@code NodeList} sein soll.
     *
     * @return immer '0'
     * @since 1.0
     */
    @Override
    public int getLength() {
        return 0;
    }
}
