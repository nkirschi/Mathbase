package util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
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

    public static final String ATTRIBUTE_ = "";
    public static final String FILE_TYPE_PICTURE = "picture";
    public static final String FILE_TYPE_DESCRIPTION = "description";
    public static final String FILE_TYPE_MOVIE = "movie";

    private static String originfile="src/elementlist.xml";
    private static String targetfile="src/elementlist.xml"; //FILEPATHS MÜSSEN NOCH HINZUGEFÜGT WERDEN
    private static ElementDataHandler ELEMENT_DATA_HANDLER=new ElementDataHandler(originfile); //Hält die Reference zum einzigen existierenden Objekt der Klasse ElementDataHandler

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
    public String getElementAttribute(String key, String attributeName) {
        try {
            NodeList nodelist=xmlHandler.getNodeListXPath("//elementlist/element[@key=\""+key+"\"]");
            if(nodelist.getLength()==1){
                Element listelement=(Element)nodelist.item(0);
                return listelement.getAttribute(attributeName);
            }
            else if(nodelist.getLength()==0){

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
    public String[] getElementFilePathsByType(String key, String type){
        String[] pathslist=new String[0];
        try {
            NodeList nodelist=xmlHandler.getNodeListXPath("//elementlist/element[@key=\""+key+"\"]/filepath[@type=\""+type+"\"]");
            pathslist=new String[nodelist.getLength()];
            for(int i=0;i<nodelist.getLength();i++){
                Node inode=nodelist.item(i);
                pathslist[i]=inode.getTextContent();
            }
            return pathslist;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pathslist;
    }

    /**
     * Methode, die eine Liste aller Keys zurückgibt
     * @return Array mit allen Keys
     */
    public String[] getElementKeyList(){
        String[] keylist=new String[0];
        try {
            NodeList nodelist=xmlHandler.getNodeListXPath("//elementlist/element");
            keylist=new String[nodelist.getLength()];
            for(int i=0;i<nodelist.getLength();i++){
                Node inode=nodelist.item(i);
                if(inode.getNodeType()==Node.ELEMENT_NODE){
                    Element ielement=(Element)inode;
                    keylist[i]=ielement.getAttribute("key");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keylist;
    }

    /**
     * Methode, mit der man ein Element hinzufügen kann.
     */
    public void addElement(String key, String theme, String title, String type){
        Element element=xmlHandler.createElement("element");
        element.setAttribute("key",key);
        element.setAttribute("theme",theme);
        element.setAttribute("title",title);
        element.setAttribute("type",type);
        xmlHandler.getRoot().appendChild(element);
    }

    /**
     * Methode, mit der man ein bestimmtes Element löschen kann.
     * @param key Eindeutiger Schlüssel des Elements
     */
    public void deleteElement(String key){

    }

    /**
     * Methode, die die Daten in einer .xml Datei speichert
     */
    public void safeElementData(){
        try {
            xmlHandler.saveDocToXml(targetfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        ElementDataHandler test=ELEMENT_DATA_HANDLER;
        test.addElement("56","Testthema","Toller Titel","film");
        try {
            test.xmlHandler.saveDocToXml(targetfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
