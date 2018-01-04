/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.util;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ResourceBundle;

/**
 * Utility-Klasse mit statischen Konstanten zur universellen Anpassung bestimmter Parameter.
 *
 * @author Nikolas Kirschstein.
 * @version 1.0
 * @since 1.0
 */
public class Constants {

    /**
     * Aktuelle Version des Programms.
     *
     * @since 1.0
     */
    public static final String APP_VERSION = "v1.0";

    /**
     * Vom Klassenpfad ausgehender Pfad zum Bilderpaket.
     *
     * @since 1.0
     */
    public static final String IMAGE_ROOT = "/images/";

    /**
     * Standard-Schriftart.
     *
     * @since 1.0
     */
    public static final String TITLE_FONT_FAMILY =
            Font.loadFont(Constants.class.getResourceAsStream("/fonts/adam.cg_pro.otf"), 11.0).getFamily();

    /**
     * Akzent-Farbe.
     *
     * @since 1.0
     */
    public static final Color ACCENT_COLOR = Color.rgb(29, 105, 224);

    /**
     * Resource-Bundle für die GUI-Texte.
     */
    public static final ResourceBundle BUNDLE = ResourceBundle.getBundle("texts/Mathbase");

    /**
     * Gehashtes Admin-Passwort.
     *
     * @since 1.0
     */
    public static final String HASHED_PASSWORD = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";

    /**
     * Fatale Fehlermeldung für den Wurst Case.
     *
     * @since 1.0
     */
    public static final String FATAL_ERROR_MESSAGE = "Uh-Oh! Kontaktieren Sie umgehend Ihren Systemadministrator!";
}