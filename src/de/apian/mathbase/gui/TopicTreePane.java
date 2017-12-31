/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.exc.NodeMissingException;
import de.apian.mathbase.exc.TitleCollisionException;
import de.apian.mathbase.xml.TopicTreeController;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

/**
 * Themenbaum in der Sidebar
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class TopicTreePane extends ScrollPane {

    /**
     * Basisanzeigefläche
     *
     * @since 1.0
     */
    private MainPane mainPane;

    /**
     * Themenbaumkontrolleur
     *
     * @since 1.0
     */
    private TopicTreeController topicTreeController;

    /**
     * GUI-Baumkomponente
     *
     * @since 1.0
     */
    private TreeView<String> treeView;

    /**
     * Konstruktion der Themenbaumscheibe
     *
     * @param mainPane            Basisanzeigefläche
     * @param topicTreeController Themenbaumkontrolleur
     */
    TopicTreePane(MainPane mainPane, TopicTreeController topicTreeController) {
        this.mainPane = mainPane;
        this.topicTreeController = topicTreeController;

        setFitToHeight(true);
        setFitToWidth(true);

        treeView = constructTree();
        setContent(treeView);
    }

    /**
     * Konstruktion des Themenbaums selbst
     *
     * @return Themenbaum als GUI-Komponente
     * @since 1.0
     */
    private TreeView<String> constructTree() {
        treeView = new TreeView<>();
        treeView.setShowRoot(false);
        treeView.setEditable(true);
        treeView.setRoot(new TreeItem<>());
        treeView.setContextMenu(createContextMenu());
        treeView.setCellFactory(param -> new DraggableTreeCell(topicTreeController));
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String title = newValue != null ? newValue.getValue() : null;
            ContentPane contentPane = new ContentPane(title, topicTreeController);
            mainPane.getItems().set(1, contentPane);
        });

        constructTree(treeView.getRoot());
        return treeView;
    }

    /**
     * Rekursive Konstruktion ab der ersten Ebene
     *
     * @param parent Jeweiliger Elternknoten im Themenbaum
     */
    private void constructTree(TreeItem<String> parent) {
        for (String s : topicTreeController.getChildren(parent.getValue())) {
            TreeItem<String> child = new TreeItem<>(s);
            parent.getChildren().add(child);
            constructTree(child);
        }
    }

    /**
     * Kreation des Kontextmenüs im Baum
     *
     * @return Nigelnagelschniekes Kontextmenü (mit Senf)
     */
    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem addItem = new MenuItem("Hinzufügen...");
        addItem.setOnAction(a -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.initOwner(mainPane.getScene().getWindow());
            dialog.setTitle("Themenverwaltung");
            dialog.setHeaderText("Neues Thema hinzufügen");
            dialog.setContentText("Titel:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(title -> {
                TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
                if (selectedItem == null)
                    selectedItem = treeView.getRoot();

                try {
                    topicTreeController.addNode(title, selectedItem.getValue());
                    TreeItem<String> newItem = new TreeItem<>(title);
                    selectedItem.getChildren().add(newItem);
                    selectedItem.setExpanded(true);
                } catch (NodeMissingException | IOException | TitleCollisionException e) {
                    e.printStackTrace();
                }
            });
        });

        MenuItem removeItem = new MenuItem("Entfernen");
        removeItem.setOnAction(a -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                PasswordDialog dialog = new PasswordDialog(mainPane);
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(pw -> {
                    try {
                        topicTreeController.removeNode(selectedItem.getValue());
                        selectedItem.getParent().getChildren().remove(selectedItem);
                        treeView.getSelectionModel().select(null);
                    } catch (NodeMissingException | IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        contextMenu.getItems().addAll(addItem, removeItem);
        return contextMenu;
    }
}
