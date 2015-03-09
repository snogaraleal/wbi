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

package client.serializers;

import java.util.List;
import java.util.SortedSet;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

import models.Country;
import models.Point;
import models.Series;

import client.managers.models.SeriesManager;

public class XMLSeriesSerializer implements SeriesManager.Serializer {
    private static final String ROOT = "series";

    private static final String COUNTRY = "country";
    private static final String COUNTRY_NAME = "name";
    private static final String COUNTRY_ISO = "iso";

    private static final String POINT = "point";
    private static final String POINT_YEAR = "year";

    private static final Double RES = 1000.0;

    @Override
    public String serialize(
            SortedSet<Integer> years, List<SeriesManager.Row> rows) {

        Document document = XMLParser.createDocument();

        Element documentElement = document.createElement(ROOT);
        document.appendChild(documentElement);

        for (SeriesManager.Row row : rows) {
            Element rowElement = document.createElement(COUNTRY);

            Series series = row.getSeries();

            Country country = series.getCountry();
            if (country != null) {
                rowElement.setAttribute(COUNTRY_NAME, country.getName());
                rowElement.setAttribute(COUNTRY_ISO, country.getISO());
            }

            List<Point> points = series.getPoints();
            if (points != null) {
                for (Point point : points) {
                    Element pointElement = document.createElement(POINT);

                    pointElement.setAttribute(
                        POINT_YEAR, point.getYear() + "");
                    pointElement.appendChild(document.createTextNode(
                        (Double)(((int)(point.getValue() * RES)) / RES) + ""));

                    rowElement.appendChild(pointElement);
                }
            }

            documentElement.appendChild(rowElement);
        }

        return document.toString();
    }
}
