package client.serializers;

import java.util.List;
import java.util.SortedSet;

import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;

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
