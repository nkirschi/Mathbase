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
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jpedal.PdfDecoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.file.Paths;
import java.util.logging.Level;


public class LinkTile extends AbstractTile {
    public LinkTile(Content content, String directoryPath, ContentPane contentPane, MainPane mainPane) {
        super(content, directoryPath, contentPane, mainPane);

        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCursor(Cursor.HAND);
        imageView.setPickOnBounds(true); // damit auch die Transparenten Teile der Bilder klickbar sind

        switch (content.getType()) {
            case GEOGEBRA:
                imageView.setImage(Images.getInternal("icons_x64/geogebra.png"));
                break;
            case IMAGE:
                try {
                    imageView.setImage(Images.getExternal(directoryPath + content.getFilename()));
                    imageView.setFitWidth(Constants.COL_MIN_WIDTH - 30);
                    setCenter(imageView);
                } catch (IOException e) {
                    Logging.log(Level.WARNING, "Bild konnte nicht geöffnet werden.", e);
                    setCenter(new Label(Constants.BUNDLE.getString("picture_load_fail")));
                }
                break;
            case VIDEO:
                imageView.setImage(Images.getInternal("icons_x64/video.png"));
                break;
            case WORKSHEET:
                new Thread(() -> {
                    try {

                        PDDocument document = PDDocument.load(new File(directoryPath + File.separator + content.getFilename()));

                        PDFRenderer renderer = new PDFRenderer(document);
                        BufferedImage canvas = renderer.renderImageWithDPI(0, 20, ImageType.RGB);
                        Image image = SwingFXUtils.toFXImage(canvas, null);
                        document.close();
/*
                    PdfDecoder pdfDecoder = new PdfDecoder();
                    pdfDecoder.openPdfFile(directoryPath + File.separator + content.getFilename());
                    Image image = SwingFXUtils.toFXImage(pdfDecoder.getPageAsImage(1), null);
                    pdfDecoder.closePdfFile();*/
                        Platform.runLater(() ->
                        imageView.setImage(image));

                    if (image.getWidth() > image.getHeight())
                        imageView.setFitWidth(Constants.COL_MIN_WIDTH - 30);
                    else
                        imageView.setFitHeight(200);
                    } catch (Exception e) {
                        Logging.log(Level.WARNING, "Vorschau der PDF-Datei " + content.getFilename() +
                                " konnte nicht erzeugt werden", e);
                        imageView.setImage(Images.getInternal("icons_x64/pdf.png"));
                    }
                }).start();
                break;
            case EDITABLE_WORKSHEET:
                imageView.setImage(Images.getInternal("icons_x64/editableworksheet.png"));
                break;
            default:
                imageView.setImage(Images.getInternal("icons_x64/file.png"));
                break;
        }


        imageView.setOnMouseClicked(a -> {
            try {
                Desktop.getDesktop().open(Paths.get(directoryPath, content.getFilename()).toFile());
            } catch (IOException e) {
                Logging.log(Level.WARNING, "Datei " + content.getFilename() + " konnte nicht geöffnet werden.");
                new WarningAlert().showAndWait();
            }
        });
        setCenter(imageView);
    }
}
