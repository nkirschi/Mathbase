/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.gui.topictree.TopicTreeView;
import de.apian.mathbase.util.Constants;
import de.apian.mathbase.xml.TopicTreeController;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.util.ResourceBundle;

/**
 * Sidebar mit dem Themenbaum.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class SidebarPane extends BorderPane {

    /**
     * Basisanzeigefläche.
     *
     * @since 1.0
     */
    private MainPane mainPane;

    /**
     * Themenbaumkontrolleur.
     *
     * @since 1.0
     */
    private TopicTreeController topicTreeController;

    /**
     * GUI-Baumkomponente.
     *
     * @since 1.0
     */
    private TopicTreeView treeView;

    /**
     * Konstruktion der Sidebar.
     *
     * @param mainPane            Basisanzeigefläche
     * @param topicTreeController Themenbaumkontrolleur
     * @since 1.0
     */
    public SidebarPane(MainPane mainPane, TopicTreeController topicTreeController) {
        this.mainPane = mainPane;
        this.topicTreeController = topicTreeController;

        setMinWidth(150);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        treeView = new TopicTreeView(mainPane, topicTreeController);
        treeView.build();
        scrollPane.setContent(treeView);
        setCenter(scrollPane);


        TextField searchField = new TextField();
        searchField.setPromptText(Constants.BUNDLE.getString("search"));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty())
                scrollPane.setContent(treeView);
            else
                scrollPane.setContent(treeView.filter(newValue));
        });
        BorderPane borderPane = new BorderPane(searchField);
        setBottom(borderPane);
        borderPane.setPadding(new Insets(4, 4, 4, 4));
    }
}
