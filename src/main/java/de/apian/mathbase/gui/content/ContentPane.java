/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.content;

import de.apian.mathbase.gui.MainPane;
import de.apian.mathbase.util.Constants;
import de.apian.mathbase.xml.Content;
import de.apian.mathbase.xml.TopicTreeController;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

/**
 * Inhaltsanzeige eines gewählten Themas.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class ContentPane extends BorderPane {
    /**
     * Themenbaumkontrolleur.
     *
     * @since 1.0
     */
    private TopicTreeController topicTreeController;

    /**
     * Konstruktion der Inhaltsanzeige.
     *
     * @param title               Titel des zugehörigen Themas
     * @param topicTreeController Themenbaumkontrolleur
     */
    public ContentPane(String title, MainPane mainPane, TopicTreeController topicTreeController) {
        this.topicTreeController = topicTreeController;

        setPadding(new Insets(10, 10, 10, 10));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font(Constants.TITLE_FONT_FAMILY, FontWeight.BOLD, 24));
        titleLabel.setTextFill(Constants.ACCENT_COLOR);

        setTop(titleLabel);

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Content[] contents = topicTreeController.getContents(title);
        for (int i = 0, column = 0; i < contents.length; i++, column = (column == 0) ? 1 : 0) {
            AbstractTile tile = new DescriptionTile(contents[i]);
            gridPane.add(tile, column, i / 2);
            GridPane.setHgrow(tile,Priority.ALWAYS );
        }
        setCenter(gridPane);

        widthProperty().addListener((observable, oldValue, newValue) -> {
            int columnCount = newValue.intValue() / Constants.COL_MIN_WIDTH;
            List<Node> children = new ArrayList<>(gridPane.getChildren());// reihenweise

            gridPane.getChildren().clear();
            for (int i = 0; i < children.size(); i++)
                gridPane.add(children.get(i), i % columnCount, i / columnCount);

            gridPane.getColumnConstraints().clear();
            for (int i = 0; i < columnCount; i++) {
                ColumnConstraints constraints = new ColumnConstraints();
                constraints.setPercentWidth(100.0 / columnCount);
                gridPane.getColumnConstraints().add(constraints);
            }

        });
    }
}
