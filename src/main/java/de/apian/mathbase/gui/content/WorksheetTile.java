/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.content;

import de.apian.mathbase.gui.MainPane;
import de.apian.mathbase.util.Images;
import de.apian.mathbase.util.Logging;
import de.apian.mathbase.xml.Content;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;

import java.io.File;
import java.util.logging.Level;

/**
 * Arbeitsblattkachel.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
class WorksheetTile extends LinkTile {
    WorksheetTile(Content content, String directoryPath, ContentPane contentPane, MainPane mainPane) {
        super(content, directoryPath, contentPane, mainPane);
    }

    @Override
    protected Image getImage() {
        try {
            PdfDecoder pdfDecoder = new PdfDecoder();
            pdfDecoder.openPdfFile(directoryPath + File.separator + content.getFilename());
            Image image = SwingFXUtils.toFXImage(pdfDecoder.getPageAsImage(1), null);
            pdfDecoder.closePdfFile();
            return image;
        } catch (PdfException e) {
            Logging.log(Level.WARNING, "Vorschau der PDF-Datei " + content.getFilename() + " konnte nicht erzeugt werden", e);

        }
        return Images.getInternal("icons_x64/pdf.png");
    }

    @Override
    protected boolean fitWidth() {
        return true;
    }
}
