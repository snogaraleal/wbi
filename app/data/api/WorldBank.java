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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import models.Country;
import models.Indicator;
import models.Point;
import models.Region;
import models.Series;
import models.Source;
import models.Topic;

public class WorldBank {
    public static class API {
        public static String BASE = "http://api.worldbank.org";
        public static String PATH_INDICATORS = "/indicators";
        public static String PATH_COUNTRIES = "/countries";
        public static String PATH_INDICATOR = "/countries/indicators/:ident";

        public static String PER_PAGE = "per_page";
        public static int PER_PAGE_MAX = 30000;
    }

    public static class Schema {
        public static String ID = "id";

        public static String NAMESPACE = "http://www.worldbank.org";

        public static String INDICATORS = "indicators";
        public static String INDICATOR = "indicator";

        public static String TOPICS = "topics";
        public static String TOPIC = "topic";

        public static String COUNTRIES = "countries";
        public static String COUNTRY = "country";

        public static String NAME = "name";
        public static String ISO = "iso2Code";
        public static String REGION = "region";

        public static String SOURCE = "source";
        public static String SOURCE_NOTE = "sourceNote";
        public static String SOURCE_ORGANIZATION = "sourceOrganization";

        public static String DATA = "data";
        public static String DATE = "date";
        public static String VALUE = "value";
        public static String DECIMAL = "decimal";
    }

    private Map<String, Indicator> indicatorMap;
    private Map<Integer, Topic> topicMap;
    private Map<Integer, Source> sourceMap;

    private Map<String, Region> regionMap;
    private Map<String, Country> countryMap;
    private Map<Country, Series> seriesMap;
    private List<Point> pointList;

    public WorldBank() {
    }

    public Map<String, Indicator> getIndicatorMap() {
        return indicatorMap;
    }

    public Map<Integer, Topic> getTopicMap() {
        return topicMap;
    }

    public Map<Integer, Source> getSourceMap() {
        return sourceMap;
    }

    public Map<String, Region> getRegionMap() {
        return regionMap;
    }

    public Map<String, Country> getCountryMap() {
        return countryMap;
    }

    public Map<Country, Series> getSeriesMap() {
        return seriesMap;
    }

    public List<Point> getPointList() {
        return pointList;
    }

    public WorldBank fetchIndicatorList() {
        String[][] params = new String[][] {
            {API.PER_PAGE, Integer.toString(API.PER_PAGE_MAX)}
        };
        String url = HTTP.URL.get(
            API.BASE, API.PATH_INDICATORS, params).toString();

        String response = HTTP.get(url);

        Document document = XML.getDocument(response);

        NodeList nodeList = document.getElementsByTagNameNS(
            Schema.NAMESPACE, Schema.INDICATOR);
        int nodeListLength = nodeList.getLength();

        indicatorMap = new HashMap<String, Indicator>();
        topicMap = new HashMap<Integer, Topic>();
        sourceMap = new HashMap<Integer, Source>();

        for (int i = 0; i < nodeListLength; i++) {
            Element indicatorElement = (Element) nodeList.item(i);

            String indicatorIdent = indicatorElement.getAttribute(Schema.ID);

            if (indicatorMap.containsKey(indicatorIdent)) {
                continue;
            }

            Element nameElement =
                (Element) indicatorElement.getElementsByTagNameNS(
                    Schema.NAMESPACE, Schema.NAME).item(0);
            Element sourceElement =
                (Element) indicatorElement.getElementsByTagNameNS(
                    Schema.NAMESPACE, Schema.SOURCE).item(0);
            Element topicsElement =
                (Element) indicatorElement.getElementsByTagNameNS(
                    Schema.NAMESPACE, Schema.TOPICS).item(0);

            List<Topic> topics = new ArrayList<Topic>();
            NodeList topicNodeList = topicsElement.getElementsByTagNameNS(
                Schema.NAMESPACE, Schema.TOPIC);
            int topicNodeListLength = topicNodeList.getLength();

            for (int n = 0; n < topicNodeListLength; n++) {
                Element topicElement = (Element) topicNodeList.item(n);

                int topicIdent = Integer.valueOf(
                    topicElement.getAttribute(Schema.ID));
                Topic topic = topicMap.get(topicIdent);
                if (topic == null) {
                    topic = new Topic(
                        topicIdent, topicElement.getTextContent());
                    topicMap.put(topicIdent, topic);
                }

                if (!topics.contains(topic)) {
                    topics.add(topic);
                }
            }

            int sourceIdent = Integer.valueOf(
                sourceElement.getAttribute(Schema.ID));
            Source source = sourceMap.get(sourceIdent);
            if (source == null) {
                source = new Source(
                    sourceIdent, sourceElement.getTextContent());
                sourceMap.put(sourceIdent, source);
            }

            indicatorMap.put(indicatorIdent, new Indicator(
                indicatorIdent, nameElement.getTextContent(), topics, source));
        }

        return this;
    }

