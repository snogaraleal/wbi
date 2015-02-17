package services;

import java.util.List;

import rpc.server.Service;

import models.Country;
import models.Indicator;
import models.Series;

public class WBIExplorationService implements Service {
    private static int LIMIT = 10;

    public static List<Indicator> queryIndicatorList(String query) {
        return Indicator.objects
            .where()
            .icontains("name", query)
            .orderBy("name")
            .setMaxRows(LIMIT)
            .fetch("source", "*")
            .fetch("topics", "*")
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

    public static List<Series> querySeriesList(Long indicatorId) {
        return Series.objects
            .where()
            .eq("indicator.id", indicatorId)
            .query()
            .fetch("indicator", "*")
            .fetch("country", "*")
            .fetch("country.region", "*")
            .fetch("points")
            .findList();
    }
}
