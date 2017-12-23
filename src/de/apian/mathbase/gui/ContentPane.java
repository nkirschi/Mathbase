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

import java.io.IOException;

/**
 * Anzeigefläche für die Inhalte des gewählten Themas
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class ContentPane extends VBox {
    ContentPane() {
        try {
            getChildren().add(new ImageView(Images.fetch(Constants.IMAGE_ROOT + "icon.png", true)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        setAlignment(Pos.CENTER);
        Label label = new Label("Mathbase " + Constants.APP_VERSION);
        label.setFont(Constants.DEFAULT_FONT);
        getChildren().add(label);
    }
}
