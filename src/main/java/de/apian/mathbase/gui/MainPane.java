/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.gui.dialog.DialogUtils;
import de.apian.mathbase.gui.dialog.ErrorAlert;
import de.apian.mathbase.util.Constants;
import de.apian.mathbase.xml.TopicTreeController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;

import java.io.IOException;
import java.util.Optional;

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
        setDividerPositions(0.35);

        TopicTreeController topicTreeController = initController();

        SidebarPane sidebarPane = new SidebarPane(this, topicTreeController);
        SplitPane.setResizableWithParent(sidebarPane, Boolean.FALSE);

        getItems().addAll(sidebarPane, new HintPane());
    }

    /**
     * Initialisierung des Themenbaumkontrolleurs mit Fehlerbehandlung
     *
     * @return Einzigster Themenbaumkontrolleur der Anwendung
     */
    private TopicTreeController initController() {
        TopicTreeController topicTreeController = null;
        try {
            topicTreeController = new TopicTreeController();
        } catch (IOException e) {
            ErrorAlert errorAlert = new ErrorAlert(e);
            errorAlert.showAndWait();
            Optional<ButtonType> result = DialogUtils.showAlert(null, Alert.AlertType.INFORMATION,
                    "Mathbase", null, Constants.BUNDLE.getString("no_data"),
                    ButtonType.YES, ButtonType.NO);

            if (result.isPresent() && result.get().equals(ButtonType.YES)) {
                try {
                    TopicTreeController.recreateFile();
                } catch (IOException e1) {
                    throw new InternalError();
                }
            }
            System.exit(0);
        }
        return topicTreeController;
    }
}
