/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

import java.io.IOException;

/**
 * Passworteingabedialog bei administrativen Operationen
 */
public class PasswordDialog extends TextInputDialog {
    public PasswordDialog(MainPane mainPane) {
        initOwner(mainPane.getScene().getWindow());
        setTitle("Administrator");
        setHeaderText("Authentifikation erforderlich!");
        setResizable(false);
        try {
            setGraphic(new ImageView(Images.fetch(Constants.IMAGE_ROOT + "password.png", true)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.setAlignment(Pos.CENTER_LEFT);

        Label passwordLabel = new Label("Passwort:");
        gridPane.add(passwordLabel, 0, 0);
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefColumnCount(20);
        GridPane.setHgrow(passwordField, Priority.ALWAYS);
        GridPane.setFillWidth(passwordField, true);
        gridPane.add(passwordField, 1, 0);
        Label incorrectLabel = new Label("Passwort inkorrekt!");
        incorrectLabel.setTextFill(Color.RED);
        incorrectLabel.setVisible(false);
        gridPane.add(incorrectLabel, 1, 1);
        getDialogPane().setContent(gridPane);


        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, e -> {
            if (!passwordField.getText().equals("123456")) {
                e.consume();
                incorrectLabel.setVisible(true);
            }
        });

        setResultConverter(buttonType -> {
            ButtonBar.ButtonData data = buttonType == null ? null : buttonType.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? passwordField.getText() : null;
        });
    }
}
