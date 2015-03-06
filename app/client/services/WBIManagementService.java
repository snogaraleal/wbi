package client.services;

import java.util.List;

import rpc.client.ClientRequest;
import rpc.client.ClientRequestFactory;
import rpc.shared.data.Type;

import models.Indicator;

public class WBIManagementService {
    private static ClientRequestFactory factory =
        new ClientRequestFactory("services.WBIManagementService");

    public static ClientRequest poll(
            List<Long> indicatorIds,
            ClientRequest.Listener<List<Indicator>> listener) {
        return factory
            .call("poll")
            .setArguments(indicatorIds)
            .setExpected(Type.get(List.class, Type.get(Indicator.class)))
            .addListener(listener)
            .send();
    }

    public static ClientRequest load(
            Long indicatorId,
            ClientRequest.Listener<Indicator> listener) {
        return factory
            .call("load")
            .setArguments(indicatorId)
            .setExpected(Type.get(Indicator.class))
            .addListener(listener)
            .send();
    }

    public static ClientRequest unload(
            Long indicatorId,
            ClientRequest.Listener<Indicator> listener) {
        return factory
            .call("unload")
            .setArguments(indicatorId)
            .setExpected(Type.get(Indicator.class))
            .addListener(listener)
            .send();
    }
}
