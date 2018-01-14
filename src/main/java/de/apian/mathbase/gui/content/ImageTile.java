/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.content;

import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import de.apian.mathbase.util.Logging;
import de.apian.mathbase.xml.Content;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;

/**
 * Bildkachel.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
class ImageTile extends AbstractTile {
    ImageTile(Content content, String directoryPath, ContentPane contentPane) {
        super(content, directoryPath, contentPane);

        try {
            ImageView imageView = new ImageView(Images.getExternal(directoryPath + content.getFilename()));
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setFitWidth(Constants.COL_MIN_WIDTH - 30);
            imageView.setCursor(Cursor.HAND);
            imageView.setOnMouseClicked(a -> {
                try {
                    Desktop.getDesktop().open(Paths.get(directoryPath, content.getFilename()).toFile());
                } catch (IOException e) {
                    Logging.log(Level.WARNING, "Bild konnte nicht geöffnet werden.", e);
                }
            });
            setCenter(imageView);
        } catch (IOException e) {
            Logging.log(Level.WARNING, "Bild konnte nicht geöffnet werden.", e);
            setCenter(new Label(Constants.BUNDLE.getString("picture_load_fail")));
        }
    }
}
