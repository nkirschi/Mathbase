/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.exc.NodeNotFoundException;
import de.apian.mathbase.exc.TitleCollisionException;
import de.apian.mathbase.xml.TopicTreeController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Optional;

/**
 * Sidebar auf der linken Seite
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class SidebarPane extends BorderPane {

    /**
     * Basisanzeigefläche
     *
     * @since 1.0
     */
    private BasePane mainPane;

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
     * Konstruktion der Sidebar
     *
     * @param mainPane            Basisanzeigefläche
     * @param topicTreeController Themenbaumkontrolleur
     * @since 1.0
     */
    public SidebarPane(BasePane mainPane, TopicTreeController topicTreeController) {
        this.mainPane = mainPane;
        this.topicTreeController = topicTreeController;

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        treeView = constructTree();
        scrollPane.setContent(treeView);
        setCenter(scrollPane);


        TextField searchField = new TextField();
        searchField.setPromptText("Suche...");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
        });
        BorderPane borderPane = new BorderPane(searchField);
        setBottom(borderPane);
        borderPane.setPadding(new Insets(4, 4, 4, 4));
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
        treeView.setCellFactory(param -> new DraggableTreeCell(mainPane, topicTreeController));
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
                } catch (NodeNotFoundException | IOException | TitleCollisionException e) {
                    e.printStackTrace();
                } catch (InternalError e) {
                    Window window = mainPane.getScene().getWindow();
                    window.hide();
                    Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.FINISH);
                    alert.setHeaderText("Fatal Error");
                    alert.initOwner(window);
                    Optional<ButtonType> opt = alert.showAndWait();
                    opt.ifPresent(buttonType -> System.exit(1));
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
                    } catch (NodeNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        contextMenu.getItems().addAll(addItem, removeItem);
        return contextMenu;
    }
}
