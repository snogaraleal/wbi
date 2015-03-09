package client.serializers;

import java.util.List;
import java.util.SortedSet;

import models.Country;
import models.Series;

import client.managers.models.SeriesManager;

public class CSVSeriesSerializer implements SeriesManager.Serializer {
    private static final String SEP_COLUMN = ", ";
    private static final String SEP_ROW = "\n";

    private static final Double RES = 1000.0;

    @Override
    public String serialize(
            SortedSet<Integer> years, List<SeriesManager.Row> rows) {

        String content = "";

        content += "Name" + SEP_COLUMN;
        content += "ISO";

        for (Integer year : years) {
            content += SEP_COLUMN + year;
        }

        content += SEP_ROW;

        for (SeriesManager.Row row : rows) {
            Series series = row.getSeries();

            Country country = series.getCountry();
            if (country == null) {
                content += SEP_COLUMN;
            } else {
                content += "\"" + country.getName() + "\"" + SEP_COLUMN;
                content += country.getISO();
            }

            for (Integer year : years) {
                content += SEP_COLUMN;

                Double value = series.getPointValue(year);
                if (value != null) {
                    content += (Double)(((int)(value * RES)) / RES) + "";
                }
            }

            content += SEP_ROW;
        }

        return content;
    }
}
