/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.xml;

import de.apian.mathbase.util.Constants;
import de.apian.mathbase.util.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;

/**
 * Betreuer einer XML-Datei für Verwaltungsoperationen wie Laden, Bearbeiten und Speichern.
 * <p>
 * Jedes Objekt dieser Klasse bearbeitet immer eine einzige XML-Datei und jede XML-Datei sollte nur von einem
 * {@code XmlFileHandler}-Objekt bearbeitet werden.
 * <p>
 * Die Dateien werden zuerst mithilfe der DOM-API eingelesen und dann als NodeList zurückgegeben,
 * um die Bearbeitung der Datei für andere Klassen möglichst einfach zu gestalten.
 *
 * @author Benedikt Mödl
 * @version 1.0
 * @see <a href="https://www.tutorialspoint.com/java_xml/java_dom_parser.htm">Java DOM Parser</a>
 * @since 1.0
 */
public class XmlFileHandler {

    /**
     * {@code Document}-Objekt der eingelesenen XML-Datei.
     *
     * @see <a href="https://docs.oracle.com/javase/7/docs/api/org/w3c/dom/Document.html">Document</a>
     * @since 1.0
     */
    private Document document;

    /**
     * Konstruktion eines FileHandlers
     *
     * @param filePath Pfad der zu ladenden Datei relativ zum Arbeitsverzeichnis
     * @throws IOException wenn die Datei nicht geladen oder geparst werden konnte
     * @since 1.0
     */
    public XmlFileHandler(String filePath) throws IOException {
        // Erzeugt eine Fabrik, die den Baumeister erzeugt
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);

        // Erzeugt den Baumeister, der anschließend die Datei einliest
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(filePath);
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("Fehler beim Parsen der Datei", e);
        }
    }

    /**
     * Speichern des {@code document}-Objekts als XML-Datei
     *
     * @param targetPath Pfad der Zieldatei relativ zum Arbeitsverzeichnis
     * @throws IOException wenn das Speichern fehlgeschlagen ist
     * @since 1.0
     */
    public void saveDocToXml(String targetPath) throws IOException {

        // Entfernen von Whitespace
        document.normalize();
        NodeList nodeList = generateNodeListFrom("//text()[normalize-space()='']");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            node.getParentNode().removeChild(node);
        }

        /*
         * Definition einer Quelle aus dem Document und eines Ziels aus dem Zielpfad
         * für den Transformer, der die XML-Datei schlussendlich erzeugt
         */
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(targetPath));
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setAttribute("indent-number", "4");

        try {
            Transformer transformer = factory.newTransformer();

            // Setzen der Parameter, sodass die XML-Datei später auch lesbar ist
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            transformer.transform(source, result); // Erzeugung der finalen XML-Datei
        } catch (Exception e) {
            throw new IOException(String.format("Fehler beim Speichern der Datei \"%s\"",
                    Paths.get(targetPath).toAbsolutePath().toString()), e);
        }
    }

    /**
     * Erzeugen einer {@code NodeList} mithilfe des XPath-Parsers.
     * <p>
     * Diese Methode erlaubt das leichte Herausfiltern und Bearbeiten bestimmer Inhalte der XML-Datei.
     *
     * @param expr XPath-Ausdruck
     * @return {@code NodeList}, die vom XPath-Parser erzeugt wurde
     * @see <a href="https://www.tutorialspoint.com/java_xml/java_xpath_parser.htm">Java XPath Parser</a>
     * @since 1.0
     */
    public NodeList generateNodeListFrom(String expr) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile(expr).evaluate(document, XPathConstants.NODESET);
            return nodeList;
        } catch (XPathException e) {
            /*
             * Dieser Fall kann eigentlich niemals eintreten, da die XPathExpression hardgecoded ist.
             * Sollte unwahrscheinlicherweise doch einmal etwas an der XPath-API geändert werden,
             * wäre das gesamte Programm sowieso erstmal unbrauchbar!
             */
            Logger.log(Level.SEVERE, Constants.FATAL_ERROR_MESSAGE, e);
            throw new InternalError(Constants.FATAL_ERROR_MESSAGE);
        }
    }

    /**
     * @return {@code Document}-Objekt der eingelesenen XML-Datei
     * @since 1.0
     */
    public Document getDocument() {
        return document;
    }
}
