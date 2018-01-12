/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.content;

import de.apian.mathbase.gui.MainPane;
import de.apian.mathbase.gui.dialog.WarningAlert;
import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import de.apian.mathbase.util.Logging;
import de.apian.mathbase.xml.Content;
import de.apian.mathbase.xml.TopicTreeController;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Inhaltsanzeige eines gewählten Themas.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class ContentPane extends BorderPane {

    /**
     * Hauptanzeigefläche
     *
     * @since 1.0
     */
    private MainPane mainPane;

    /**
     * Themenbaumkontrolleur
     *
     * @since 1.0
     */
    private TopicTreeController topicTreeController;

    /**
     * Konstruktion der Inhaltsanzeige.
     *
     * @param title               Titel des zugehörigen Themas
     * @param topicTreeController Themenbaumkontrolleur
     */
    public ContentPane(String title, MainPane mainPane, TopicTreeController topicTreeController) {
        this.mainPane = mainPane;
        this.topicTreeController = topicTreeController;

        setPadding(new Insets(10, 10, 10, 10));

        VBox titleBox = initTitleBox(title);
        setTop(titleBox);

        GridPane contentGrid = initContentGrid(title);
        setCenter(contentGrid);
    }

    private VBox initTitleBox(String title) {
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font(Constants.TITLE_FONT_FAMILY, FontWeight.BOLD, 24));
        titleLabel.setTextFill(Constants.ACCENT_COLOR);
        Button addButton = new Button(Constants.BUNDLE.getString("add"),
                new ImageView(Images.getInternal("icons_x16/add.png")));
        addButton.setOnAction(a -> {
            AddContentDialog dialog = new AddContentDialog(mainPane.getScene().getWindow());
            Optional<Content> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    topicTreeController.addContent(result.get(), title);
                } catch (IOException | TransformerException e) {
                    Logging.log(Level.WARNING, "Inhalt hinzufügen fehlgeschlagen!", e);
                    new WarningAlert().showAndWait();
                }
            }
        });
        BorderPane titlePane = new BorderPane();
        titlePane.setCenter(titleLabel);
        titlePane.setRight(addButton);
        VBox titleBox = new VBox(5, titlePane, new Separator());
        BorderPane.setMargin(titleBox, new Insets(0, 0, 10, 0));
        return titleBox;
    }

    private GridPane initContentGrid(String title) {
        GridPane contentGrid = new GridPane();
        contentGrid.setVgap(10);
        contentGrid.setHgap(10);

        // Füllen mit Inhalt
        Content[] contents = topicTreeController.getContents(title);
        for (Content content : contents)
            contentGrid.getChildren().add(createTile(content, topicTreeController.locateDirectory(title)));

        // Herstellen der Responsivität
        widthProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(widthProperty().get());
            int columnCount = Math.max(newValue.intValue() / Constants.COL_MIN_WIDTH, 1);
            List<Node> children = new ArrayList<>(contentGrid.getChildren());// reihenweise

            contentGrid.getChildren().clear();
            for (int i = 0; i < children.size(); i++)
                contentGrid.add(children.get(i), i % columnCount, i / columnCount);

            contentGrid.getColumnConstraints().clear();
            for (int i = 0; i < columnCount; i++) {
                ColumnConstraints constraints = new ColumnConstraints();
                constraints.setHgrow(Priority.ALWAYS);
                constraints.setPercentWidth(100.0 / columnCount);
                contentGrid.getColumnConstraints().add(constraints);
            }

        });

        return contentGrid;
    }

    private AbstractTile createTile(Content content, String directoryPath) {
        switch (content.getType()) {
            case DESCRIPTION:
                return new DescriptionTile(content, directoryPath);
            case GEOGEBRA:
                return new GeogebraTile(content, directoryPath);
            case IMAGE:
                return new ImageTile(content, directoryPath);
            case VIDEO:
                return new VideoTile(content, directoryPath);
            case WORKSHEET:
                return new WorksheetTile(content, directoryPath);
            case EDITABLE_WORKSHEET:
                return new EditableWorksheetTile(content, directoryPath);
            default:
                return new LinkTile(content, directoryPath);
        }
    }
}
