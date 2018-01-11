/*
 * Copyright (c) 2018. MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.dialog;

import com.sun.javafx.stage.StageHelper;
import de.apian.mathbase.util.Constants;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Allgemeine Fehlermeldung bei kleineren Sachen.
 *
 * @author Benedikt Mödl
 * @version 1.0
 * @since 1.0
 */
public class WarningAlert extends Alert {
    public WarningAlert() {
        super(AlertType.WARNING);

        for (Stage stage : StageHelper.getStages())
            initOwner(stage);

        setTitle(Constants.BUNDLE.getString("warning"));
        setHeaderText(Constants.BUNDLE.getString("warning_header"));
        setContentText(Constants.BUNDLE.getString("warning_content"));
        setResizable(true);
    }
}
