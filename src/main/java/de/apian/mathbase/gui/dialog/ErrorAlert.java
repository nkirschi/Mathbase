/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.dialog;

import com.sun.javafx.stage.StageHelper;
import de.apian.mathbase.util.Constants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Screen;
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

        for (Stage stage : StageHelper.getStages())
            initOwner(stage);

        setTitle(Constants.BUNDLE.getString("error"));
        setHeaderText(Constants.BUNDLE.getString("error_header"));
        setContentText(t.getMessage());
        setResizable(true);

        StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer));

        TextArea textArea = new TextArea(writer.toString());
        textArea.setEditable(false);
        textArea.setWrapText(false);
        textArea.setPrefRowCount(30);
        textArea.setPrefColumnCount(60);

        getDialogPane().setExpandableContent(new BorderPane(textArea));
    }
}