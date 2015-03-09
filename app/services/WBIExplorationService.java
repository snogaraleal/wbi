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

package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rpc.server.Service;

import models.Country;
import models.Indicator;
import models.Point;
import models.Series;

import client.managers.history.HistoryState;
import client.managers.history.HistoryStateData;

public class WBIExplorationService implements Service {
    private static int LIMIT = 10;

    public static List<Indicator> queryIndicatorList(String query) {
        return Indicator.objects
            .where()
                .icontains("name", query)
                .orderBy("name")
            .setMaxRows(LIMIT)
            .fetch("source")
            .fetch("topics")
            .findList();
    }

    public static List<Country> queryCountryList(String query) {
        return Country.objects
            .where()
                .icontains("name", query)
                .orderBy("name")
            .setMaxRows(LIMIT)
            .fetch("region")
            .findList();
    }

    public static List<Series> querySeriesList(
            Long indicatorId, Integer startYear, Integer endYear) {

        List<Point> points = Point.objects
            .where()
                .eq("series.indicator.id", indicatorId)
                .ge("year", startYear)
                .le("year", endYear)
            .query()
            .fetch("series")
            .fetch("series.country")
            .fetch("series.country.region")
            .findList();

        Map<Series, List<Point>> pointsBySeries =
            new HashMap<Series, List<Point>>();

        for (Point point : points) {
            Series series = point.getSeries();

            List<Point> seriesPoints = pointsBySeries.get(series);
            if (seriesPoints == null) {
                seriesPoints = new ArrayList<Point>();
                pointsBySeries.put(series, seriesPoints);
            }

            seriesPoints.add(point);
        }

        List<Series> allSeries = new ArrayList<Series>();

        for (Map.Entry<Series, List<Point>> entry :
                pointsBySeries.entrySet()) {
            Series series = entry.getKey();
            series.setPoints(entry.getValue());
            allSeries.add(series);
        }

        return allSeries;
    }

    public static HistoryStateData getStateData(HistoryState state) {
        Indicator indicator = null;
        String indicatorIdent = state.getIndicatorIdent();
        if (indicatorIdent != null) {
            indicator = Indicator.objects
                .where()
                    .ilike("ident", indicatorIdent)
                .query()
                .findUnique();
        }

        List<Country> countries = null;
        List<String> countryISOList = state.getCountryISOList();
        if (countryISOList != null) {
            countries = Country.objects
                .where()
                    .in("iso", countryISOList)
                .findList();
        }

        return new HistoryStateData(indicator, countries);
    }
}
