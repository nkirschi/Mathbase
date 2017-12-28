/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Passworteingabedialog bei administrativen Operationen
 */
public class PasswordDialog extends TextInputDialog {
    public PasswordDialog() {
        setTitle("Authentifikation");
        setHeaderText("Administrative Operation");
        setContentText("Passwort:");
        try {
            setGraphic(new ImageView(Images.fetch(Constants.IMAGE_ROOT + "password.png", true)));
            Stage stage = (Stage) getDialogPane().getScene().getWindow();
            stage.getIcons().add(Images.fetch(Constants.IMAGE_ROOT + "icon.png", true));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        setResizable(false);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label("Passwort:");
        gridPane.add(label, 0, 0);
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefColumnCount(16);
        GridPane.setHgrow(passwordField, Priority.ALWAYS);
        GridPane.setFillWidth(passwordField, true);
        gridPane.add(passwordField, 1, 0);
        getDialogPane().setContent(gridPane);

        setResultConverter(buttonType -> {
            ButtonBar.ButtonData data = buttonType == null ? null : buttonType.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? passwordField.getText() : null;
        });
    }
}
