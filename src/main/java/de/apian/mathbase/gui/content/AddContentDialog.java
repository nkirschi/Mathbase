/*
 * Copyright (c) 2018. MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.content;

import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import de.apian.mathbase.xml.Content;
import javafx.collections.FXCollections;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Window;
import javafx.util.StringConverter;

/**
 * Dialog zum Hinzufügen eines Inhalts
 *
 * @author Benedikt Mödl
 * @version 1.0
 * @see de.apian.mathbase.xml.Content
 * @since 1.0
 */
public class AddContentDialog extends Dialog<Content> {
    private Content.Type type;
    private String filePath;
    private String caption;

    public AddContentDialog(Window owner) {
        type = Content.Type.OTHER;
        filePath = "";
        caption = null;

        initOwner(owner);
        setTitle(Constants.BUNDLE.getString("topic_management"));
        setHeaderText(Constants.BUNDLE.getString("add_content"));
        setGraphic(new ImageView(Images.getInternal("icons_x48/multimedia.png")));
        setResizable(true);


        ComboBox<Content.Type> comboBox = new ComboBox<>(FXCollections.observableArrayList(Content.Type.values()));
        comboBox.setPromptText(Constants.BUNDLE.getString("please_select"));
        comboBox.setConverter(new StringConverter<Content.Type>() {
            @Override
            public String toString(Content.Type object) {
                return Constants.BUNDLE.getString(object.toString());
            }

            @Override
            public Content.Type fromString(String string) {
                return null;
            }
        });

        comboBox.setOnAction(a -> {
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            gridPane.addRow(0, new Label(Constants.BUNDLE.getString("content_type") + ":"), comboBox);

            Label titleLabel = new Label(Constants.BUNDLE.getString("title") + ":");
            TextField titleField = new TextField();
            GridPane.setFillWidth(titleField, true);
            GridPane.setHgrow(titleField, Priority.ALWAYS);
            gridPane.addRow(1, titleLabel, titleField);

            //TODO Abspeichern in Datei implementieren! -> Variable "filePath"
            type = comboBox.getSelectionModel().getSelectedItem();
            if (type.equals(Content.Type.DESCRIPTION)) {
                type = Content.Type.DESCRIPTION;
                Label descriptionLabel = new Label(Constants.BUNDLE.getString("description") + ":");
                TextArea descriptionArea = new TextArea();
                descriptionArea.setPrefRowCount(5);
                GridPane.setValignment(descriptionLabel, VPos.TOP);
                gridPane.addRow(2, descriptionLabel, descriptionArea);
                //TODO impl
            } else if (type.equals(Content.Type.OTHER)) {
                Label selectFileLabel = new Label(Constants.BUNDLE.getString("please_select"));
                Button selectFileButton = new Button(Constants.BUNDLE.getString("file"));
                gridPane.addRow(3, selectFileLabel, selectFileButton);
                selectFileButton.setOnAction(a1 -> {
                    //TODO impl
                });
            } else {
                //Bringt Dateierweiterungen in ein schönes Format
                StringBuilder fileExtensionsBuilder = new StringBuilder();
                for (String s : type.getFileExtensions()) {
                    fileExtensionsBuilder.append("*").append(s).append(", ");
                }
                if (fileExtensionsBuilder.length() > 1) {
                    //Herrauslöschen des letzten ", ", da es unnötig ist.
                    fileExtensionsBuilder.delete(fileExtensionsBuilder.length() - 2, fileExtensionsBuilder.length() - 1);
                } else {
                    // Sollte nicht vorkommen, da jeder Typ außer OTHER besondere Dateierweiterungen
                    // zugeordnet bekommen sollte
                    fileExtensionsBuilder.append("*.*");
                }

                Label selectFileLabel = new Label(Constants.BUNDLE.getString("please_select"));
                Button selectFileButton = new Button(Constants.BUNDLE.getString("file") + " (" +
                        fileExtensionsBuilder.toString() + ")");
                gridPane.addRow(3, selectFileLabel, selectFileButton);
                selectFileButton.setOnAction(a1 -> {
                    //TODO impl
                });
            }

            getDialogPane().setContent(gridPane);

            getDialogPane().getScene().getWindow().sizeToScene();
            getDialogPane().getScene().getWindow().centerOnScreen();
        });

        getDialogPane().setContent(new HBox(comboBox));

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        setResultConverter(buttonType -> {
            if (buttonType.equals(ButtonType.OK))
                return new Content(type, filePath, caption);
            return null;
        });
    }
}
