package client.services;

import java.util.List;

import rpc.client.ClientRequest;
import rpc.shared.data.Type;

import models.Country;
import models.Indicator;
import models.Series;

public class WBIExplorationService {
    public static final String CLASS_NAME = "services.WBIExplorationService";

    public static ClientRequest<List<Indicator>> queryIndicatorList(
            String query, ClientRequest.Listener<List<Indicator>> listener) {

        return new ClientRequest<List<Indicator>>(
                CLASS_NAME, "queryIndicatorList")
            .setArguments(query)
            .setExpected(Type.get(List.class, Type.get(Indicator.class)))
            .addListener(listener)
            .send();
    }

    public static ClientRequest<List<Country>> queryCountryList(
            String query, ClientRequest.Listener<List<Country>> listener) {

        return new ClientRequest<List<Country>>(CLASS_NAME, "queryCountryList")
            .setArguments(query)
            .setExpected(Type.get(List.class, Type.get(Country.class)))
            .addListener(listener)
            .send();
    }

    public static ClientRequest<List<Series>> querySeriesList(
            Long indicatorId, Integer startYear, Integer endYear,
            ClientRequest.Listener<List<Series>> listener) {

        return new ClientRequest<List<Series>>(CLASS_NAME, "querySeriesList")
            .setArguments(indicatorId, startYear, endYear)
            .setExpected(Type.get(List.class, Type.get(Series.class)))
            .addListener(listener)
            .send();
    }
}
