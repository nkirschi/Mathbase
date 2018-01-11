/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.content;

import de.apian.mathbase.gui.dialog.WarningAlert;
import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.FileUtils;
import de.apian.mathbase.util.Images;
import de.apian.mathbase.util.Logging;
import de.apian.mathbase.xml.Content;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;

/**
 * Abstrakte Inhaltskachel.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class AbstractTile extends BorderPane {
    protected Button editButton, saveButton;

    public AbstractTile(Content content, String directoryPath) {
        BorderPane topPane = new BorderPane();

        editButton = new Button(null, new ImageView(Images.getInternal("icons_x16/edit.png")));
        saveButton = new Button(null, new ImageView(Images.getInternal("icons_x16/save.png")));
        Button dragButton = new Button(null, new ImageView(Images.getInternal("icons_x16/drag.png")));
        topPane.setRight(new HBox(3, editButton, saveButton, dragButton));

        Label titleLabel = new Label(content.getCaption());
        titleLabel.setFont(Font.font(Constants.TITLE_FONT_FAMILY));
        topPane.setCenter(titleLabel);

        BorderPane.setMargin(topPane, new Insets(0, 0, 5, 0));

        setTop(topPane);

        setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        setPadding(new Insets(5));
        setBorder(new Border(new BorderStroke(Constants.ACCENT_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));


        saveButton.setOnAction(a -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Textdateien (*.txt)", "*.txt"));
            File file = fileChooser.showSaveDialog(getScene().getWindow());
            if (file != null) {
                try {
                    FileUtils.copy(Paths.get(directoryPath, content.getFilename()), file.toPath());
                } catch (IOException e) {
                    Logging.log(Level.WARNING, "Datei abspeichern fehlgeschlagen!", e);
                    new WarningAlert().showAndWait();
                }
            }
        });
    }
}
