/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.topictree;

import de.apian.mathbase.gui.MainPane;
import de.apian.mathbase.gui.dialog.PasswordDialog;
import de.apian.mathbase.xml.NodeNotFoundException;
import de.apian.mathbase.xml.TitleCollisionException;
import de.apian.mathbase.xml.TopicTreeController;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;

/**
 * Ziehbarer Themenbaumeintrag für Drag and Drop.
 * <p>
 * Hier wird das Verhalten der Einträge beim Verschieben festgelegt.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class DraggableTreeCell extends TreeCell<String> {

    /**
     * Aktuell gewählte Quell- und Zielknoten
     *
     * @since 1.0
     */
    private static TreeItem<String> sourceItem, targetItem;

    /**
     * Konstruktion des Themenbaumeintrages
     *
     * @since 1.0
     */
    DraggableTreeCell(MainPane mainPane, TopicTreeController topicTreeController) {

        // Drücken der linken Maustaste und Bewegung des Mauszeigers
        setOnDragDetected(a -> {
            sourceItem = getTreeItem();

            if (sourceItem != null) {
                Dragboard db = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(sourceItem.getValue());
                db.setContent(content);
                System.out.println(sourceItem.getValue());
            }
            a.consume();
        });

        // Gedrückthalten der linken Maustaste
        setOnDragOver(a -> {
            targetItem = getTreeItem();
            if (targetItem == null)
                targetItem = getTreeView().getRoot();

            if (sourceItem.getParent() != targetItem && sourceItem != targetItem && !isChild(targetItem, sourceItem)) {
                if (targetItem.getValue() != null) {
                    InnerShadow shadow = new InnerShadow();
                    shadow.setOffsetX(1.0);
                    shadow.setColor(Color.web("#666666"));
                    shadow.setOffsetY(1.0);
                    setEffect(shadow);
                }
                a.acceptTransferModes(TransferMode.MOVE);
            }
            a.consume();
        });

        // Ende der Bewegung des Mauszeigers
        setOnDragExited(a -> setEffect(null));

        // Loslassen der linken Maustaste
        setOnDragDropped(a -> {
            if (targetItem == null)
                targetItem = getTreeView().getRoot();

            PasswordDialog dialog = new PasswordDialog(mainPane);
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(pw -> {
                try {
                    topicTreeController.moveNode(sourceItem.getValue(), targetItem.getValue());
                    sourceItem.getParent().getChildren().remove(sourceItem);
                    targetItem.getChildren().add(sourceItem);
                    targetItem.setExpanded(true);
                    targetItem.getChildren().sort(Comparator.comparing(TreeItem::getValue));
                } catch (NodeNotFoundException | IOException | TitleCollisionException | TransformerException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    /**
     * Korrekte Aktualisierung des Themenbaumeintrags
     *
     * @param item  zugehöriger Knoten im Themenbaum
     * @param empty Leerigkeit des Knotens
     * @since 1.0
     */
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty && item != null) {
            setText(item);
            setGraphic(getTreeItem().getGraphic());
        } else {
            setText(null);
            setGraphic(null);
        }
    }

    /**
     * Prüfung auf Elternschaft eines bestimmten Knotens bezüglich eines anderen
     *
     * @param child  mutmaßlicher Kindknoten
     * @param parent Ausgangsknoten
     * @return Elter?
     * @since 1.0
     */
    private boolean isChild(TreeItem<String> child, TreeItem<String> parent) {
        boolean isChild = false;
        for (TreeItem<String> item : parent.getChildren()) {
            if (item == child || isChild(child, item))
                isChild = true;
        }
        return isChild;
    }
}