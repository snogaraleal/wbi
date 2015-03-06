package data.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XML {
    private static DocumentBuilderFactory factory =
        DocumentBuilderFactory.newInstance();

    public static Document getDocument(String response) {
        factory.setNamespaceAware(true);

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(
                new ByteArrayInputStream(response.getBytes()));

            document.getDocumentElement().normalize();
            return document;

        } catch (ParserConfigurationException exception) {
            exception.printStackTrace();
            return null;

        } catch (SAXException exception) {
            exception.printStackTrace();
            return null;

        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
