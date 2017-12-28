/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import javafx.scene.control.*;

import java.util.Optional;

/**
 * Themenbaum in der Sidebar
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class TopicTreePane extends ScrollPane {
    private MainPane mainPane;
    private TreeView<String> treeView;

    TopicTreePane(MainPane mainPane) {
        this.mainPane = mainPane;

        treeView = new TreeView<>();
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

        ContextMenu contextMenu = createContextMenu();
        treeView.setContextMenu(contextMenu);

        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            mainPane.getItems().set(1, new ContentPane(newValue.getValue()));
        });
    }

    TreeView<String> getTreeView() {
        return treeView;
    }

    private ContextMenu createContextMenu() { // TODO Abbildung auf XML
        ContextMenu contextMenu = new ContextMenu();

        MenuItem addItem = new MenuItem("Hinzufügen...");
        addItem.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(title -> {
                TreeItem<String> newItem = new TreeItem<>(title);
                TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
                System.out.println(selectedItem);
                if (selectedItem == null)
                    treeView.getRoot().getChildren().add(newItem);
                else
                    selectedItem.getChildren().add(newItem);
            });
        });

        MenuItem removeItem = new MenuItem("Entfernen");
        removeItem.setOnAction(e -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null)
                selectedItem.getParent().getChildren().remove(selectedItem);
            treeView.getSelectionModel().select(null);
        });

        contextMenu.getItems().addAll(addItem, removeItem);
        return contextMenu;
    }
}
