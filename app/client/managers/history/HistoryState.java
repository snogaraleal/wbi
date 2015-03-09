package client.managers.history;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private List<String> countryISOList = new ArrayList<String>();

    private HistoryStateData data;
    private boolean loadingData;
    private List<Callback<HistoryStateData, Void>> dataCallbacks =
        new ArrayList<Callback<HistoryStateData, Void>>();

    public HistoryState() {
        invalidateData();
    }

    public HistoryState(
            Integer intervalStartYear,
            Integer intervalEndYear,
            String seriesTabName,
            String indicatorIdent,
            List<String> countryISOList) {

        this();

        setInterval(intervalStartYear, intervalEndYear);
        setSeriesTabName(seriesTabName);
        setIndicatorIdent(indicatorIdent);
        setCountryISOList(countryISOList);
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

    public static HistoryState fromHistoryToken(String historyToken) {
        MatchResult result = REGEX.exec(historyToken);

        if (result == null || result.getGroupCount() <= 1) {
            return new HistoryState();
        }

        Integer intervalStartYear =
            Integer.valueOf(result.getGroup(REGEX_INTERVAL_START_YEAR));
        Integer intervalEndYear =
            Integer.valueOf(result.getGroup(REGEX_INTERVAL_END_YEAR));

        String seriesTabName = result.getGroup(REGEX_SERIES_TAB_NAME);
        if (seriesTabName != null) {
            seriesTabName = seriesTabName.trim();

            if (seriesTabName.isEmpty()) {
                seriesTabName = null;
            }
        }

        String indicatorIdent = result.getGroup(REGEX_INDICATOR_IDENT);
        if (indicatorIdent != null) {
            indicatorIdent = indicatorIdent.trim();

            if (indicatorIdent.isEmpty()) {
                indicatorIdent = null;
            }
        }

        String countryISOListString = result.getGroup(REGEX_COUNTRY_ISO_LIST);
        List<String> countryISOList = null;
        if (countryISOListString != null) {
            countryISOListString = countryISOListString.trim();

            if (!countryISOListString.isEmpty()) {
                countryISOList = new ArrayList<String>(Arrays.asList(
                    countryISOListString.split(COUNTRY_ISO_LIST_SEPARATOR)));
            }
        }

        return new HistoryState(
            intervalStartYear, intervalEndYear,
            seriesTabName, indicatorIdent, countryISOList);
    }

    public boolean isEmpty() {
        return intervalStartYear == null || intervalEndYear == null ||
            seriesTabName == null;
    }

    public String getHistoryToken() {
        if (isEmpty()) {
            return null;
        }

        String historyToken =
            intervalStartYear + ":" + intervalEndYear +
            "/" + seriesTabName;

        if (indicatorIdent != null) {
            historyToken += "/" + indicatorIdent;

            if (countryISOList != null && !countryISOList.isEmpty()) {
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
        }

        return historyToken;
    }

    public Integer getIntervalStartYear() {
        return intervalStartYear;
    }

    public Integer getIntervalEndYear() {
        return intervalEndYear;
    }

    public void setInterval(Integer startYear, Integer endYear) {
        this.intervalStartYear = startYear;
        this.intervalEndYear = endYear;
    }

    public String getSeriesTabName() {
        return seriesTabName;
    }

    public void setSeriesTabName(String seriesTabName) {
        this.seriesTabName = seriesTabName;
    }

    public String getIndicatorIdent() {
        return indicatorIdent;
    }

    public void setIndicatorIdent(String indicatorIdent) {
        if (this.indicatorIdent != null &&
                this.indicatorIdent.equals(indicatorIdent)) {
            return;
        }

        this.indicatorIdent = indicatorIdent;
        invalidateData();
    }

    public List<String> getCountryISOList() {
        return countryISOList;
    }

    public void setCountryISOList(List<String> countryISOList) {
        if (countryISOList == null) {
            countryISOList = new ArrayList<String>();
        }

        if (this.countryISOList != null) {
            Set<String> countryISOSet = new HashSet<String>(countryISOList);

            countryISOList.clear();
            countryISOList.addAll(countryISOSet);

            List<String> toRemoveCountryISOList =
                new ArrayList<String>(this.countryISOList);
            toRemoveCountryISOList.removeAll(countryISOList);

            if (this.countryISOList.size() == countryISOList.size() &&
                    toRemoveCountryISOList.isEmpty()) {
                return;
            }
        }

        this.countryISOList = countryISOList;
        invalidateData();
    }

    public void replace(HistoryState state) {
        setInterval(state.getIntervalStartYear(), state.getIntervalEndYear());
        setSeriesTabName(state.getSeriesTabName());
        setIndicatorIdent(state.getIndicatorIdent());
        setCountryISOList(state.getCountryISOList());
    }

    private void invalidateData() {
        this.loadingData = false;
        this.data = null;
    }

    private void setData(HistoryStateData data) {
        this.loadingData = false;
        this.data = data;
    }

    public void getData(final Callback<HistoryStateData, Void> callback) {
        if (data != null) {
            callback.onSuccess(data);
            return;
        }

        dataCallbacks.add(callback);

        if (loadingData) {
            return;
        }

        loadingData = true;

        WBIExplorationService.getStateData(
            this, new ClientRequest.Listener<HistoryStateData>() {
                @Override
                public void onSuccess(HistoryStateData data) {
                    setData(data);

                    for (Callback<HistoryStateData, Void> callback :
                            dataCallbacks) {

                        callback.onSuccess(data);
                    }

                    dataCallbacks.clear();
                }

                @Override
                public void onFailure(ClientRequest.Error error) {
                    invalidateData();

                    for (Callback<HistoryStateData, Void> callback :
                            dataCallbacks) {

                        callback.onFailure(null);
                    }

                    dataCallbacks.clear();
                }
            });
    }

    public static final String FIELD_INDICATOR_IDENT = "indicator";
    public static final String FIELD_COUNTRY_ISO_LIST = "countries";

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
