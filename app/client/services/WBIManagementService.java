package client.services;

import java.util.List;

import rpc.client.ClientRequest;
import rpc.shared.data.Type;

import models.Indicator;

public class WBIManagementService {
    public static final String CLASS_NAME = "services.WBIManagementService";

    public static ClientRequest<List<Indicator>> poll(
            List<Long> indicatorIds,
            ClientRequest.Listener<List<Indicator>> listener) {

        return new ClientRequest<List<Indicator>>(CLASS_NAME, "poll")
            .setArguments(indicatorIds)
            .setExpected(Type.get(List.class, Type.get(Indicator.class)))
            .addListener(listener)
            .send();
    }

    public static ClientRequest<Indicator> load(
            Long indicatorId, ClientRequest.Listener<Indicator> listener) {

        return new ClientRequest<Indicator>(CLASS_NAME, "load")
            .setArguments(indicatorId)
            .setExpected(Type.get(Indicator.class))
            .addListener(listener)
            .send();
    }

    public static ClientRequest<Indicator> unload(
            Long indicatorId, ClientRequest.Listener<Indicator> listener) {

        return new ClientRequest<Indicator>(CLASS_NAME, "unload")
            .setArguments(indicatorId)
            .setExpected(Type.get(Indicator.class))
            .addListener(listener)
            .send();
    }
}
