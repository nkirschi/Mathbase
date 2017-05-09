package util;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;

public class XmlFileHandler {
    private Document doc; //Document Objekt der zu ladenden Datei

    /**
     * Der Konstruktor des FileHandlers
     * Hier wird die Datei als in ein Document Objekt verwandelt
     * @param filename Name der zu ladenden Datei
     */
    public XmlFileHandler(String filename){
        DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder=factory.newDocumentBuilder();
            doc=builder.parse(filename);
        } catch (ParserConfigurationException e) { //Hier sollte dringenst noch eine Rückmeldung an Nutzer stattfinden -> Fehler beim Laden des Dokuments
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Methode, um Nodelist aus allen Elementen eines bestimmten XPath-Pfads zu erzeugen,
     * ausgehend von doc
     * Bsp für myExpr: "//elementlist/element[title=\"test\"]" ->liefert alle Elemente mit dem Titel "test"
     * @param myExpr XPath-Pfad
     * @return Nodelist aus allen Elementen mit des Pfades
     */
    public NodeList getNodeListXPath(String myExpr) {
        NodeList tNodelist = null;
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        try {
            tNodelist = (NodeList) xpath.compile(myExpr).evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return tNodelist;
    }

    //ZUM TESTEN; NICHT LÖSCHEN!
    /*public static void main(String[] args){
     *  XmlLoader asd=new XmlLoader("elementlist.xml");
     *   NodeList test=asd.getNodeListXPath("/elementlist/element[title=\"test\"]");
     *   for(int i=0;i<test.getLength();i++){
     *       Node element=test.item(i);
     *       Element sig=(Element)element;
     *       System.out.println(sig.getAttribute("key"));
     *   }
     *}
    */
}
