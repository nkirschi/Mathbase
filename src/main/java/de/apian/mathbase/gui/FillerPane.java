/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Lückenfüller.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class FillerPane extends VBox {
    public FillerPane() {
        setSpacing(20);

        VBox vBox = new VBox(-10);
        vBox.getChildren().add(new ImageView(Images.getInternal("logo/logo.png")));
        setAlignment(Pos.CENTER);

        Label mathbase = new Label("Mathbase");
        mathbase.setFont(Font.font(Constants.TITLE_FONT_FAMILY, 16));
        vBox.getChildren().add(mathbase);
        vBox.setAlignment(Pos.CENTER);

        getChildren().add(vBox);

        Label hint = new Label(Constants.BUNDLE.getString("hint"));
        hint.setWrapText(true);
        hint.setMaxWidth(400);

        hint.setTextAlignment(TextAlignment.CENTER);
        getChildren().add(hint);
    }
}
