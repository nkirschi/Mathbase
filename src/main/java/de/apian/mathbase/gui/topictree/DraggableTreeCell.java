/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.topictree;

import de.apian.mathbase.Mathbase;
import de.apian.mathbase.gui.MainPane;
import de.apian.mathbase.xml.TitleCollisionException;
import de.apian.mathbase.xml.TopicTreeController;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.*;
import javafx.scene.paint.Color;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Comparator;

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
     * Aktuell gewählte Quell- und Zielknoten.
     *
     * @since 1.0
     */
    private static TreeItem<String> sourceItem, targetItem;

    /**
     * Hauptanzeigefläche
     *
     * @since 1.0
     */
    private MainPane mainPane;

    /**
     * Konstruktion des Themenbaumeintrages.
     *
     * @param mainPane Hauptanzeigefläche
     * @since 1.0
     */
    DraggableTreeCell(MainPane mainPane) {
        this.mainPane = mainPane;

        setOnDragDetected(this::dragDetected);
        setOnDragOver(this::dragOver);
        setOnDragExited(a -> setEffect(null));
        setOnDragDropped(this::dragDropped);
    }

    /**
     * Korrekte Aktualisierung des Themenbaumeintrags.
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
     * Aktionen beim Drücken der linken Maustaste und gleichzeitigem Bewegen des Mauszeigers.
     *
     * @param event Ziehereignis
     * @since 1.0
     */
    private void dragDetected(MouseEvent event) {
        sourceItem = getTreeItem();

        if (sourceItem != null) {
            Dragboard db = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(sourceItem.getValue());
            db.setContent(content);
        }
        event.consume();
    }

    /**
     * Aktionen beim Gedrückthalten der linken Maustaste und Ziehen über andere Elemente.
     *
     * @param event Ziehereignis
     * @since 1.0
     */
    private void dragOver(DragEvent event) {
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
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }

    /**
     * Aktionen beim Loslassen der linken Maustaste über einem Knoten.
     *
     * @param event Ziehereignis
     * @since 1.0
     */
    private void dragDropped(DragEvent event) {
        if (targetItem == null)
            targetItem = getTreeView().getRoot();

        if (Mathbase.authenticate(mainPane)) {
            try {
                TopicTreeController.getInstance().moveNode(sourceItem.getValue(), targetItem.getValue());
                sourceItem.getParent().getChildren().remove(sourceItem);
                targetItem.getChildren().add(sourceItem);
                targetItem.setExpanded(true);
                targetItem.getChildren().sort(Comparator.comparing(TreeItem::getValue));
            } catch (IOException | TitleCollisionException | TransformerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Prüfung auf Elternschaft eines bestimmten Knotens bezüglich eines anderen.
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