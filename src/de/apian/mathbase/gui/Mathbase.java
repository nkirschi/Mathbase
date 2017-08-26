/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import de.apian.mathbase.util.Logger;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Hauptklasse des Programms mit umfassenden Kontrollmöglichkeiten.
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
     * Ghost-Konstruktor, der wegen JavaFX {@code public} -Sichtbarkeit benötigt
     *
     * @since 1.0
     */
    public Mathbase() {
        instance = this;
    }

    /**
     * Hauptmethode der Applikation
     *
     * @param args Kommandozeilenargumente
     * @since 1.0
     */
    public static void main(String[] args) {
        Logger.log(Level.INFO, "Das Programm wurde gestartet!");
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
     * Einstiegsmethode der JavaFX-Anwendung
     *
     * @param stage Vom Framework übergebenes Hauptfenster
     * @since 1.0
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        initStage();
        stage.show();
        /* TODO Das sollte nur kommen, sobald jemand was verändern möchte
           TODO Die Edit-Buttons sind einfach immer eingeblendet, aber solang kein gültiges pwd eingegeben wurde,
           TODO kann der User halt nix machen
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Passwort eingeben");
        dialog.setHeaderText("Administrator-Passwort");
        dialog.setContentText("Um mediale Inhalte bearbeiten zu können, müssen Sie sich mit dem Admin-Passwort authentifizieren");
        dialog.getDialogPane().getButtonTypes().setAll(new ButtonType("Benutzer", ButtonType.CANCEL.getButtonData()),
                new ButtonType("Admin", ButtonType.OK.getButtonData()));
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(pwd -> System.out.println(pwd.equals("123456")));
        */
    }

    /**
     * Methode zum Wechseln der aktuellen Szene zu einer gegebenen Komponente
     *
     * @param component Anzuzeigende {@code Component}
     * @since 1.0
     */
    void changeTo(Component component) {
        stage.setScene(new Scene(component.getParent()));
    }

    /**
     * Getter-Methode für die in der momentan auf der Bühne befindlichen Szene enthaltene Komponente
     *
     * @return aktuell im Hauptfenster angezeigte Szene
     * @since 1.0
     */
    Parent getRoot() {
        return stage.getScene().getRoot();
    }

    /**
     * Delegierte Methode zur Initialisierung des Hauptfensters
     *
     * @since 1.0
     */
    private void initStage() {
        stage.setTitle("Mathbase " + Constants.APP_VERSION);
        stage.setWidth(800);
        stage.setHeight(600);
        try {
            stage.getIcons().add(Images.fetch(Constants.IMAGE_ROOT + "icon.png", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setOnCloseRequest(e -> cleanUp());
        changeTo(MainView.getInstance());
    }

    /**
     * Routine bei Beendigung des Programms
     *
     * @since 1.0
     */
    private void cleanUp() {
        // Alerts ersetzen den JOptionPane-Dialog
        Alert alert = new Alert(Alert.AlertType.WARNING); // Nur ein Test :D
        alert.setTitle("Mathbase " + Constants.APP_VERSION);
        alert.setHeaderText("Eine Mitteilung...");
        alert.setContentText("Sie widerwärtiger Unhold!");
        alert.initOwner(stage);
        alert.initStyle(StageStyle.DECORATED);
        alert.showAndWait();
    }
}