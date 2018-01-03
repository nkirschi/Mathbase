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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

import static de.apian.mathbase.util.Constants.FATAL_ERROR_MESSAGE;
import static de.apian.mathbase.util.Constants.HASHED_PASSWORD;

/**
 * Passworteingabedialog für administrative Operationen, z.B. Löschen von Themen.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class PasswordDialog extends AbstractTextDialog {

    /**
     * Konstruktion des Passworteingabedialogs.
     *
     * @param mainPane Basisanzeigefläche
     * @since 1.0
     */
    public PasswordDialog(MainPane mainPane) {
        super(mainPane, new PasswordField());
        setTitle("Authentifikation");
        setGraphic(new ImageView(Images.getInternal("password.png")));
        setInputDescription("Passwort");
    }

    @Override
    protected void initOkButton() {
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
                            infoLabel.setVisible(false);
                        });
                        timer.cancel();
                        timer.purge();
                    }
                }
            }, 0, 1000);

            if (!hash(textField.getText()).equals(HASHED_PASSWORD)) {
                infoLabel.setText("Passwort inkorrekt!");
                infoLabel.setVisible(true);
                a.consume();
            }
        });
    }

    /**
     * Hashing einer Zeichenkette.
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
