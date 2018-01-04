/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.gui.dialog.DialogUtils;
import de.apian.mathbase.gui.dialog.ErrorAlert;
import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Logging;
import de.apian.mathbase.xml.TopicTreeController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Basisanzeigefläche der GUI.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class MainPane extends SplitPane {

    /**
     * Konstruktion der Basisanzeigefläche.
     *
     * @since 1.0
     */
    public MainPane() {
        try {
            TopicTreeController topicTreeController = new TopicTreeController();
            SidebarPane sidebarPane = new SidebarPane(this, topicTreeController);
            sidebarPane.setMinWidth(150);
            setDividerPositions(0.35);
            getItems().addAll(sidebarPane, new ContentPane(null, this, topicTreeController));
            SplitPane.setResizableWithParent(sidebarPane, Boolean.FALSE);
        } catch (IOException e) {
            ErrorAlert errorAlert = new ErrorAlert(e);
            errorAlert.show();
            Optional<ButtonType> result = DialogUtils.showAlert(null, Alert.AlertType.INFORMATION,
                    "Mathbase", Constants.BUNDLE.getString("no_data"),
                    Constants.BUNDLE.getString("no_file"), ButtonType.YES, ButtonType.NO);

            result.ifPresent(buttonType -> {
                if (buttonType.equals(ButtonType.YES)) {
                    try {
                        TopicTreeController.recreateFile();
                    } catch (IOException e1) {
                        throw new InternalError();
                    }
                } else {
                    Logging.log(Level.SEVERE, Constants.FATAL_ERROR_MESSAGE, e);
                }
            });
            System.exit(0);
        }
    }
}
