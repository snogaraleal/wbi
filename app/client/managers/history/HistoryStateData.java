package client.managers.history;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rpc.shared.data.Serializable;
import rpc.shared.data.Type;

import models.Country;
import models.Indicator;

public class HistoryStateData implements Serializable {
    private Indicator indicator;
    private List<Country> countries;

    public HistoryStateData() {
    }

    public HistoryStateData(Indicator indicator, List<Country> countries) {
        this();

        this.indicator = indicator;
        this.countries = countries;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public static String FIELD_INDICATOR = "indicator";
    public static String FIELD_COUNTRIES = "countries";

    @Override
    public Object get(String field) {
        if (field == FIELD_INDICATOR) return indicator;
        if (field == FIELD_COUNTRIES) return countries;
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void set(String field, Object value) {
        if (field == FIELD_INDICATOR) indicator = (Indicator) value;
        if (field == FIELD_COUNTRIES) {
            countries = (List<Country>) value;
        }
    }

    private static Map<String, Type> fields;

    @Override
    public Map<String, Type> fields() {
        if (fields == null) {
            fields = new HashMap<String, Type>();
            fields.put(FIELD_INDICATOR, Type.get(Indicator.class));
            fields.put(
                FIELD_COUNTRIES,
                Type.get(List.class, Type.get(Country.class)));
        }
        return fields;
    }
}
