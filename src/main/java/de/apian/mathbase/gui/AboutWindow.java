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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AboutWindow extends Stage {
    public AboutWindow(Window owner) {
        initOwner(owner);
        setTitle(Constants.BUNDLE.getString("about_title"));
        setResizable(false);
        getIcons().add(Images.getInternal("icons_x16/mathsbox.png"));

        initModality(Modality.WINDOW_MODAL);

        setScene(new Scene(initContentPane()));
    }

    private BorderPane initContentPane() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(0, 10, 0, 10));

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BOTTOM_CENTER);

        ImageView imageView = new ImageView(Images.getInternal("logo/logo.png"));
        Label label = new Label("Mathbase");
        label.setFont(Font.font(Constants.TITLE_FONT_FAMILY, FontWeight.NORMAL, 36));
        HBox.setMargin(label, new Insets(0, 0, 25, 10));

        hBox.getChildren().addAll(imageView, label);

        borderPane.setTop(hBox);
        borderPane.setCenter(initText());
        return borderPane;
    }

    private Text initText() {
        Text text = new Text();
        try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/texts/" + Constants.BUNDLE.
                getString("about_filename")), StandardCharsets.UTF_8.toString())) {
            while (scanner.hasNextLine())
                text.setText(text.getText() + scanner.nextLine() + "\n");
        }
        text.setTextAlignment(TextAlignment.CENTER);
        return text;
    }
}
