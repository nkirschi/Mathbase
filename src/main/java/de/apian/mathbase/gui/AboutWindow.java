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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Scanner;

public class AboutWindow extends Stage {
    public AboutWindow(Window owner) {
        initOwner(owner);
        setTitle(Constants.BUNDLE.getString("about_title"));
        setWidth(570);
        setHeight(530);
        setResizable(false);
        getIcons().add(Images.getInternal("icons_x16/mathsbox.png"));

        setScene(new Scene(initContentPane()));
    }

    private BorderPane initContentPane() {
        return new BorderPane(initText());
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
