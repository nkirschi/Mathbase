/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.topictree;

import de.apian.mathbase.gui.AboutWindow;
import de.apian.mathbase.gui.MainPane;
import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
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
     * Baumanzeigefläche.
     *
     * @since 1.0
     */
    private ScrollPane scrollPane;

    /**
     * Konstruktion der Sidebar.
     *
     * @param mainPane Basisanzeigefläche
     * @since 1.0
     */
    public SidebarPane(MainPane mainPane) {
        this.mainPane = mainPane;
        setMinWidth(150);

        scrollPane = initTreePane();
        setCenter(scrollPane);

        BorderPane bottomPane = initBottomPane();
        BorderPane.setMargin(bottomPane, new Insets(4, 4, 4, 4));
        setBottom(bottomPane);
    }

    /**
     * Initialisierung der Baumanzeigefläche.
     *
     * @return Baumanzeigefläche
     * @since 1.0
     */
    private ScrollPane initTreePane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        treeView = new TopicTreeView(mainPane);
        treeView.build();
        scrollPane.setContent(treeView);
        return scrollPane;
    }

    /**
     * Initialisierung der Bodenfläche
     *
     * @return Bodenfläche
     * @since 1.0
     */
    private BorderPane initBottomPane() {
        BorderPane borderPane = new BorderPane();

        TextField searchField = new TextField();
        searchField.setPromptText(Constants.BUNDLE.getString("search"));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty())
                scrollPane.setContent(treeView);
            else
                scrollPane.setContent(treeView.filter(newValue));
        });
        borderPane.setCenter(searchField);

        Button aboutButton = new Button("", new ImageView(Images.getInternal("icons_x16/info.png")));
        aboutButton.setOnAction(a -> new AboutWindow(getScene().getWindow()).show());
        BorderPane.setMargin(aboutButton, new Insets(0, 0, 0, 4));
        borderPane.setRight(aboutButton);

        return borderPane;
    }
}
