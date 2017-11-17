/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.util;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Utility-Klasse mit statischen Konstanten zur universellen Anpassung bestimmter Parameter.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class Constants {

    /**
     * Aktuelle Version des Programms
     *
     * @since 1.0
     */
    public static final String APP_VERSION = "1.0";

    /**
     * Vom Klassenpfad ausgehender Pfad zum Bilderpaket
     *
     * @since 1.0
     */
    public static final String IMAGE_ROOT = "de/apian/mathbase/images/";

    /**
     * Standard-Schriftart
     *
     * @since 1.0
     */
    public static final Font DEFAULT_FONT = Font.loadFont(
            Constants.class.getResourceAsStream("/de/apian/mathbase/fonts/ADAM.CG_PRO.otf"), 11.0);

    /**
     * Standard-Schriftfarbe
     *
     * @since 1.0
     */
    public static final Color DEFAULT_COLOR = Color.BLACK;

    /**
     * Für den Wurst Case
     *
     * @since 1.0
     */
    public static final String FATAL_ERROR_MESSAGE = "Uh-Oh! Das schaut nicht gut aus. Kontaktieren Sie umgehend " +
            "Ihren Systemadministrator!";
}