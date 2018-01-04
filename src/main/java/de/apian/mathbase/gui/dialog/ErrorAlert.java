/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.dialog;

import com.sun.javafx.stage.StageHelper;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import de.apian.mathbase.util.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Allgemeine Fehlermeldung bei gröberen Sachen.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class ErrorAlert extends Alert {
    public ErrorAlert(Throwable t) {
        super(AlertType.ERROR);

        for (Stage stage : StageHelper.getStages()) {
            initOwner(stage);
            stage.hide();
        }

        setTitle(Constants.BUNDLE.getString("fatal_error"));
        setHeaderText(Constants.FATAL_ERROR_MESSAGE);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefHeight(600);

        StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer));

        Text text = new Text(writer.toString());

        scrollPane.setContent(text);
        getDialogPane().setContent(scrollPane);
    }
}