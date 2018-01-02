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
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.Locale;
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
     * Themenbaumkontrolleur
     *
     * @since 1.0
     */
    private TopicTreeController topicTreeController;

    /**
     * Konstruktion der Inhaltsanzeige
     *
     * @param title               Titel des zugehörigen Themas
     * @param topicTreeController Themenbaumkontrolleur
     */
    public ContentPane(String title, MainPane mainPane, TopicTreeController topicTreeController) {
        this.topicTreeController = topicTreeController;

        setPadding(new Insets(10, 10, 10, 10));

        // TODO weitermachen
        if (title == null) {
            VBox outerBox = new VBox(40);
            VBox vbox = new VBox(0);
            vbox.getChildren().add(new ImageView(Images.getInternal("icon2.png")));
            vbox.setAlignment(Pos.CENTER);
            Label label = new Label("Mathbase " + Constants.APP_VERSION);
            label.setFont(Constants.TITLE_FONT);
            vbox.getChildren().add(label);
            outerBox.getChildren().add(vbox);
            outerBox.setAlignment(Pos.CENTER);

            ResourceBundle bundle = ResourceBundle.getBundle(Constants.RESOURCE_BUNDLE_PATH, Locale.GERMAN);
            Label hint = new Label(bundle.getString("hint"));
            hint.setWrapText(true);
            hint.setMaxWidth(400);

            hint.setTextAlignment(TextAlignment.CENTER);
            outerBox.getChildren().add(hint);

            setFitToWidth(true);
            setFitToHeight(true);
            setContent(outerBox);
        } else {
            VBox vBox = new VBox(10);
            vBox.getChildren().add(new Label(title));

            for (Content content : topicTreeController.getContents(title))
                vBox.getChildren().add(new Label(content.getPath()));
            setContent(vBox);
        }
    }
}
