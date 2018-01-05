/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HelpWindow extends Stage {
    public HelpWindow(MainPane mainPane) { // TODO nach dem Release hinzufügen
        initOwner(mainPane.getScene().getWindow());
        setTitle(Constants.BUNDLE.getString("help"));
        setWidth(500);
        setHeight(300);
        getIcons().add(Images.getInternal("icons_x16/mathsbox.png"));

        setScene(new Scene(new BorderPane()));
    }
}
