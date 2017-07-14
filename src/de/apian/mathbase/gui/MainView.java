/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * Hauptansicht des Programms mit einem BorderPane.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
class MainView implements Component {

    /**
     * Statische Instanzreferenz auf das Singleton <tt>MainMenu</tt>
     *
     * @since 1.0
     */
    private static MainView instance;

    /**
     * Komponente dieser Ansicht
     *
     * @since 1.0
     */
    private BorderPane parent;

    /**
     * Privater Singleton-Konstruktor
     *
     * @since 1.0
     */
    private MainView() {
        parent = new BorderPane();

        BorderPane pane = new BorderPane();
        try {
            pane.setCenter(new ImageView(Images.fetch(Constants.IMAGE_ROOT + "icon.png", true)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setContent(pane); // so sähe der Aufruf von außen aus...

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
        getParent().setLeft(treeView);
    }

    /**
     * Singleton-Instanzoperation
     *
     * @return einzige Instanz von <tt>MainMenu</tt>
     * @since 1.0
     */
    static MainView getInstance() {
        if (instance == null)
            instance = new MainView();
        return instance;
    }

    /**
     * Implementierung und Spezialisierung der Interface-Methode
     *
     * @return Enthaltene GUI-Komponente
     * @since 1.0
     */
    public BorderPane getParent() {
        return parent;
    }

    /**
     * Gettter-Methode für den momentan angezeigten Hauptinhalt
     *
     * @return Aktuell dargestellter Inhalt
     * @since 1.0
     */
    Parent getContent() {
        return (Parent) parent.getCenter();
    }

    /**
     * Nützliche Methode für das Setzen des angezeigten Hauptinhalts
     *
     * @param content Anzuzeigender Inhalt
     * @since 1.0
     */
    void setContent(Parent content) {
        parent.setCenter(content);
    }
}
