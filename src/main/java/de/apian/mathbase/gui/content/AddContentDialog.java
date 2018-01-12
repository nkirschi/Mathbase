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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Window;
import javafx.util.Callback;
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
        comboBox.setPromptText("Bitte auswählen...");
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
        getDialogPane().setContent(new HBox(comboBox));

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        setResultConverter(buttonType -> {
            if (buttonType.equals(ButtonType.OK))
                return new Content(Content.Type.DESCRIPTION, "", null);
            return null;
        });
    }
}
