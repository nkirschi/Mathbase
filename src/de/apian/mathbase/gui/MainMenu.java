/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * Hauptansicht des Programms mit einem BorderPane.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class MainMenu implements View {

    /**
     * Statische Instanzreferenz auf das Singleton <tt>MainMenu</tt>
     *
     * @since 1.0
     */
    protected static MainMenu instance;

    /**
     * Komponente dieser Ansicht
     */
    private BorderPane component;

    /**
     * Privater Singleton-Konstruktor
     */
    private MainMenu() {
        component = new BorderPane();
        BorderPane pane = new BorderPane();
        pane.setCenter(new ImageView(new Image(getClass().getResourceAsStream("/de/apian/mathbase/images/icon.png"))));
        changeContentTo(pane); // so sähe der Aufruf von außen aus...
        TreeView<String> treeView = new TreeView<>();
        treeView.setRoot(new TreeItem<>());
        treeView.setShowRoot(false);
        treeView.setEditable(true);
        TreeItem<String> pythagoras = new TreeItem<>("Satzgruppe des Pythagoras");
        pythagoras.setExpanded(true);
        pythagoras.getChildren().add(new TreeItem<>("Satz des Pythagoras"));
        pythagoras.getChildren().add(new TreeItem<>("Kathetensatz"));
        pythagoras.getChildren().add(new TreeItem<>("Höhensatz"));
        treeView.getRoot().getChildren().add(pythagoras);
        component.setLeft(treeView);
    }

    /**
     * Singleton-Instanzoperation
     *
     * @return einzige Instanz von <tt>MainMenu</tt>
     */
    public static MainMenu getInstance() {
        if (instance == null)
            instance = new MainMenu();
        return instance;
    }

    /**
     * Nützliche Methode für das Setzen des Angezeigten Hauptinhalts
     *
     * @param content Anzuzeigender Inhalt
     */
    public void changeContentTo(Pane content) {
        component.setCenter(content);
    }

    /**
     * Implementierung der Interface-Methode
     *
     * @return Enthaltene GUI-Komponente
     */
    @Override
    public BorderPane getComponent() {
        return component;
    }
}
