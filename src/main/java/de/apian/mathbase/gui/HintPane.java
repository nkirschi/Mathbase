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
import javafx.scene.text.TextAlignment;

public class HintPane extends VBox {
    HintPane() {
        setSpacing(10);
        getChildren().add(new ImageView(Images.getInternal("icon2.png")));
        setAlignment(Pos.CENTER);

        Label hint = new Label(Constants.BUNDLE.getString("hint"));
        hint.setWrapText(true);
        hint.setMaxWidth(400);

        hint.setTextAlignment(TextAlignment.CENTER);
        getChildren().add(hint);
    }
}
