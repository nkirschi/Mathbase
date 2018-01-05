/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.content;

import de.apian.mathbase.xml.Content;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Abstrakte Inhaltskachel.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class AbstractTile extends BorderPane {
    public AbstractTile(Content content) {
        HBox hBox = new HBox();

        Label titleLabel = new Label(content.getTitle());

        Button editButton = new Button("edit");
        Button moveButton = new Button("move");

        hBox.getChildren().addAll(titleLabel, editButton, moveButton);
        setTop(hBox);

        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }
}
