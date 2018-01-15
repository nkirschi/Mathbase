/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.content;

import de.apian.mathbase.util.Images;
import de.apian.mathbase.util.Logging;
import de.apian.mathbase.xml.Content;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;

/**
 * Linkkachel.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
class LinkTile extends AbstractTile {

    LinkTile(Content content, String directoryPath, ContentPane contentPane) {
        super(content, directoryPath, contentPane);

        String filename = content.getFilename();

        ImageView imageView = new ImageView(Images.getInternal(getImagePath()));
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        //imageView.setFitWidth(Constants.COL_MIN_WIDTH - 30);
        imageView.setCursor(Cursor.HAND);
        imageView.setPickOnBounds(true); //Damit auch die Transparenten Teile der Bilder klickbar sind
        imageView.setOnMouseClicked(a -> {
            try {
                Desktop.getDesktop().open(Paths.get(directoryPath, filename).toFile());
            } catch (IOException e) {
                Logging.log(Level.WARNING, "Datei " + filename + " konnte nicht geöffnet werden.");
            }
        });
        setCenter(imageView);
    }

    protected String getImagePath() {
        return "icons_x64/file.png";
    }

}
