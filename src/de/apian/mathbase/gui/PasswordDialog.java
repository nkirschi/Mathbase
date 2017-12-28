/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

import static de.apian.mathbase.util.Constants.FATAL_ERROR_MESSAGE;
import static de.apian.mathbase.util.Constants.HASHED_PASSWORD;

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
            okButton.setDisable(true);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                private int counter = 5;
                private String text = okButton.getText();

                @Override
                public void run() {
                    if (counter > 0) {
                        Platform.runLater(() -> okButton.setText(text + " (" + counter-- + ")"));
                    } else {
                        Platform.runLater(() -> {
                            okButton.setDisable(false);
                            okButton.setText(text);
                        });
                        timer.cancel();
                    }
                }
            }, 0, 1000);

            if (!hash(passwordField.getText()).equals(HASHED_PASSWORD)) {
                e.consume();
                incorrectLabel.setVisible(true);
            }
        });

        setResultConverter(buttonType -> {
            ButtonBar.ButtonData data = buttonType == null ? null : buttonType.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? passwordField.getText() : null;
        });
    }

    private String hash(String s) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] bytes = sha256.digest(s.getBytes(StandardCharsets.UTF_8));

            // Überführung des byte-Arrays in einen Hexadezimalstring
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hash.append('0');
                hash.append(hex);
            }

            return hash.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new InternalError(FATAL_ERROR_MESSAGE); // Weltuntergang!
        }
    }
}
