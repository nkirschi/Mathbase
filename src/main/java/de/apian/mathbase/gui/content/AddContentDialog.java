/*
 * Copyright (c) 2018. MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.content;

import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import de.apian.mathbase.xml.Content;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;
import javafx.stage.Window;

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

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        setResultConverter(buttonType -> {
            if (buttonType.equals(ButtonType.OK))
                return new Content(Content.Type.DESCRIPTION, "", null);
            return null;
        });
    }
}
