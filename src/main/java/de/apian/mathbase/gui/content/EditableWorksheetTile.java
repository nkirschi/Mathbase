/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui.content;

import de.apian.mathbase.xml.Content;

/**
 * Kachel eines editierbaren Arbeitsblattes.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class EditableWorksheetTile extends LinkTile {
    public EditableWorksheetTile(Content content, String directoryPath) {
        super(content, directoryPath);
    }
}
