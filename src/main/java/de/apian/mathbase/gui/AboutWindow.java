/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Scanner;

public class AboutWindow extends Stage {
    public AboutWindow(Window owner) {
        initOwner(owner);
        setTitle(Constants.BUNDLE.getString("about_title"));
        setResizable(false);
        getIcons().add(Images.getInternal("icons_x16/mathsbox.png"));

        setScene(new Scene(initContentPane()));
    }

    private VBox initContentPane() {
        VBox vBox = new VBox();

        BorderPane borderPane = new BorderPane();
        ImageView imageView = new ImageView(Images.getInternal("logo/logo.png"));
        borderPane.setLeft(imageView);
        Label label = new Label("Mathbase");
        label.setFont(Font.font(Constants.TITLE_FONT_FAMILY, FontWeight.NORMAL, 36));
        borderPane.setCenter(label);
        label.setAlignment(Pos.CENTER_LEFT);
        BorderPane.setAlignment(label, Pos.BOTTOM_LEFT);
        BorderPane.setMargin(label, new Insets(0, 0, 25, 20));
        borderPane.setPadding(new Insets(0, 20, 10, 20));

        vBox.setPadding(new Insets(10, 10, 10, 10));

        vBox.getChildren().addAll(borderPane, initText());

        return vBox;
    }

    private Text initText() {
        Text text = new Text();
        try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/texts/ABOUT"))) {
            while (scanner.hasNextLine())
                text.setText(text.getText() + scanner.nextLine() + "\n");
        }
        text.setTextAlignment(TextAlignment.CENTER);
        return text;
    }
}
