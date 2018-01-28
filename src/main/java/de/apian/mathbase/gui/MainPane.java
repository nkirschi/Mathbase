/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.gui.topictree.SidebarPane;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

/**
 * Basisanzeigefläche der GUI.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class MainPane extends SplitPane {

    /**
     * Konstruktion der Basisanzeigefläche.
     *
     * @since 1.0
     */
    public MainPane() {
        setDividerPositions(0.35);

        SidebarPane sidebarPane = new SidebarPane(this);
        SplitPane.setResizableWithParent(sidebarPane, Boolean.FALSE);

        getItems().addAll(sidebarPane, new FillerPane());
    }

    public void setContent(Node node) {
        getItems().set(1, node);
    }
}
