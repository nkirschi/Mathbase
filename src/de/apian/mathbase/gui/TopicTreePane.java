/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * Themenbaum in der Sidebar
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class TopicTreePane extends ScrollPane {
    private MainPane mainPane;

    TopicTreePane(MainPane mainPane) {
        this.mainPane = mainPane;

        TreeView<String> treeView = new TreeView<>();
        treeView.setRoot(new TreeItem<>());
        treeView.setShowRoot(false);
        treeView.setEditable(true);

        //TODO Implementieren
        TreeItem<String> pythagoras = new TreeItem<>("Satzgruppe des Pythagoras");
        pythagoras.setExpanded(true);
        pythagoras.getChildren().add(new TreeItem<>("Satz des Pythagoras"));
        pythagoras.getChildren().add(new TreeItem<>("Kathetensatz"));
        pythagoras.getChildren().add(new TreeItem<>("Höhensatz"));
        //treeView.getSelectionModel().getSelectedItem();
        treeView.getRoot().getChildren().add(pythagoras);
        setContent(treeView);
        setFitToHeight(true);
        setFitToWidth(true);
    }
}
