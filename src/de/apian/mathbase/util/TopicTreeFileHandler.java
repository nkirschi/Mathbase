/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.util;

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
     * @see <a href="{@docRoot}/de/apian/mathbase/util/XMLFileHandler.html">Copyright</a>
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
    private static final String ROOT = "topictree";

    /**
     * Bezeichner der Knoten in der XML-Datei
     *
     * @since 1.0
     */
    private static final String NODE = "node";

    /**
     * Bezeichner der Inhalte in der XML-Datei
     *
     * @since 1.0
     */
    private static final String CONTENT = "content";

    /**
     * Privater Singleton-Konstruktor
     *
     * @throws IOException wenn die Datei sowie die Backup-Datei nicht geladen werden konnten
     * @since 1.0
     */
    private TopicTreeFileHandler() throws IOException{
        //Versucht zuerst die Original-Datei zu laden
        try {
            xmlHandler = new XMLFileHandler(ORIGINAL_FILEPATH);
            log(Level.INFO, "Original-Datei \"" + ORIGINAL_FILEPATH + "\" erfolgreich geladen");
        } catch (IOException e1){
            log(Level.WARNING, "Konnte Original-Datei \"" + ORIGINAL_FILEPATH + "\" nicht laden", e1);
            try {
                //Versucht die Backup-Datei wiederherzustellen
                Files.copy(Paths.get(BACKUP_FILEPATH), Paths.get(ORIGINAL_FILEPATH), StandardCopyOption.REPLACE_EXISTING);
                xmlHandler = new XMLFileHandler(ORIGINAL_FILEPATH);
                log(Level.INFO, "Original-Datei \"" + ORIGINAL_FILEPATH + "\" erfolgreich aus \""
                        + BACKUP_FILEPATH + "\" wiederhergestellt und geladen");
            } catch (IOException e2){
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
     *         deswegen keine Instanz von {@code TopicTreeFileHandler} erzeugt werden konnte
     * @since 1.0
     */
    public static TopicTreeFileHandler getInstance() throws IOException{
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
    public void save() throws IOException{
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
     * Erstellt die XML-Datei neu im Pfad {@code ORIGINAL_FILEPATH} relativ zum Arbeitsverzeichnis
     * <p>
     * Kann auch aufgerufen werden, wenn der {@code TopicTreeFileHandler} noch nicht instanziert wurde,
     * damit das Programm trotz Fehlen der XML-Datei + Backup funktionstüchtig bleibt.
     *
     * @throws IOException wenn das Erstellen und nicht erfolgreich war
     * @since 1.0
     */
    public static void createNewFile() throws IOException{
        try {
            File file = new File(ORIGINAL_FILEPATH);
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                    "<" + ROOT + "></" + ROOT + ">");
            writer.close();
            log(Level.INFO, "Datei \"" + ORIGINAL_FILEPATH + "\" wurde erfolgreich neu erstellt");
        } catch (IOException e) {
            log(Level.SEVERE, "Konnte Datei \"" + ORIGINAL_FILEPATH + "\" nicht neu erstellen");
            //Wirft IOException, um den aufrufenden Klassen mitzuteilen, dass die Datei nicht erstellt werden konnte
            //Diese sollten dann weiter verfahren! TODO Errorhandling in den anderen Klassen implementieren
            throw e;
        }
    }
}
