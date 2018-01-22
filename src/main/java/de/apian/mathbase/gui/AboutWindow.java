/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import de.apian.mathbase.util.Images;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Scanner;

public class AboutWindow extends Stage {
    public AboutWindow(Window owner) {
        initOwner(owner);
        setTitle("About Mathbase");
        setWidth(600);
        setHeight(450);
        getIcons().add(Images.getInternal("icons_x16/mathsbox.png"));

        Text text = new Text();

        try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/texts/LICENSE"))) {
            while (scanner.hasNextLine())
                text.setText(text.getText() + scanner.nextLine() + "\n");
        }
        setScene(new Scene(new ScrollPane(text)));
    }
}
