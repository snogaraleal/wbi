package client.serializers;

import java.util.List;

import client.managers.models.SeriesManager;

public class JSONSeriesSerializer implements SeriesManager.Serializer {
    @Override
    public String serialize(List<SeriesManager.Row> rows) {
        return null;
    }
}
