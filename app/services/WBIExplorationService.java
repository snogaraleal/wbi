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

import client.managers.HistoryManager;

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

    public static HistoryManager.State.Data getStateData(
            HistoryManager.State state) {

        return null;
    }
}
