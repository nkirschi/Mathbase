/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainPane extends BorderPane {
    /**
     * Statische Instanzreferenz auf das Singleton {@code MainMenu}
     *
     * @since 1.0
     */
    private static MainPane instance;

    private MainPane() {
        try {
            VBox vBox = new VBox();
            vBox.getChildren().add(new ImageView(Images.fetch(Constants.IMAGE_ROOT + "icon.png", true)));
            vBox.setAlignment(Pos.CENTER);
            Label label = new Label("Mathbase " + Constants.APP_VERSION);
            label.setFont(Constants.DEFAULT_FONT);
            vBox.getChildren().add(label);
            setCenter(vBox);


            TreeView<String> treeView = new TreeView<>();
            treeView.setRoot(new TreeItem<>());
            treeView.setShowRoot(false);
            treeView.setEditable(true);

            //TODO Implementieren
            TreeItem<String> pythagoras = new TreeItem<>("Satzgruppe des Pythagoras");
            pythagoras.setExpanded(true);
            pythagoras.getChildren().add(new TreeItem<>("Satz des Pythagoras"));
            pythagoras.getChildren().add(new TreeItem<>("Kathetensatz"));
            pythagoras.getChildren().add(new TreeItem<>("Höhensatz"));
            //treeView.getSelectionModel().getSelectedItem();
            treeView.getRoot().getChildren().add(pythagoras);

            setLeft(treeView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Singleton-Instanzoperation
     *
     * @return einzige Instanz von {@code MainMenu}
     * @since 1.0
     */
    static MainPane getInstance() {
        if (instance == null)
            instance = new MainPane();
        return instance;
    }
}
