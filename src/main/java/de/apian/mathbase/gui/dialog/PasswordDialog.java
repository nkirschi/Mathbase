/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.dialog;

import de.apian.mathbase.gui.MainPane;
import de.apian.mathbase.util.Images;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

import static de.apian.mathbase.util.Constants.FATAL_ERROR_MESSAGE;
import static de.apian.mathbase.util.Constants.HASHED_PASSWORD;

/**
 * Passworteingabedialog für administrative Operationen
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class PasswordDialog extends TextInputDialog {
    /**
     * Passworteingabefeld
     *
     * @since 1.0
     */
    private TextField passwordField;

    /**
     * Hinweis auf falsches Passwort
     *
     * @since 1.0
     */
    private Label incorrectLabel;

    /**
     * Konstruktion des Passworteingabedialogs
     *
     * @param mainPane Basisanzeigefläche
     * @since 1.0
     */
    public PasswordDialog(MainPane mainPane) {
        initOwner(mainPane.getScene().getWindow());
        setTitle("Administrator");
        setHeaderText("Authentifikation erforderlich!");
        setResizable(false);
        setGraphic(new ImageView(Images.getInternal("password.png")));

        getDialogPane().setContent(createGrid());
        initOkButton();
        setResultConverter(buttonType -> (buttonType == null ? null : buttonType.getButtonData())
                == ButtonBar.ButtonData.OK_DONE ? passwordField.getText() : null);
    }

    /**
     * Kreation des Dialoggitters
     *
     * @return Dialoggitter
     * @since 1.0
     */
    private GridPane createGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.setAlignment(Pos.CENTER_LEFT);


        Label passwordLabel = new Label("Passwort:");
        gridPane.add(passwordLabel, 0, 0);

        passwordField = new PasswordField();
        passwordField.setPrefColumnCount(20);
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> incorrectLabel.setVisible(false));
        GridPane.setHgrow(passwordField, Priority.ALWAYS);
        GridPane.setFillWidth(passwordField, true);
        gridPane.add(passwordField, 1, 0);

        incorrectLabel = new Label("Passwort inkorrekt!");
        incorrectLabel.setTextFill(Color.RED);
        incorrectLabel.setVisible(false);
        gridPane.add(incorrectLabel, 1, 1);

        return gridPane;
    }

    /**
     * Initialisierung des OK-Buttons
     *
     * @since 1.0
     */
    private void initOkButton() {
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, a -> {
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
                            incorrectLabel.setVisible(false);
                        });
                        timer.cancel();
                        timer.purge();
                    }
                }
            }, 0, 1000);

            if (!hash(passwordField.getText()).equals(HASHED_PASSWORD)) {
                incorrectLabel.setVisible(true);
                a.consume();
            }
        });
    }

    /**
     * Hashing einer Zeichenkette
     *
     * @param s Klartext
     * @return Hashwert
     * @since 1.0
     */
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
