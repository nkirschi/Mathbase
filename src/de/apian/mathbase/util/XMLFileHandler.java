/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.util;

import org.w3c.dom.Document;
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
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Utility-Klasse zum Laden, Bearbeiten und Speichern von XML-Dateien.
 * <p>
 * Jedes Objekt dieser Klasse bearbeitet immer eine einzige XML-Datei und jede XML-Datei sollte nur von einem
 * {@code XMLFileHandler}-Objekt bearbeitet werden.
 * <p>
 * Die Dateien werden zuerst mithilfe der DOM-API eingelesen und dann als NodeList zurückgegeben,
 * um die Bearbeitung der Datei für andere Klassen möglichst einfach zu gestalten.
 *
 * @author Benedikt Mödl
 * @version 1.0
 * @see <a href="https://www.tutorialspoint.com/java_xml/java_dom_parser.htm">Java DOM Parser</a>
 * @since 1.0
 */
public class XMLFileHandler {

    /**
     * {@code Document}-Objekt der eingelesenen XML-Datei.
     *
     * @see <a href="https://docs.oracle.com/javase/7/docs/api/org/w3c/dom/Document.html">Document</a>
     * @since 1.0
     */
    private Document doc;

    /**
     * Instanziierung eines FileHandlers
     *
     * @param filePath Pfad der zu ladenden Datei relativ zum Arbeitsverzeichnis
     * @throws IOException wenn die Datei nicht geladen oder geparst werden konnte
     * @since 1.0
     */
    public XMLFileHandler(String filePath) throws IOException {
        // Erzeugt eine Fabrik, die den Baumeister erzeugt
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);

        // Erzeugt den Baumeister, der anschließend die Datei einliest
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(filePath);
        } catch (ParserConfigurationException | SAXException ex) {
            throw new IOException("Fehler beim Parsen der Datei", ex);
        }
    }

    /**
     * Speichern des {@code doc}-Objekts als XML-Datei
     *
     * @param targetPath Pfad der Zieldatei relativ zum Arbeitsverzeichnis
     * @throws IOException wenn das Speichern fehlgeschlagen ist
     * @since 1.0
     */
    public void saveDocToXml(String targetPath) throws IOException {
        /*
         * Definition einer Quelle aus dem Document und eines Ziels aus dem Zielpfad
         * für den Transformer, der die XML-Datei schlussendlich erzeugt
         */
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(targetPath));
        TransformerFactory factory = TransformerFactory.newInstance();

        try {
            Transformer transformer = factory.newTransformer();

            // Parameter des Transformers werden so gesetzt, dass die XML-Datei später auch lesbar ist
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            // transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

            transformer.transform(source, result); // Erzeugung der finalen XML-Datei
        } catch (Exception ex) {
            throw new IOException(String.format("Fehler beim Speichern der Datei \"%s\"",
                    Paths.get(targetPath).toAbsolutePath().toString()), ex);
        }
    }

    /**
     * Erzeugen einer {@code NodeList} mithilfe des XPath-Parsers.
     * <p>
     * Erlaubt das leichte Herausfiltern und Bearbeiten bestimmer Inhalte der XML-Datei.
     *
     * @param expr XPath-Ausdruck
     * @return {@code NodeList}, die vom XPath-Parser erzeugt wurde
     * @throws XPathExpressionException wenn {@code myExpr} kein gültiger XPath-Ausdruck ist
     * @see <a href="https://www.tutorialspoint.com/java_xml/java_xpath_parser.htm">Java XPath Parser</a>
     * @since 1.0
     */
    public NodeList getNodeListXPath(String expr) throws XPathExpressionException {
        NodeList nodeList;
        XPathFactory factory = XPathFactory.newInstance();
        XPath xPath = factory.newXPath();
        nodeList = (NodeList) xPath.compile(expr).evaluate(doc, XPathConstants.NODESET);
        return nodeList;
    }

    /**
     * Getter-Methode für das {@code Document}-Objekt
     *
     * @return Das {@code Document}-Objekt
     * @since 1.0
     */
    public Document getDoc() {
        return doc;
    }
}
