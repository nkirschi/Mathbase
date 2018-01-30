/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.content;

import de.apian.mathbase.gui.MainPane;
import de.apian.mathbase.gui.dialog.TitleDialog;
import de.apian.mathbase.gui.dialog.WarningAlert;
import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Images;
import de.apian.mathbase.util.Logging;
import de.apian.mathbase.xml.Content;
import de.apian.mathbase.xml.TopicTreeController;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.jpedal.PdfDecoder;

import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Level;


public class LinkTile extends AbstractTile {
    private BorderPane borderPane;
    private ImageView imageView;

    public LinkTile(Content content, String directoryPath, ContentPane contentPane, MainPane mainPane) {
        super(content, directoryPath, contentPane, mainPane);

        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCursor(Cursor.HAND);
        imageView.setPickOnBounds(false);
        imageView.setOnMouseClicked(a -> {
            try {
                Desktop.getDesktop().open(Paths.get(directoryPath, content.getFilename()).toFile());
            } catch (IOException e) {
                Logging.log(Level.WARNING, "Datei " + content.getFilename() + " konnte nicht geöffnet werden.");
                new WarningAlert().showAndWait();
            }
        });

        editButton.setOnAction(a -> {
            TitleDialog dialog = new TitleDialog(mainPane);
            dialog.setHeaderText(Constants.BUNDLE.getString("rename_content"));
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(caption -> {
                try {
                    TopicTreeController.getInstance().renameContent(content, contentPane.getTitle(), caption);
                    mainPane.setContent(new ContentPane(contentPane.getTitle(), mainPane));
                } catch (IOException | TransformerException e) {
                    Logging.log(Level.WARNING, "Inhalt " + content + " konnte nicht umbenannt werden", e);
                    new WarningAlert().showAndWait();
                }
            });
        });

        borderPane = new BorderPane();
        new Thread(this::initDisplay).start();
        Label filenameLabel = new Label(content.getFilename());
        BorderPane.setAlignment(filenameLabel, Pos.CENTER);
        borderPane.setBottom(filenameLabel);
        setCenter(borderPane);
    }

    private void initDisplay() {
        switch (content.getType()) {
            case GEOGEBRA:
                imageView.setPickOnBounds(true);
                imageView.setImage(Images.getInternal("icons_x64/geogebra.png"));
                break;
            case IMAGE:
                try {
                    imageView.setPickOnBounds(true);
                    imageView.setImage(Images.getExternal(directoryPath + content.getFilename()));
                    imageView.setFitWidth(Constants.COL_MIN_WIDTH - 30);
                } catch (IOException e) {
                    Logging.log(Level.WARNING, "Bild konnte nicht geöffnet werden.", e);
                }
                break;
            case VIDEO:
                imageView.setImage(Images.getInternal("icons_x64/video.png"));
                break;
            case WORKSHEET:
                try {
                    PdfDecoder pdfDecoder = new PdfDecoder();
                    pdfDecoder.openPdfFile(directoryPath + File.separator + content.getFilename());
                    Image image = SwingFXUtils.toFXImage(pdfDecoder.getPageAsImage(1), null);
                    pdfDecoder.closePdfFile();
                    imageView.setImage(image);
                    ImageView pdf = new ImageView(Images.getInternal("icons_x64/pdf.png"));
                    pdf.setPreserveRatio(true);
                    pdf.setSmooth(true);
                    pdf.setFitHeight(48);
                    StackPane.setAlignment(imageView, Pos.CENTER);
                    StackPane.setAlignment(pdf, Pos.CENTER);
                    StackPane stackPane = new StackPane(imageView, pdf);
                    Platform.runLater(() -> imageView.setImage(stackPane.snapshot(null, null)));
                    if (image.getWidth() > image.getHeight())
                        imageView.setFitWidth(Constants.COL_MIN_WIDTH - 30);
                    else
                        imageView.setFitHeight(128);
                } catch (Exception e) {
                    Logging.log(Level.WARNING, "Vorschau der PDF-Datei " + content.getFilename() +
                            " konnte nicht erzeugt werden", e);
                    imageView.setImage(Images.getInternal("icons_x64/pdf.png"));
                }
                break;
            case EDITABLE_WORKSHEET:
                imageView.setImage(Images.getInternal("icons_x64/editable_worksheet.png"));
                break;
            default:
                imageView.setImage(Images.getInternal("icons_x64/file.png"));
                break;
        }
        Platform.runLater(() -> borderPane.setCenter(imageView));
    }
}
