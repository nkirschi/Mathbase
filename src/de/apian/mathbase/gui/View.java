/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import javafx.scene.layout.Pane;

/**
 * Abstraktion einer GUI-Ansicht
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
interface View {
    /**
     * Einzige Methode des funktionalen Interfaces
     *
     * @return Darzustellende GUI-Komponente
     */
    Pane getComponent();
}