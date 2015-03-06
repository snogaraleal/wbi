package client.services;

import java.util.List;

import rpc.client.ClientRequest;
import rpc.client.ClientRequestFactory;
import rpc.shared.data.Type;

import models.Country;
import models.Indicator;
import models.Series;

public class WBIExplorationService {
    private static ClientRequestFactory factory =
        new ClientRequestFactory("services.WBIExplorationService");

    public static ClientRequest queryIndicatorList(
            String query,
            ClientRequest.Listener<List<Indicator>> listener) {
        return factory
            .call("queryIndicatorList")
            .setArguments(query)
            .setExpected(Type.get(List.class, Type.get(Indicator.class)))
            .addListener(listener)
            .send();
    }

    public static ClientRequest queryCountryList(
            String query,
            ClientRequest.Listener<List<Country>> listener) {
        return factory
            .call("queryCountryList")
            .setArguments(query)
            .setExpected(Type.get(List.class, Type.get(Country.class)))
            .addListener(listener)
            .send();
    }

    public static ClientRequest querySeriesList(
            Long indicatorId, Integer startYear, Integer endYear,
            ClientRequest.Listener<List<Series>> listener) {
        return factory
            .call("querySeriesList")
            .setArguments(indicatorId, startYear, endYear)
            .setExpected(Type.get(List.class, Type.get(Series.class)))
            .addListener(listener)
            .send();
    }
}
