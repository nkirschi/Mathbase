/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.topictree;

import de.apian.mathbase.gui.FillerPane;
import de.apian.mathbase.gui.HelpWindow;
import de.apian.mathbase.gui.MainPane;
import de.apian.mathbase.gui.content.ContentPane;
import de.apian.mathbase.gui.dialog.ErrorAlert;
import de.apian.mathbase.gui.dialog.TitleDialog;
import de.apian.mathbase.gui.dialog.WarningAlert;
import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import de.apian.mathbase.util.Logging;
import de.apian.mathbase.xml.TitleCollisionException;
import de.apian.mathbase.xml.TopicTreeController;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;
import java.util.logging.Level;

/**
 * GUI-Repräsentation des Themenbaums.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class TopicTreeView extends TreeView<String> {

    /**
     * Basisanzeigefläche
     *
     * @since 1.0
     */
    private MainPane mainPane;

    /**
     * Konstruktion eines Themenbaums
     *
     * @param mainPane Basisanzeigefläche
     * @since 1.0
     */
    public TopicTreeView(MainPane mainPane) {
        this.mainPane = mainPane;

        setRoot(new TreeItem<>());
        setShowRoot(false);
        setEditable(true);

        initSelectionBehavior();
        initContextMenu();

        setCellFactory(param -> new DraggableTreeCell(mainPane));
    }

    /**
     * Füllen des Themenbaums
     *
     * @since 1.0
     */
    public void build() {
        build(getRoot());
    }

    /**
     * Rekursives Füllen ab der Wurzel
     *
     * @param parent Jeweiliger Elternknoten im Themenbaum
     * @since 1.0
     */
    private void build(TreeItem<String> parent) {
        for (String s : TopicTreeController.getInstance().getChildren(parent.getValue())) {
            TreeItem<String> child = new TreeItem<>(s);
            parent.getChildren().add(child);
            build(child);
        }
    }

    /**
     * Konstruktion eines gefilterten Themenbaums
     *
     * @param key Suchbegriff
     * @return gefilterter Themenbaum
     * @since 1.0
     */
    public TreeView<String> filter(String key) {
        TopicTreeView treeView = new TopicTreeView(mainPane);
        filter(getRoot(), treeView.getRoot(), key);
        return treeView;
    }

    /**
     * Rekursive Konstruktion eines gefilterten Baums ab der Wurzel
     *
     * @param originalParent Elternknoten im originalen Baum
     * @param filteredParent Elternknoten im gefilterten Baum
     * @param key            Suchbegriff
     * @since 1.0
     */
    private void filter(TreeItem<String> originalParent, TreeItem<String> filteredParent, String key) {
        for (TreeItem<String> originalChild : originalParent.getChildren()) {
            if (subtreeContainsIgnoreCase(originalChild, key)) {
                TreeItem<String> filteredChild = new TreeItem<>(originalChild.getValue());
                filteredChild.setExpanded(true);
                filteredParent.getChildren().add(filteredChild);
                filter(originalChild, filteredChild, key);
            }
        }
    }

    /**
     * Prüfung eines Teilbaums auf enthalten eines Suchbegriffes
     *
     * @param parent Elternknoten
     * @param key    Suchbegriff
     * @return Boolesche Aussage, ob der Suchbegriff enthalten ist
     * @since 1.0
     */
    private boolean subtreeContainsIgnoreCase(TreeItem<String> parent, String key) {
        if (parent.getValue().toLowerCase().contains(key.toLowerCase()))
            return true;
        for (TreeItem<String> child : parent.getChildren())
            if (child.getValue().toLowerCase().contains(key.toLowerCase()) || subtreeContainsIgnoreCase(child, key))
                return true;
        return false;
    }

    /**
     * Initialisierung des Auswahlverhaltens
     *
     * @since 1.0
     */
    private void initSelectionBehavior() {
        getSelectionModel().select(null);
        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        getSelectionModel().selectedItemProperty().addListener((observable, oldItem, newItem) -> {
            String title = newItem != null ? newItem.getValue() : null;
            Node node = title != null ? new ContentPane(title, mainPane) : new FillerPane();
            mainPane.setContent(node);
        });

        // Wenn auf leeren Platz im Themenbaum geclickt wird, soll nichts ausgewählt sein
        addEventHandler(MouseEvent.MOUSE_CLICKED, a -> {
            Node node = a.getPickResult().getIntersectedNode();
            if (node instanceof TreeCell && ((TreeCell) node).getText() == null)
                getSelectionModel().select(null);
        });
    }

    /**
     * Initiierung eines nigelnagelschnieken Kontextmenüs (mit Senf)
     *
     * @since 1.0
     */
    private void initContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem addItem = new MenuItem(Constants.BUNDLE.getString("add"));
        addItem.setGraphic(new ImageView(Images.getInternal("icons_x16/add.png")));
        addItem.setOnAction(a -> addUnderSelected());

        MenuItem renameItem = new MenuItem(Constants.BUNDLE.getString("rename"));
        renameItem.setGraphic(new ImageView(Images.getInternal("icons_x16/rename.png")));
        renameItem.setOnAction(a -> renameSelected());

        MenuItem removeItem = new MenuItem(Constants.BUNDLE.getString("remove"));
        removeItem.setGraphic(new ImageView(Images.getInternal("icons_x16/remove.png")));
        removeItem.setOnAction(a -> removeSelected());

        MenuItem expandItem = new MenuItem(Constants.BUNDLE.getString("expand_all"));
        expandItem.setGraphic(new ImageView(Images.getInternal("icons_x16/expand.png")));
        expandItem.setOnAction(a -> setExpandedAll(getRoot(), true));

        MenuItem collapseItem = new MenuItem(Constants.BUNDLE.getString("collapse_all"));
        collapseItem.setGraphic(new ImageView(Images.getInternal("icons_x16/collapse.png")));
        collapseItem.setOnAction(a -> setExpandedAll(getRoot(), false));

        MenuItem helpItem = new MenuItem(Constants.BUNDLE.getString("help"));
        helpItem.setGraphic(new ImageView(Images.getInternal("icons_x16/help.png")));
        helpItem.setOnAction(a -> new HelpWindow(mainPane).show());

        contextMenu.getItems().addAll(addItem, renameItem, removeItem, new SeparatorMenuItem(),
                expandItem, collapseItem, new SeparatorMenuItem(), helpItem);
        setContextMenu(contextMenu);
    }

    /**
     * Festlegung der Aktionen beim Hinzufügen eines Themas
     *
     * @see <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html">Lambda-Ausdrücke</a>
     * @since 1.0
     */
    private void addUnderSelected() {
        TitleDialog dialog = new TitleDialog(mainPane);
        dialog.setHeaderText(Constants.BUNDLE.getString("add_topic"));
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(title -> {
            TreeItem<String> selectedItem = getSelectionModel().getSelectedItem();
            if (selectedItem == null)
                selectedItem = getRoot();

            try {
                TopicTreeController.getInstance().addNode(title, selectedItem.getValue());
                TreeItem<String> newItem = new TreeItem<>(title);
                selectedItem.getChildren().add(newItem);
                selectedItem.getChildren().sort(Comparator.comparing(TreeItem::getValue));
                selectedItem.setExpanded(true);
            } catch (IOException | TitleCollisionException | TransformerException e) {
                Logging.log(Level.WARNING, "Knoten \"" + title + "\" konnte nicht erstellt werden.", e);
                new ErrorAlert(e).showAndWait();
            }
        });
    }

    /**
     * Festlegung der Aktionen beim Umbenennen eines Themas.
     *
     * @since 1.0
     */
    private void renameSelected() {
        TreeItem<String> selectedItem = getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, Constants.BUNDLE.getString("nothing_to_rename"));
            alert.setTitle(Constants.BUNDLE.getString("topic_management"));
            alert.setHeaderText(Constants.BUNDLE.getString("rename_topic"));
            alert.initOwner(mainPane.getScene().getWindow());
            alert.showAndWait();
            return;
        }

            TitleDialog dialog = new TitleDialog(mainPane);
            dialog.setHeaderText(Constants.BUNDLE.getString("rename_topic"));
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(title -> {
                try {
                    TopicTreeController.getInstance().renameNode(selectedItem.getValue(), title);
                    selectedItem.setValue(title);
                    mainPane.setContent(new ContentPane(title, mainPane));
                } catch (IOException | TransformerException e) {
                    Logging.log(Level.WARNING, "Knoten umbenennen fehlgeschlagen!", e);
                    new WarningAlert().showAndWait();
                }
            });

    }

    /**
     * Festlegung der Aktionen beim Löschen eines Themas.
     *
     * @see <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html">Lambda-Ausdrücke</a>
     * @since 1.0
     */
    private void removeSelected() {
        TreeItem<String> selectedItem = getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, Constants.BUNDLE.getString("nothing_to_remove"));
            alert.setTitle(Constants.BUNDLE.getString("topic_management"));
            alert.setHeaderText(Constants.BUNDLE.getString("remove_topic"));
            alert.initOwner(mainPane.getScene().getWindow());
            alert.showAndWait();
            return;
        }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, Constants.BUNDLE.getString("remove_topic_confirmation"),
                    ButtonType.YES, ButtonType.NO);
            alert.setTitle(Constants.BUNDLE.getString("topic_management"));
            alert.setHeaderText(Constants.BUNDLE.getString("remove_topic"));
            alert.initOwner(mainPane.getScene().getWindow());
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.YES) {
                try {
                    selectedItem.getParent().getChildren().remove(selectedItem);
                    TopicTreeController.getInstance().removeNode(selectedItem.getValue());
                    getSelectionModel().select(null);
                } catch (IOException | TransformerException e) {
                    Logging.log(Level.WARNING, "Knoten löschen fehlgeschlagen!", e);
                    new WarningAlert().showAndWait();
                }
            }

    }

    /**
     * Rekursives Setzen des Expansionswahrheitswertes aller Einträge des Teilbaums
     *
     * @param parent   Elternknoten des Teilbaums
     * @param expanded zu setzender Wert für die Expandiertheit der Einträge
     */
    private void setExpandedAll(TreeItem<String> parent, boolean expanded) {
        for (TreeItem<String> child : parent.getChildren()) {
            child.setExpanded(expanded);
            setExpandedAll(child, expanded);
        }
    }
}
