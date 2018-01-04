/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.dialog;

import de.apian.mathbase.gui.MainPane;
import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.FileUtils;
import de.apian.mathbase.xml.TopicTreeController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

/**
 * Titeleingabedialog für ein Thema, z.B. beim Erstellen oder Umbenennen.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */

public class TopicTitleDialog extends AbstractTextDialog {

    /**
     * Themenbaumkontrolleur.
     *
     * @since 1.0
     */
    private TopicTreeController topicTreeController;

    /**
     * Konstruktion des Thementiteldialogs.
     *
     * @param mainPane Basisanzeigefläche
     * @since 1.0
     */
    public TopicTitleDialog(MainPane mainPane, TopicTreeController topicTreeController) {
        super(mainPane, new TextField());
        this.topicTreeController = topicTreeController;
        setTitle(Constants.BUNDLE.getString("topic_management"));
        setInputDescription(Constants.BUNDLE.getString("add_topic"));
    }

    @Override
    protected void initOkButton() {
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, a -> {
            String title = textField.getText();
            if (title == null || title.isEmpty()) {
                infoLabel.setText(Constants.BUNDLE.getString("empty_title"));
                infoLabel.setVisible(true);
                a.consume();
            } else if (topicTreeController.doesExist(textField.getText())) {
                infoLabel.setText(Constants.BUNDLE.getString("existing_title"));
                infoLabel.setVisible(true);
                a.consume();
            }
        });
    }
}
