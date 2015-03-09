/*
 * WBI Indicator Explorer
 *
 * Copyright 2015 Sebastian Nogara <snogaraleal@gmail.com>
 *
 * This file is part of WBI.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
