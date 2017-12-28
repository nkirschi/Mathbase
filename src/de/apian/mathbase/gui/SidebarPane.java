/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * Sidebar auf der linken Seite
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class SidebarPane extends BorderPane {
    private MainPane mainPane;

    public SidebarPane(MainPane mainPane) {
        this.mainPane = mainPane;

        setCenter(new TopicTreePane(mainPane));

        TextField textField = new TextField();
        textField.setPromptText("Suche...");
        setBottom(textField);
    }
}
