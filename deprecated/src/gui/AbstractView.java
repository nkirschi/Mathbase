/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import javax.swing.*;

/**
 * Abstrakte Klasse für eine abstrakte Ansicht
 */
public abstract class AbstractView extends JPanel {
    protected MainFrame mainFrame;

    /**
     * @param mainFrame Das für den weiteren Zugriff benötigte Hauptfenster
     */
    public AbstractView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    protected abstract void update();
}
