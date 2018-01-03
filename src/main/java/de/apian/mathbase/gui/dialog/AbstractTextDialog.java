/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.dialog;

import de.apian.mathbase.gui.MainPane;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

/**
 * Abstrakter Texteingabedialog für mannigfaltigste Notwendigkeiten
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractTextDialog extends TextInputDialog {

    /**
     * Eingabebeschreibung.
     *
     * @since 1.0
     */
    protected Label descriptionLabel;

    /**
     * Texteingabefeld.
     *
     * @since 1.0
     */
    protected TextField textField;

    /**
     * Hinweistext.
     *
     * @since 1.0
     */
    protected Label infoLabel;

    /**
     * Konstruktion des Texteingabedialogs.
     *
     * @param mainPane Basisanzeigefläche
     * @since 1.0
     */
    protected AbstractTextDialog(MainPane mainPane, TextField textField) {
        this.textField = textField;
        initOwner(mainPane.getScene().getWindow());
        setResizable(false);
        getDialogPane().setContent(createDialogContent());
        initOkButton();
        setResultConverter(buttonType -> buttonType != null && buttonType.getButtonData()
                == ButtonBar.ButtonData.OK_DONE ? textField.getText() : null);
    }

    /**
     * Kreation des Dialoginhalts.
     *
     * @return Dialoggitter
     * @since 1.0
     */
    private GridPane createDialogContent() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.setAlignment(Pos.CENTER_LEFT);


        descriptionLabel = new Label();
        gridPane.add(descriptionLabel, 0, 0);

        textField.setPrefColumnCount(20);
        textField.textProperty().addListener((observable, oldValue, newValue) -> infoLabel.setVisible(false));
        GridPane.setHgrow(textField, Priority.ALWAYS);
        GridPane.setFillWidth(textField, true);
        gridPane.add(textField, 1, 0);

        infoLabel = new Label();
        infoLabel.setTextFill(Color.RED);
        infoLabel.setVisible(false);
        gridPane.add(infoLabel, 1, 1);

        return gridPane;
    }

    /**
     * @param description Eingabebeschreibung.
     */
    protected void setInputDescription(String description) {
        descriptionLabel.setText(description + ":");
    }

    /**
     * Initialisierung des OK-Buttons.
     *
     * @since 1.0
     */
    protected abstract void initOkButton();
}
