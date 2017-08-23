/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.util;

import org.w3c.dom.Document;

/**
 * Utility-Klasse zum Laden, Bearbeiten und Speichern von XML-Dateien
 * <p>
 * Jedes Objekt dieser Klasse bearbeitet immer eine einzige XML-Datei und jede XML-Datei sollte nur von einem
 * {@code XMLFileHandler} -Objekt bearbeitet werden.
 *
 * Die Dateien werden zuerst mit Hilfe der DOM-API eingelesen und dann als Nodelist zurückgegeben,
 * um die Bearbeitung der Datei für andere Klassen möglichst einfach zu gestalten.
 *
 *
 * @author Benedikt Mödl
 * @version 1.0
 * @see <a href="https://docs.oracle.com/cd/B28359_01/appdev.111/b28394/adx_j_parser.htm#ADXDK3000">Parsing in Java</a>
 * @since 1.0
 */
public class XMLFileHandler {

    /**
     * Dass {@code Document} -Objekt der eingelesenen XML-Datei.
     *
     * @since 1.0
     */
    private Document doc;
}
