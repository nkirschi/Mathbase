/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.xml.TopicTreeController;
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
     * Konstruktion der Sidebar
     *
     * @param mainPane            Basisanzeigefläche
     * @param topicTreeController Themenbaumkontrolleur
     * @since 1.0
     */
    public SidebarPane(MainPane mainPane, TopicTreeController topicTreeController) {
        this.mainPane = mainPane;
        this.topicTreeController = topicTreeController;

        TopicTreePane topicTreePane = new TopicTreePane(mainPane, topicTreeController);
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
