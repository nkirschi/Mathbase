/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase;

import de.apian.mathbase.gui.MainPane;
import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import de.apian.mathbase.util.Logger;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Level;

/**
 * Hauptklasse des Programms.
 * <p>
 * Der Konstruktor sollte niemals explizit aufgerufen werden; ein auf diese Weise erzeugtes {@code Mathbase} -Objekt
 * besitzt keinen realen Nutzen, weil es keinerlei Referenz auf das Hauptfenster {@code stage}  hält,
 * die es einzig und allein durch die Erzeugung von JavaFX beim Programmstart höchstpersönlich erhält.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class Mathbase extends Application {

    /**
     * Statische Instanzreferenz auf das Singleton {@code Mathbase}
     *
     * @since 1.0
     */
    private static Mathbase instance;

    /**
     * Referenz auf das Hauptfenster {@code stage}
     *
     * @see <a href="https://docs.oracle.com/javase/8/javafx/api/javafx/stage/Stage.html">Stage</a>
     * @since 1.0
     */
    private Stage stage;

    /**
     * Ghost-Konstruktor, der wegen JavaFX {@code public}-Sichtbarkeit benötigt
     *
     * @since 1.0
     */
    public Mathbase() {
        instance = this;
    }

    /**
     * Einstiegspunkt der Applikation
     *
     * @param args Kommandozeilenargumente
     * @since 1.0
     */
    public static void main(String[] args) {
        Logger.log(Level.INFO, "Programm gestartet");
        launch(args); // ruft die statische Methode launch() auf, welche wiederum die Applikation startet
    }

    /**
     * Singleton-Instanzoperation
     *
     * @return Einzige Instanz von {@code Mathbase}
     * @since 1.0
     */
    static Mathbase getInstance() {
        return instance;
    }

    /**
     * Start der JavaFX-Anwendung
     *
     * @param stage Vom Framework übergebenes Hauptfenster
     * @since 1.0
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        initStage();
        stage.show();
    }

    /**
     * Initialisierung des Hauptfensters
     *
     * @since 1.0
     */
    private void initStage() {
        stage.setTitle("Mathbase " + Constants.APP_VERSION);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.getIcons().add(Images.getInternal("icon.png"));
        stage.setOnCloseRequest(a -> cleanUp());
        changeTo(new MainPane());
    }

    /**
     * Wechseln der aktuellen Szene
     *
     * @param parent Anzuzeigende {@code Komponente}
     * @since 1.0
     */
    private void changeTo(Parent parent) {
        stage.setScene(new Scene(parent));
    }

    /**
     * Aufräumen vor Beendigung des Programms
     *
     * @since 1.0
     */
    private void cleanUp() {
        stage.hide();
        System.exit(0);
    }
}