/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Hilfefenster.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class HelpWindow extends Stage {
    public HelpWindow(MainPane mainPane) {
        initOwner(mainPane.getScene().getWindow());
        setTitle(Constants.BUNDLE.getString("help"));
        setResizable(false);
        getIcons().add(Images.getInternal("icons_x16/help.png"));

        initModality(Modality.WINDOW_MODAL);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(initText());
        borderPane.setPadding(new Insets(10, 10, 10, 10));

        setScene(new Scene(borderPane));
    }

    private Text initText() {
        Text text = new Text();
        try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/texts/" + Constants.BUNDLE.
                getString("help_filename")), StandardCharsets.UTF_8.toString())) {
            while (scanner.hasNextLine())
                text.setText(text.getText() + scanner.nextLine() + "\n");
        }
        text.setTextAlignment(TextAlignment.LEFT);
        return text;
    }
}
