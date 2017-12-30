/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.model.TopicTreeController;
import javafx.scene.control.SplitPane;

import java.io.IOException;

/**
 * Basisanzeigefläche der GUI
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class MainPane extends SplitPane {

    /**
     * Konstruktion der Basisanzeigefläche
     *
     * @since 1.0
     */
    public MainPane() {
        try {
            TopicTreeController topicTreeController = new TopicTreeController();
            SidebarPane sidebarPane = new SidebarPane(this, topicTreeController);
            sidebarPane.setMinWidth(225);
            getItems().addAll(sidebarPane, new ContentPane(null, topicTreeController));
            SplitPane.setResizableWithParent(sidebarPane, Boolean.FALSE);
        } catch (IOException e) { // TODO Fehlerbehandlung
            e.printStackTrace();
        }
    }
}
