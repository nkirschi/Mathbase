/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import javafx.scene.Parent;
import javafx.scene.control.Label;

/**
 * Themenbaum im Hauptmenü als Unterklasse von <tt>Parent</tt>
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class TopicTree extends Parent {
    /**
     * Statische Instanzreferenz auf das Singleton <tt>TopicTree</tt>
     *
     * @since 1.0
     */
    private static TopicTree instance;

    /**
     * Privater Singleton-Konstruktor
     *
     * @since 1.0
     */
    private TopicTree() {
        Label label = new Label("Text");
        getChildren().add(label);
    }

    /**
     * Singleton-Instanzoperation
     *
     * @return einzige Instanz von <tt>TopicTree</tt>
     */
    static TopicTree getInstance() {
        if (instance == null)
            instance = new TopicTree();
        return instance;
    }
}
