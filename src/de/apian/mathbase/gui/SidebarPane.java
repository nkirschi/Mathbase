/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import javafx.geometry.Insets;
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

        TopicTreePane topicTreePane = new TopicTreePane(mainPane);
        setCenter(topicTreePane);

        TextField searchField = new TextField();
        searchField.setPromptText("Suche...");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
        });
        BorderPane borderPane = new BorderPane(searchField);
        setBottom(borderPane);
        borderPane.setPadding(new Insets(4, 4, 4, 4));
    }
}
