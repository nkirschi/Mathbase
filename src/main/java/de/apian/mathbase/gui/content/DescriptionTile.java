/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.content;

import de.apian.mathbase.util.Constants;
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
        super(content, directoryPath);

        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setEditable(false);

        try {
            int position = textArea.getCaretPosition();
            for (String line : Files.readAllLines(Paths.get(directoryPath, content.getFilename())))
                textArea.appendText(line + "\n");
            textArea.positionCaret(position);
        } catch (IOException e) {
            textArea.setText(Constants.BUNDLE.getString("text_load_fail"));
        }

        editButton.setOnAction(a -> {
            if (editButton.getText() == null) {
                editButton.setText("Fertig");
                saveButton.setVisible(false);
                textArea.setEditable(true);
            } else {
                editButton.setText(null);
                saveButton.setVisible(true);
                textArea.setEditable(false);
                try {
                    Files.write(Paths.get(directoryPath, content.getFilename()), textArea.getText().getBytes("utf-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        setCenter(textArea);
    }
}
