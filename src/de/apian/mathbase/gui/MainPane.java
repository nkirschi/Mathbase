/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import javafx.scene.layout.BorderPane;

public class MainPane extends BorderPane {
    /**
     * Statische Instanzreferenz auf das Singleton {@code MainMenu}
     *
     * @since 1.0
     */
    private static MainPane instance;

    private MainPane() {
        setCenter(new ContentPane());
        setLeft(new TopicTreePane());
    }

    /**
     * Singleton-Instanzoperation
     *
     * @return einzige Instanz von {@code MainMenu}
     * @since 1.0
     */
    static MainPane getInstance() {
        if (instance == null)
            instance = new MainPane();
        return instance;
    }
}
