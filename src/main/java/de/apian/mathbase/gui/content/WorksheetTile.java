/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.content;

import de.apian.mathbase.util.Images;
import de.apian.mathbase.xml.Content;
import javafx.scene.image.Image;

/**
 * Arbeitsblattkachel.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
class WorksheetTile extends LinkTile {
    WorksheetTile(Content content, String directoryPath, ContentPane contentPane) {
        super(content, directoryPath, contentPane);
    }

    @Override
    protected Image getImage() {
        return Images.getInternal("icons_x64/pdf.png");
    }
}
