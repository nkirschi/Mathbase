/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import javafx.scene.control.SplitPane;

/**
 * Hauptanzeigefläche der GUI
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class MainPane extends SplitPane {
    public MainPane() {
        //setCenter(new ContentPane());
        //setLeft(new TopicTreePane(this));
        SidebarPane sidebarPane = new SidebarPane(this);
        sidebarPane.setMinWidth(225);
        getItems().addAll(sidebarPane, new ContentPane());
        SplitPane.setResizableWithParent(sidebarPane, Boolean.FALSE);
    }
}
