package util;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Klasse, von der später einmal über getter-Methoden die Daten von Exponaten abgefragt werden können.
 * Ziel ist es, dass dann nur Strings returnt werden, um die Verarbeitung zu erleichtern.
 * Welche getter es gibt, hängt davon ab welche benötigt werden ;D.
 *
 * Im Prinzip die einzige Klasse, die mit XmlFileHandler in direktem Kontakt steht.
 */
public class ElementDataHandler {
    private XmlFileHandler elementlistHandler;

    /**
     * Der Konstruktor des DataHandlers
     */
    public ElementDataHandler(){
        try {
            elementlistHandler = new XmlFileHandler("src/elementlist.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