    public WorldBank fetchCountryList() {
        String[][] params = new String[][] {
            {API.PER_PAGE, Integer.toString(API.PER_PAGE_MAX)}
        };
        String url = HTTP.URL.get(
            API.BASE, API.PATH_COUNTRIES, params).toString();

        String response = HTTP.get(url);
        Document document = XML.getDocument(response);

        NodeList nodeList = document.getElementsByTagNameNS(
            Schema.NAMESPACE, Schema.COUNTRY);
        int nodeListLength = nodeList.getLength();

        regionMap = new HashMap<String, Region>();
        countryMap = new HashMap<String, Country>();

        for (int i = 0; i < nodeListLength; i++) {
            Element countryElement = (Element) nodeList.item(i);

            Element isoElement =
                (Element) countryElement.getElementsByTagNameNS(
                    Schema.NAMESPACE, Schema.ISO).item(0);

            String countryISO = isoElement.getTextContent();

            if (countryMap.containsKey(countryISO)) {
                continue;
            }

            Element nameElement =
                (Element) countryElement.getElementsByTagNameNS(
                    Schema.NAMESPACE, Schema.NAME).item(0);
            Element regionElement =
                (Element) countryElement.getElementsByTagNameNS(
                    Schema.NAMESPACE, Schema.REGION).item(0);

            String regionIdent = regionElement.getAttribute(Schema.ID);

            Region region = regionMap.get(regionIdent);
            if (region == null) {
                region = new Region(
                    regionIdent, regionElement.getTextContent());
                regionMap.put(regionIdent, region);
            }

            countryMap.put(countryISO, new Country(
                countryISO, nameElement.getTextContent(), region));
        }

        return this;
    }

    public WorldBank fetchSeries(
            Indicator indicator, List<Country> countryList) {

        countryMap = new HashMap<String, Country>();
        for (Country country : countryList) {
            countryMap.put(country.getISO(), country);
        }

        String[][] params = new String[][] {
            {API.PER_PAGE, Integer.toString(API.PER_PAGE_MAX)}
        };
        String url = HTTP.URL.get(
            API.BASE,
            API.PATH_INDICATOR.replace(":ident", indicator.getIdent()),
            params).toString();

        String response = HTTP.get(url);
        Document document = XML.getDocument(response);

        NodeList nodeList = document.getElementsByTagNameNS(
            Schema.NAMESPACE, Schema.DATA);
        int nodeListLength = nodeList.getLength();

        seriesMap = new HashMap<Country, Series>();
        pointList = new ArrayList<Point>();

        for (int i = 0; i < nodeListLength; i++) {
            Element dataElement = (Element) nodeList.item(i);

            Element countryElement =
                (Element) dataElement.getElementsByTagNameNS(
                    Schema.NAMESPACE, Schema.COUNTRY).item(0);
            Element dateElement =
                (Element) dataElement.getElementsByTagNameNS(
                    Schema.NAMESPACE, Schema.DATE).item(0);
            Element valueElement =
                (Element) dataElement.getElementsByTagNameNS(
                    Schema.NAMESPACE, Schema.VALUE).item(0);

            String countryISO = countryElement.getAttribute(Schema.ID);
            Country country = countryMap.get(countryISO);

            Series series = seriesMap.get(country);
            if (series == null) {
                series = new Series(indicator, country);
                seriesMap.put(country, series);
            }

            try {
                pointList.add(new Point(
                    series, Integer.valueOf(dateElement.getTextContent()),
                    Double.valueOf(valueElement.getTextContent())));
            } catch (NumberFormatException exception) {
            }
        }

        return this;
    }
}
