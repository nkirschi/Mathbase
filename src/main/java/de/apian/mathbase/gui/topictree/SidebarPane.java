/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.topictree;

import de.apian.mathbase.gui.MainPane;
import de.apian.mathbase.gui.AboutWindow;
import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import de.apian.mathbase.xml.TopicTreeController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

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
     * GUI-Baumkomponente.
     *
     * @since 1.0
     */
    private TopicTreeView treeView;

    /**
     * Konstruktion der Sidebar.
     *
     * @param mainPane            Basisanzeigefläche
     * @since 1.0
     */
    public SidebarPane(MainPane mainPane) {
        this.mainPane = mainPane;
        setMinWidth(150);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        treeView = new TopicTreeView(mainPane);
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

        Button aboutButton = new Button("", new ImageView(Images.getInternal("icons_x16/info.png")));
        aboutButton.setOnAction(a -> new AboutWindow(getScene().getWindow()).show());

        BorderPane.setMargin(aboutButton, new Insets(0, 0, 0, 4));
        borderPane.setRight(aboutButton);
        BorderPane.setMargin(borderPane, new Insets(4, 4, 4, 4));
        setBottom(borderPane);
    }
}
