/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import util.ImageUtil;
import javax.swing.*;
import java.io.IOException;

/**
 * Klasse des Überfensters *gnihihi*
 */
public class AboutDialog extends JDialog {
    public AboutDialog(MainFrame mainFrame) {
        super(mainFrame);
        setTitle("Über Mathbase");
        setSize(500, 300);
        setResizable(false);
        setLocationRelativeTo(mainFrame);
        JPanel panel = new JPanel();
        setContentPane(panel);
        try {
            setIconImage(ImageUtil.getInternalImage("de/apian/mathbase/images/icon.png"));
            panel.add(new JLabel(ImageUtil.getInternalIcon("de/apian/mathbase/images/icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        panel.add(new JLabel("Hier stehen die Mitwirkenden, Sponsoren, Quellen etc."));
        setVisible(true);
    }
}
