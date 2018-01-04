/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.dialog;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.stage.Window;

public class DialogUtils {
    private DialogUtils() {
    }

    public static void showMessageDialog(Window window, Alert.AlertType alertType, String title, String headerText,
                                         String contentText, ButtonType... buttonTypes) {

        Alert alert = new Alert(alertType, contentText, buttonTypes);
        alert.initOwner(window);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
}
