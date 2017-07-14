/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import sun.applet.Main;
import util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static util.Logger.INFO;
import static util.Logger.log;

/**
 * Klasse für die Detailansicht der einzelnen Themen
 */
public class TopicView extends AbstractView {
    private String topicKey;
    private static TopicView instance;

    private TopicView(MainFrame mainFrame, String topicKey) {
        super(mainFrame);
        this.topicKey = topicKey;
        setLayout(new BorderLayout());
        initToolPane();
        log(INFO, "Themenansicht vollständig initialisiert");
    }

    public void update() { //TODO implementieren

    }

    public void updateWithKey(String topicKey) { //TODO weiter implementieren
        this.topicKey = topicKey;
        log(INFO, "Themenansicht für Key '" + topicKey + "' aktualisiert");

        System.out.println(topicKey);
    }

    public static TopicView getInstance(MainFrame mainFrame, String topicKey) {
        if (instance == null)
            instance = new TopicView(mainFrame, topicKey);
        instance.updateWithKey(topicKey);
        return instance;
    }

    /**
     * Hilfsmethode für die Initialisierung der Werkzeugleiste
     */
    private void initToolPane() {
        JPanel toolPane = new JPanel(new BorderLayout());

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Hinzufügen"); //TODO ActionListener
        JButton removeButton = new JButton("Entfernen"); //TODO ActionListener
        JButton backButton = new JButton("Zurück");
        backButton.addActionListener(e -> mainFrame.changeTo(MenuView.getInstance(mainFrame)));
        try {
            addButton.setIcon(ImageUtil.getInternalIcon("de/apian/mathbase/images/add.png", 12, 12));
            removeButton.setIcon(ImageUtil.getInternalIcon("de/apian/mathbase/images/remove.png", 12, 12));
            backButton.setIcon(ImageUtil.getInternalIcon("de/apian/mathbase/images/back.png", 12, 12));
        } catch (IOException e) {
            e.printStackTrace();
        }

        addButton.setIconTextGap(MainFrame.BUTTON_ICON_TEXT_GAP);
        addButton.setMargin(MainFrame.BUTTON_INSETS);
        removeButton.setIconTextGap(MainFrame.BUTTON_ICON_TEXT_GAP);
        removeButton.setMargin(MainFrame.BUTTON_INSETS);
        backButton.setIconTextGap(MainFrame.BUTTON_ICON_TEXT_GAP);
        backButton.setMargin(MainFrame.BUTTON_INSETS);


        buttonPane.add(addButton);
        buttonPane.add(removeButton);
        buttonPane.add(backButton);

        /*
        JPanel searchPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel searchLabel = new JLabel("Suchen:");
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 22));
        searchPane.add(searchLabel);
        searchPane.add(searchField);
        toolPane.add(searchPane, BorderLayout.EAST);
        */

        toolPane.add(buttonPane, BorderLayout.WEST);
        add(toolPane, BorderLayout.SOUTH);
    }
}