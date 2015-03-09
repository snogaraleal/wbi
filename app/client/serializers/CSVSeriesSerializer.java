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
