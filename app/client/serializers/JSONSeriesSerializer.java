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

public class JSONSeriesSerializer implements SeriesManager.Serializer {
    private static final String COUNTRY_NAME = "name";
    private static final String COUNTRY_ISO = "iso";

    private static final Double RES = 1000.0;

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
                            (Double)(((int)(point.getValue() * RES)) / RES)));
                }
            }

            array.set(arrayIndex, rowObject);
            arrayIndex++;
        }

        return array.toString();
    }
}
