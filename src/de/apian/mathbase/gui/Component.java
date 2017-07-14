/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import javafx.scene.Parent;

/**
 * Abstraktion einer GUI-Komponente.
 * <p>
 * Im Gegensatz zu Swing sollten bei JavaFX möglichst keinerlei Bibliotheksklassen mehr beerbt werden.
 * Stattdessen wird das Prinzip der <b>Komposition</b> angewandt:
 * <p>
 * Jede für die Darstellung einer Komponente zuständige GUI-Klasse hält eine Referenz auf die betreffende Komponente
 * und bietet eine entsprechende Operation zur Rückgabe dieser.
 * Es gilt der Grundsatz: "Komposition vor Vererbung" (engl. "Composition over inheritance")
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
interface Component {
    /**
     * Einzige Methode des funktionalen Interfaces
     *
     * @return Darzustellende GUI-Komponente
     */
    Parent getComponent();
}