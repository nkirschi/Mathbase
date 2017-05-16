package util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;
import java.nio.Buffer;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import static util.Logger.*;

/**
 * Klasse, welche sich um die Verwaltung der Element-Daten handelt;
 * mit Hilfe der XMLFileHandler
 */
public class ElementDataHandler {
    private XMLFileHandler xmlHandler;

    public static final String ATTRIBUTE_TITLE = "title";
    public static final String FILE_TYPE_PICTURE = "picture";
    public static final String FILE_TYPE_DESCRIPTION = "description";
    public static final String FILE_TYPE_MOVIE = "movie";

    private static String originfile= "topics.xml";
    private static String backupfile = "topics.xml.bak";
    private static String targetfile= "topics.xml";
    private static ElementDataHandler ELEMENT_DATA_HANDLER=new ElementDataHandler(originfile); //Hält die Reference zum einzigen existierenden Objekt der Klasse ElementDataHandler

    private ElementDataHandler(String filePath){
        try {
            xmlHandler=new XMLFileHandler(filePath);
            log(INFO,"Datei '"+originfile+"' erfolgreich geladen!");
        } catch (FileNotFoundException e) {
            log(WARNING, "Datei \"" + originfile + "\" konnte nicht geladen werden!");
            e.printStackTrace();
            try {
                Files.copy(Paths.get(backupfile), Paths.get(originfile), StandardCopyOption.REPLACE_EXISTING);
                xmlHandler = new XMLFileHandler(filePath);
                log(INFO, "Datei \"" + originfile + "\" wurde aus dem Backup \"" + backupfile + "\" erfolgreich wiederhergestellt!");
            } catch (IOException f) {
                log(ERROR, "Datei \"" + originfile + "\" konnte nicht wiederhergestellt werden!");
                f.printStackTrace();
                try {
                    File file = new File(originfile);
                    file.createNewFile();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                            "<topiclist></topiclist>");
                    writer.close();
                    xmlHandler = new XMLFileHandler(filePath);
                    log(INFO,"Datei '"+originfile+"' wurde neu erstellt!");
                } catch (IOException g) {
                    log(ERROR,"Datei '"+originfile+"' konnte nicht neu erstellt werden!");
                    log(ERROR, e);
                    log(ERROR, f);
                    log(ERROR, g);
                    g.printStackTrace();
                }
            }
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
            Node inode=nodelist.item(0);
            if(inode.getNodeType()==Node.ELEMENT_NODE){
                Element listelement=(Element)inode;
                return listelement.getAttribute(attributeName);
            }
        } catch (IOException e) {
            log(WARNING,e);
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
            NodeList nodelist=xmlHandler.getNodeListXPath("//topiclist/theme/element[@key=\""+key+"\"]/file[@type=\""+type+"\"]");
            pathslist=new String[nodelist.getLength()];
            for(int i=0;i<nodelist.getLength();i++){
                Node inode=nodelist.item(i);
                pathslist[i]=inode.getTextContent();
            }
            return pathslist;
        } catch (IOException e) {
            log(WARNING,e);
        }
        return pathslist;
    }

    /**
     * Methode, die eine Liste aller Keys der Elemente eines bestimmten Themas zurückgibt
     * @param themekey Key des gewünschten Themas
     * @return Array mit allen Keys
     */
    public String[] getElementKeyList(String themekey){
        String[] keylist=new String[0];
        try {
            NodeList nodelist=xmlHandler.getNodeListXPath("//topiclist/theme[@key=\""+themekey+"\"]/element");
            keylist=new String[nodelist.getLength()];
            for(int i=0;i<nodelist.getLength();i++){
                Node inode=nodelist.item(i);
                if(inode.getNodeType()==Node.ELEMENT_NODE){
                    Element ielement=(Element)inode;
                    keylist[i]=ielement.getAttribute("key");
                }
            }
        } catch (IOException e) {
            log(WARNING,e);
        }
        return keylist;
    }

    public String[] getThemeKeyList(){
        String[] keylist=new String[0];
        try {
            NodeList nodelist=xmlHandler.getNodeListXPath("//topiclist/theme");
            keylist=new String[nodelist.getLength()];
            for(int i=0;i<nodelist.getLength();i++){
                Node inode=nodelist.item(i);
                if(inode.getNodeType()==Node.ELEMENT_NODE){
                    Element ielement=(Element)inode;
                    keylist[i]=ielement.getAttribute("key");
                }
            }
        } catch (IOException e) {
            log(WARNING,e);
        }
        return keylist;
    }

    /**
     * Getter-Methode für den Titel eines Themas
     * @param key Der Schlüssel des betreffenden Themas
     * @return Der gesuchte Titel
     */
    public String getTopicName(String key){
        try {
            NodeList nodeList = xmlHandler.getNodeListXPath("//topiclist/theme");
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element topicElement = (Element) nodeList.item(i);
                    if (topicElement.getAttribute("key").equals(key))
                        return topicElement.getAttribute("name");
                }
            }
        } catch (IOException e) {
            log(WARNING, e);
        }
        return "";
    }

    /**
     * Methode, mit der man ein Element hinzufügen kann.
     * @param elementkey Key des neuen Elements
     * @param themekey Key des Themas
     * @param title Titel des Elements
     * @param pathmap Map mit allen Datei-Pfaden
     */
    public void addElement(String elementkey, String themekey, String title, Map<String,String> pathmap){
        Element element=xmlHandler.createElement("element");
        element.setAttribute("key",elementkey);
        element.setAttribute(ATTRIBUTE_TITLE,title);
        for (Map.Entry<String, String> entry : pathmap.entrySet()) {
            Element pathelement=xmlHandler.createElement("file");
            pathelement.setAttribute("type",entry.getValue()); //Map-Value = Typ
            pathelement.setTextContent(entry.getKey()); //Map-Key = Pfad
            element.appendChild(pathelement);
        }
        try {
            NodeList nodelist=xmlHandler.getNodeListXPath("//topiclist/theme[@key=\""+themekey+"\"]");
            Node inode=nodelist.item(0);
            if(inode.getNodeType()==Node.ELEMENT_NODE){
                Element topicelement=(Element)inode;
                topicelement.appendChild(element);
                log(INFO,"Element '"+title+"' erfolgreich hinzugefügt!");
            }
        } catch (IOException e) {
            log(WARNING,e);
        }
    }


    /**
     * Methode, mit der man ein Thema hinzufügen kann
     * @param key Key des neuen Themas
     * @param theme Name des neuen Themas
     */
    public void addTheme(String key, String theme){
        Element element=xmlHandler.createElement("theme");
        element.setAttribute("key", key);
        element.setAttribute("name",theme);
        xmlHandler.getRoot().appendChild(element);
        log(INFO,"Thema '"+theme+"' erfolgreich hinzugefügt!");
    }

    /**
     * Methode, mit der man ein bestimmtes Element löschen kann.
     * @param key Eindeutiger Schlüssel des Elements
     */
    public void deleteElement(String key){
        try {
            NodeList nodelist=xmlHandler.getNodeListXPath("//topiclist/theme/element[@key=\""+key+"\"]");
            Node inode=nodelist.item(0);
            if(inode.getNodeType()==Node.ELEMENT_NODE){
                Element element=(Element)inode;
                element.getParentNode().removeChild(element);
                log(INFO,"Element mit key '"+key+"' erfolgreich gelöscht!");
            }
        } catch (IOException e) {
            log(WARNING,e);
        }
    }

    /**
     * Methode, mit der man ein bestimmtes Thema löschen kann.
     * @param key Eindeutiger Schlüssel des Themas
     */
    public void deleteTheme(String key){
        try {
            NodeList nodelist=xmlHandler.getNodeListXPath("//topiclist/theme[@key=\""+key+"\"]");
            Node inode=nodelist.item(0);
            if(inode.getNodeType()==Node.ELEMENT_NODE){
                Element element=(Element)inode;
                element.getParentNode().removeChild(element);
                log(INFO,"Thema mit key '"+key+"' erfolgreich gelöscht!");
            }
        } catch (IOException e) {
            log(WARNING,e);
        }
    }

    /**
     * Methode, die die Daten in einer .xml Datei speichert
     */
    public void safeElementData(){
        try {
            xmlHandler.saveDocToXml(targetfile);
            log(INFO,"Datei '"+targetfile+"' wurde gespeichert!");
        } catch (IOException e) {
            log(ERROR,"Datei '"+targetfile+"' konnte nicht gespeichert werden!");
            log(ERROR,e);
        }
    }

    //ZUM TESTEN TODO test-Methode entfernen
    private void test(){
        addTheme(String.valueOf(System.nanoTime()),"Thema1");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        addTheme(String.valueOf(System.nanoTime()),"Thema2");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        addTheme(String.valueOf(System.nanoTime()),"Thema3");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(String i:getThemeKeyList()){
            for(int temp=0;temp<3;temp++){
                String title="PDFTest"+temp;
                Map<String,String> map=new HashMap<>();
                map.put("topics/"+ getTopicName(i)+"/"+title+"/description.txt",FILE_TYPE_DESCRIPTION);
                map.put("topics/"+ getTopicName(i)+"/"+title+"/movie.txt",FILE_TYPE_MOVIE);
                map.put("topics/"+ getTopicName(i)+"/"+title+"/picture.txt",FILE_TYPE_PICTURE);
                addElement(String.valueOf(System.nanoTime()),i,title,map);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}