package client.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Callback;

import rpc.client.ClientRequest;
import rpc.shared.data.Serializable;
import rpc.shared.data.Type;

import models.Country;
import models.Indicator;

import client.services.WBIExplorationService;

public class HistoryManager implements Manager {
    public static class State implements Serializable {
        public static class Data implements Serializable {
            private Indicator indicator;
            private List<Country> countries;

            public Data() {
            }

            public Data(Indicator indicator, List<Country> countries) {
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

        private String seriesTabName;

        private String intervalStartYear;
        private String intervalEndYear;

        private String indicatorIdent;
        private List<String> countryISOList;

        public State() {
        }

        public State(
                String seriesTabName,
                String intervalStartYear,
                String intervalEndYear,
                String indicatorIdent,
                List<String> countryISOList) {

            this();

            this.seriesTabName = seriesTabName;
            this.intervalStartYear = intervalStartYear;
            this.intervalEndYear = intervalEndYear;
            this.indicatorIdent = indicatorIdent;
            this.countryISOList = countryISOList;
        }

        public String getSeriesTabName() {
            return seriesTabName;
        }

        public String getIntervalStartYear() {
            return intervalStartYear;
        }

        public String getIntervalEndYear() {
            return intervalEndYear;
        }

        public String getIndicatorIdent() {
            return indicatorIdent;
        }

        public List<String> getCountryISOList() {
            return countryISOList;
        }

        private Data data;

        public void setData(Data data) {
            this.data = data;
        }

        public void getData(final Callback<Data, Void> callback) {
            if (data != null) {
                callback.onSuccess(data);
                return;
            }

            WBIExplorationService.getStateData(
                this, new ClientRequest.Listener<Data>() {
                    @Override
                    public void onSuccess(Data data) {
                        setData(data);
                        callback.onSuccess(data);
                    }

                    @Override
                    public void onFailure(ClientRequest.Error error) {
                        callback.onFailure(null);
                    }
                });
        }

        public static String FIELD_INDICATOR_IDENT = "indicator";
        public static String FIELD_COUNTRY_ISO_LIST = "countries";

        @Override
        public Object get(String field) {
            if (field == FIELD_INDICATOR_IDENT) return indicatorIdent;
            if (field == FIELD_COUNTRY_ISO_LIST) return countryISOList;
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void set(String field, Object value) {
            if (field == FIELD_INDICATOR_IDENT) {
                indicatorIdent = (String) value;
            }
            if (field == FIELD_COUNTRY_ISO_LIST) {
                countryISOList = (List<String>) value;
            }
        }

        private static Map<String, Type> fields;

        @Override
        public Map<String, Type> fields() {
            if (fields == null) {
                fields = new HashMap<String, Type>();
                fields.put(FIELD_INDICATOR_IDENT, Type.get(String.class));
                fields.put(
                    FIELD_COUNTRY_ISO_LIST,
                    Type.get(List.class, Type.get(String.class)));
            }
            return fields;
        }
    }

    public static interface Listener {
        void onAdd(HistoryManager historyManager);
        void onRemove(HistoryManager historyManager);
        void onChange(State state);
    }

    public static abstract class BaseHistory implements Listener {
        protected HistoryManager historyManager;

        @Override
        public void onAdd(HistoryManager historyManager) {
            this.historyManager = historyManager;
        }

        @Override
        public void onRemove(HistoryManager historyManager) {
            this.historyManager = null;
        }

        public HistoryManager getCurrentHistoryManager() {
            return historyManager;
        }
    }

    private List<Listener> listeners = new ArrayList<Listener>();

    public HistoryManager() {
    }

    public void setState(State state) {
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
        listener.onAdd(this);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
        listener.onRemove(this);
    }
}
