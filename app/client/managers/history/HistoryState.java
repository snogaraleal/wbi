package client.managers.history;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Callback;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import rpc.client.ClientRequest;
import rpc.shared.data.Serializable;
import rpc.shared.data.Type;

import client.services.WBIExplorationService;

public class HistoryState implements Serializable {
    private Integer intervalStartYear;
    private Integer intervalEndYear;

    private String seriesTabName;

    private String indicatorIdent;
    private List<String> countryISOList;

    public HistoryState() {
    }

    public HistoryState(
            Integer intervalStartYear,
            Integer intervalEndYear,
            String seriesTabName,
            String indicatorIdent,
            List<String> countryISOList) {

        this();

        this.intervalStartYear = intervalStartYear;
        this.intervalEndYear = intervalEndYear;

        this.seriesTabName = seriesTabName;

        this.indicatorIdent = indicatorIdent;
        this.countryISOList = countryISOList;
    }

    /*
     * ([0-9]*) : ([0-9]*) / ([^/]*) /? ([^/]*) /? ((.{2},?)*)
     *     |          |         |          |           |
     *  Interval   Interval   Series    Indicator   Comma-separated
     *   start       end     tab name     ident     country ISO list
     *
     *       Indicator ident and country ISO list are optional.
     *
     * Examples:
     *
     *      2010:2014/map:europe/ABC.DEF.1234/SE,DK,NO
     *      2010:2014/map:europe/ABC.DEF.1234
     *      2010:2014/map:europe
     */

    private static RegExp REGEX =
        RegExp.compile("([0-9]*):([0-9]*)/([^/]*)/?([^/]*)/?((.{2},?)*)");

    private static final String COUNTRY_ISO_LIST_SEPARATOR = ",";

    private static final int REGEX_INTERVAL_START_YEAR = 1;
    private static final int REGEX_INTERVAL_END_YEAR = 2;
    private static final int REGEX_SERIES_TAB_NAME = 3;
    private static final int REGEX_INDICATOR_IDENT = 4;
    private static final int REGEX_COUNTRY_ISO_LIST = 5;

    public HistoryState(String historyToken) {
        MatchResult result = REGEX.exec(historyToken);

        if (result == null || result.getGroupCount() <= 1) {
            return;
        }

        this.intervalStartYear =
            Integer.valueOf(result.getGroup(REGEX_INTERVAL_START_YEAR));
        this.intervalEndYear =
            Integer.valueOf(result.getGroup(REGEX_INTERVAL_END_YEAR));

        String seriesTabName = result.getGroup(REGEX_SERIES_TAB_NAME);
        if (seriesTabName != null) {
            seriesTabName = seriesTabName.trim();

            if (!seriesTabName.isEmpty()) {
                this.seriesTabName = seriesTabName;
            }
        }

        String indicatorIdent = result.getGroup(REGEX_INDICATOR_IDENT);
        if (indicatorIdent != null) {
            indicatorIdent = indicatorIdent.trim();

            if (!indicatorIdent.isEmpty()) {
                this.indicatorIdent = indicatorIdent;
            }
        }


        String countryISOList = result.getGroup(REGEX_COUNTRY_ISO_LIST);
        if (countryISOList != null) {
            countryISOList = countryISOList.trim();

            if (!countryISOList.isEmpty()) {
                this.countryISOList = Arrays.asList(
                    countryISOList.split(COUNTRY_ISO_LIST_SEPARATOR));
            }
        }
    }

    public String getHistoryToken() {
        if (intervalStartYear == null || intervalEndYear == null ||
                seriesTabName == null) {

            return null;
        }

        String historyToken =
            intervalStartYear + ":" + intervalEndYear +
            "/" + seriesTabName;

        if (indicatorIdent != null) {
            historyToken += "/" + indicatorIdent;
        }

        if (countryISOList != null) {
            historyToken += "/";

            boolean needsComma = false;

            for (String iso : countryISOList) {
                if (needsComma) {
                    historyToken += ",";
                } else {
                    needsComma = true;
                }

                historyToken += iso;
            }
        }

        return historyToken;
    }

    public Integer getIntervalStartYear() {
        return intervalStartYear;
    }

    public Integer getIntervalEndYear() {
        return intervalEndYear;
    }

    public String getSeriesTabName() {
        return seriesTabName;
    }

    public String getIndicatorIdent() {
        return indicatorIdent;
    }

    public List<String> getCountryISOList() {
        return countryISOList;
    }

    private HistoryStateData data;

    public void setData(HistoryStateData data) {
        this.data = data;
    }

    public void getData(final Callback<HistoryStateData, Void> callback) {
        if (data != null) {
            callback.onSuccess(data);
            return;
        }

        WBIExplorationService.getStateData(
            this, new ClientRequest.Listener<HistoryStateData>() {
                @Override
                public void onSuccess(HistoryStateData data) {
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