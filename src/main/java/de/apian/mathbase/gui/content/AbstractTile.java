/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.content;

import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import de.apian.mathbase.xml.Content;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Abstrakte Inhaltskachel.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class AbstractTile extends BorderPane {
    public AbstractTile(Content content) {
        BorderPane borderPane = new BorderPane();

        Label titleLabel = new Label(content.getTitle());
        titleLabel.setFont(Font.font(Constants.TITLE_FONT_FAMILY));
        borderPane.setCenter(titleLabel);

        Button editButton = new Button(null, new ImageView(Images.getInternal("icons_x16/edit.png")));
        Button dragButton = new Button(null, new ImageView(Images.getInternal("icons_x16/drag.png")));
        borderPane.setRight(new HBox(3, editButton, dragButton));


        BorderPane.setMargin(borderPane, new Insets(0, 0, 5, 0));

        setTop(borderPane);

        setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        setPadding(new Insets(5));
        setBorder(new Border(new BorderStroke(Constants.ACCENT_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }
}
