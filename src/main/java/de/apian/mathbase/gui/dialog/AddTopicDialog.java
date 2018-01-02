/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.dialog;

import de.apian.mathbase.gui.MainPane;
import de.apian.mathbase.util.FileUtils;
import de.apian.mathbase.util.Images;
import de.apian.mathbase.xml.TopicTreeController;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class AddTopicDialog extends TextInputDialog {

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
    private TopicTreeController controller;

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
     * Miniaturansicht des Themenicons
     *
     * @since 1.0
     */
    private ImageView icon;

    /**
     * Pfad zum Icon
     *
     * @since 1.0
     */
    private String iconPath;

    /**
     * Konstruktion des Themenhinzufügungsdialogs
     *
     * @param mainPane Basisanzeigefläche
     * @since 1.0
     */
    public AddTopicDialog(MainPane mainPane, TopicTreeController controller) {
        this.mainPane = mainPane;
        this.controller = controller;
        initOwner(mainPane.getScene().getWindow());
        setTitle("Themenverwaltung");
        setHeaderText("Neues Thema hinzufügen");
        setResizable(false);

        getDialogPane().setContent(createGrid());
        initOkButton();
        setResultConverter(buttonType -> buttonType != null && buttonType.getButtonData()
                == ButtonBar.ButtonData.OK_DONE ? titleField.getText() + ";" + iconPath : null);
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

        Label iconLable = new Label("Icon:");
        gridPane.add(iconLable, 0, 2);

        Button iconButton = new Button("Durchsuchen...");
        iconButton.setOnAction(a -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File file = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
            try {
                Image image = SwingFXUtils.toFXImage(ImageIO.read(file), null);
                image = Images.resize(image, 25, 25);
                icon.setImage(image);
                iconPath = file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        HBox hBox = new HBox(10);

        Image image = Images.getInternal("icon.png");
        image = Images.resize(image, 25, 25);
        icon = new ImageView(image);
        iconPath = "";
        hBox.getChildren().add(icon);

        hBox.getChildren().add(iconButton);
        gridPane.add(hBox, 1, 2);

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
            } else if (controller.doesExist(FileUtils.normalize(titleField.getText()))) {
                invalidLabel.setText("Thema mit diesem Titel existiert bereits!");
                invalidLabel.setVisible(true);
                a.consume();
            }
        });
    }
}
