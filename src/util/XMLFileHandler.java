package util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
 *Klasse zum bearbeiten von XML-Dateien
 */

public class XMLFileHandler {
    private Document doc; //Document Objekt der zu ladenden Datei

    /**
     * Der Konstruktor des FileHandlers
     * Hier wird die Datei als in ein Document Objekt verwandelt
     * @param filePath Name der zu ladenden Datei
     */
    public XMLFileHandler(String filePath) throws FileNotFoundException {
        DocumentBuilderFactory dFactory= DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder=dFactory.newDocumentBuilder();
            doc=builder.parse(filePath);
            NodeList whitespace=getNodeListXPath("//text()[normalize-space()='']");
            for (int i = 0; i < whitespace.getLength(); ++i) {
                Node node = whitespace.item(i);
                node.getParentNode().removeChild(node);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileNotFoundException("Error while loading File. File "+filePath+"not loaded!");
        }
    }

    /**
     * Methode, die das Document Objekt in einer Datei speichert
     * @param targetPath Pfad der Zieldatei
     */
    public void saveDocToXml(String targetPath) throws IOException{
        DOMSource source=new DOMSource(doc);
        StreamResult result=new StreamResult(new File(targetPath));
        TransformerFactory tFactory=TransformerFactory.newInstance();
        try {
            Transformer transformer=tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD,"xml");
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            //transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.transform(source,result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error while saving File. File "+targetPath+"not saved!");
        }
    }

    public Node getRoot(){
        return doc.getFirstChild();
    }

    public Element createElement(String tagname){
        return doc.createElement(tagname);
    }


    /**
     * Methode, um Nodelist aus allen Elementen eines bestimmten XPath-Pfads zu erzeugen,
     * ausgehend von doc
     * Bsp für myExpr: "//elementlist/element[title=\"test\"]" ->liefert alle Elemente mit dem Titel "test"
     * @param myExpr XPath-Pfad
     * @return Nodelist aus allen Elementen mit des Pfades
     */
    public NodeList getNodeListXPath(String myExpr) throws IOException {
        NodeList tNodelist = null;
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        try {
            tNodelist = (NodeList) xpath.compile(myExpr).evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            throw new IOException("Error while parsing File with '"+myExpr+"'");
        }
        return tNodelist;
    }
}