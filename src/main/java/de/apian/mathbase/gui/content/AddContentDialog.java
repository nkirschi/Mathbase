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
    public AddContentDialog(Window owner) {
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

            switch (comboBox.getSelectionModel().getSelectedItem()) {
                case DESCRIPTION:
                    Label descriptionLabel = new Label(Constants.BUNDLE.getString("description") + ":");
                    TextArea descriptionArea = new TextArea();
                    descriptionArea.setPrefRowCount(5);
                    GridPane.setValignment(descriptionLabel, VPos.TOP);
                    gridPane.addRow(2, descriptionLabel, descriptionArea);
                    break;
                case GEOGEBRA:
                    break;
                case IMAGE:
                    break;
                case VIDEO:
                    break;
                case WORKSHEET:
                    break;
                case EDITABLE_WORKSHEET:
                    break;
                default:
                    break;
            }

            getDialogPane().setContent(gridPane);

            getDialogPane().getScene().getWindow().sizeToScene();
            getDialogPane().getScene().getWindow().centerOnScreen();
        });

        getDialogPane().setContent(new HBox(comboBox));

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        setResultConverter(buttonType -> {
            if (buttonType.equals(ButtonType.OK))
                return new Content(Content.Type.DESCRIPTION, "", null);
            return null;
        });
    }
}
