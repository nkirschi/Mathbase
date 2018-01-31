/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.dialog;

import de.apian.mathbase.gui.MainPane;
import de.apian.mathbase.util.Constants;
import javafx.scene.control.TextField;

public class CaptionDialog extends AbstractTextDialog {
    /**
     * Konstruktion des Inhaltstiteldialogs.
     *
     * @param mainPane Basisanzeigefläche
     * @since 1.0
     */
    public CaptionDialog(MainPane mainPane, String presentCaption) {
        super(mainPane, new TextField(presentCaption));
        setTitle(Constants.BUNDLE.getString("content_management"));
        descriptionLabel.setText(Constants.BUNDLE.getString("caption") + ":");
    }

    @Override
    protected void initOkButton() {

    }
}
