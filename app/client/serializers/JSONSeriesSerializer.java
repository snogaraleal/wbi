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

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import models.Country;
import models.Point;
import models.Series;

import client.managers.models.SeriesManager;

/**
 * JSON serializer of {@link Row} lists.
 */
public class JSONSeriesSerializer implements SeriesManager.Serializer {
    /**
     * Name of the JSON key with the name of a {@code Country}.
     */
    private static final String COUNTRY_NAME = "name";

    /**
     * Name of the JSON key with the ISO code of a {@code Country}.
     */
    private static final String COUNTRY_ISO = "iso";

    /**
     * Resolution of {@code Point} values.
     */
    private static final Double RES = 1000.0;

    /**
     * Serialize to JSON.
     */
    @Override
    public String serialize(
            SortedSet<Integer> years, List<SeriesManager.Row> rows) {

        JSONArray array = new JSONArray();
        int arrayIndex = 0;

        for (SeriesManager.Row row : rows) {
            JSONObject rowObject = new JSONObject();

            Series series = row.getSeries();

            Country country = series.getCountry();
            if (country != null) {
                rowObject.put(COUNTRY_NAME, new JSONString(country.getName()));
                rowObject.put(COUNTRY_ISO, new JSONString(country.getISO()));
            }

            List<Point> points = series.getPoints();
            if (points != null) {
                for (Point point : points) {
                    rowObject.put(
                        point.getYear() + "",
                        new JSONNumber(
                            ((int)(point.getValue() * RES)) / RES));
                }
            }

            array.set(arrayIndex, rowObject);
            arrayIndex++;
        }

        return array.toString();
    }
}
