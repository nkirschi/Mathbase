/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import de.apian.mathbase.xml.Content;
import de.apian.mathbase.xml.TopicTreeController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.ResourceBundle;

/**
 * Inhaltsanzeige eines gewählten Themas.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class ContentPane extends ScrollPane {
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
        // Kein Thema ausgewählt
        if (title == null) {
            setContent(createSpaceFiller());
            return;
        }

        BorderPane borderPane = new BorderPane();
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font(Constants.TITLE_FONT.getFamily(), FontWeight.BOLD, 24));
        titleLabel.setTextFill(Constants.ACCENT_COLOR);

        borderPane.setTop(titleLabel);

        VBox vBox = new VBox(10);
        for (Content content : topicTreeController.getContents(title))
            vBox.getChildren().add(new Label(content.getPath()));
        borderPane.setCenter(vBox);

        setContent(borderPane);
    }

    private VBox createSpaceFiller() {
        VBox vBox = new VBox(10);
        vBox.getChildren().add(new ImageView(Images.getInternal("icon2.png")));
        vBox.setAlignment(Pos.CENTER);

        Label hint = new Label(Constants.BUNDLE.getString("hint"));
        hint.setWrapText(true);
        hint.setMaxWidth(400);

        hint.setTextAlignment(TextAlignment.CENTER);
        vBox.getChildren().add(hint);

        setFitToWidth(true);
        setFitToHeight(true);
        return vBox;
    }
}
