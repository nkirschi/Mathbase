package util;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Klasse, welche sich um die Verwaltung der Element-Daten handelt;
 * mit Hilfe der XMLFileHandler
 */

//!!!!!!FEHLERVERARBEITUNG MUSS NOCH STATTFINDEN!!!!!!!

public class ElementDataHandler {
    private XMLFileHandler xmlHandler;

    private static String originfile="src/elementlist.xml";
    private static String targetfile="src/elementlist.xml"; //FILEPATHS MÜSSEN NOCH HINZUGEFÜGT WERDEN
    private static ElementDataHandler ELEMENT_DATA_HANDLER=new ElementDataHandler(originfile);

    public ElementDataHandler(String filePath){
        try {
            xmlHandler=new XMLFileHandler(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ElementDataHandler getElementDataHandler(){
        return ELEMENT_DATA_HANDLER;
    }

    /**
     * Methode, die ein bestimmtes Attribut eines Elements zurückgibt
     * @param key Eindeutiger Schlüssel des Elements
     * @param attributeName Name des Attributs
     * @return Wert des Attributs
     */
    public String getElementAttribute(long key, String attributeName) {
        try {
            NodeList list=xmlHandler.getNodeListXPath("//elementlist/element[@key=\""+key+"\"]");
            if(list.getLength()==1){
                Element listelement=(Element)list.item(0);
                return listelement.getAttribute(attributeName);
            }
            else if(list.getLength()==0){

            }
            else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Methode, die alle Filepaths eines bestimmten Elements mit dem bestimmten Typ zurückgibt
     * @param key Eindeutiger Schlüssel des Elements
     * @param type Welche typen zurückgegeben werden sollen
     * @return Array mit allen Filepaths vom Typ type
     */
    public String[] getElementFilePathsByType(long key, int type){
        String[] pathlist=new String[10];          //UNFERTIG
        return pathlist;
    }

    /**
     * Methode, die das die Daten in einer .xml Datei speichert
     */
    public void safeElementData(){
        try {
            xmlHandler.saveDocToXml(targetfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
