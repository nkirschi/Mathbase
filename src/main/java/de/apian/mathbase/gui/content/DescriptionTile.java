/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.content;

import de.apian.mathbase.xml.Content;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Beschreibungskachel.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class DescriptionTile extends AbstractTile {
    public DescriptionTile(Content content, String directoryPath) {
        super(content);

        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setEditable(false);


        try {
            for (String line : Files.readAllLines(Paths.get(directoryPath, content.getFilename())))
                textArea.appendText(line);
        } catch (IOException e) {
            textArea.setText("Leider konnte der Text nicht geladen werden!");
        }

        setCenter(textArea);
    }
}
