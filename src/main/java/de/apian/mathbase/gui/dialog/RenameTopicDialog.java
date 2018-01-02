/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.dialog;

import de.apian.mathbase.gui.MainPane;
import de.apian.mathbase.util.FileUtils;
import de.apian.mathbase.xml.TopicTreeController;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class RenameTopicDialog extends TextInputDialog {

    /**
     * Basisanzeigefläche
     *
     * @since 1.0
     */
    private MainPane mainPane;

    /**
     * Themenbaumkontrolleur
     *
     * @since 1.0
     */
    private TopicTreeController topicTreeController;

    /**
     * Titeleingabefeld
     *
     * @since 1.0
     */
    private TextField titleField;

    /**
     * Hinweis auf ungültigen Titel
     *
     * @since 1.0
     */
    private Label invalidLabel;

    /**
     * Konstruktion des Themenhinzufügungsdialogs
     *
     * @param mainPane Basisanzeigefläche
     * @since 1.0
     */
    public RenameTopicDialog(MainPane mainPane, TopicTreeController topicTreeController) {
        this.mainPane = mainPane;
        this.topicTreeController = topicTreeController;
        initOwner(mainPane.getScene().getWindow());
        setTitle("Themenverwaltung");
        setHeaderText("Thema umbenennen");
        setResizable(false);

        getDialogPane().setContent(createGrid());
        initOkButton();
        setResultConverter(buttonType -> buttonType != null && buttonType.getButtonData()
                == ButtonBar.ButtonData.OK_DONE ? titleField.getText() : null);
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


        Label titleLabel = new Label("Titel:");
        gridPane.add(titleLabel, 0, 0);

        titleField = new TextField();
        titleField.setPrefColumnCount(20);
        titleField.textProperty().addListener((observable, oldValue, newValue) -> invalidLabel.setVisible(false));
        GridPane.setHgrow(titleField, Priority.ALWAYS);
        GridPane.setFillWidth(titleField, true);
        gridPane.add(titleField, 1, 0);

        invalidLabel = new Label();
        invalidLabel.setTextFill(Color.RED);
        invalidLabel.setVisible(false);
        gridPane.add(invalidLabel, 1, 1);

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
            String title = titleField.getText();
            if (title == null || title.isEmpty()) {
                invalidLabel.setText("Titel darf nicht leer sein!");
                invalidLabel.setVisible(true);
                a.consume();
            } else if (topicTreeController.doesExist(FileUtils.normalize(titleField.getText()))) {
                invalidLabel.setText("Thema mit diesem Titel existiert bereits!");
                invalidLabel.setVisible(true);
                a.consume();
            }
        });
    }
}
