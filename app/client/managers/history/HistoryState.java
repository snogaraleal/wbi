/*
 * WBI Indicator Explorer
 *
 * Copyright 2015 Sebastian Nogara <snogaraleal@gmail.com>
 *
 * This file is part of WBI.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

/**
 * Browser history state.
 *
 * @see HistoryManager
 */
public class HistoryState implements Serializable {
    /**
     * Start year of the visualized {@code IntervalManager.Option}.
     */
    private Integer intervalStartYear;

    /**
     * End year of the visualized {@code IntervalManager.Option}.
     */
    private Integer intervalEndYear;

    /**
     * Name of the selected tab from the series tab panel.
     */
    private String seriesTabName;

    /**
     * Identifier of the selected {@code Indicator}.
     */
    private String indicatorIdent;

    /**
     * List of ISO codes from the selected {@code Country} objects.
     */
    private List<String> countryISOList = new ArrayList<String>();

    /*
     * The following fields relate to {@code HistoryStateData} retrieval.
     */

    /**
     * {@code HistoryStateData} that represents the values of this
     * {@code HistoryState} or {@code null} if it must be retrieved.
     */
    private HistoryStateData data;

    /**
     * Whether a {@code HistoryStateData} is being retrieved.
     */
    private boolean loadingData;

    /**
     * {@code Callback} objects awaiting a {@code HistoryStateData}.
     */
    private List<Callback<HistoryStateData, Void>> dataCallbacks =
        new ArrayList<Callback<HistoryStateData, Void>>();

    /**
     * Initialize {@code HistoryState}.
     */
    public HistoryState() {
        invalidateData();
    }

    /**
     * Initialize {@code HistoryState} with specific information about the
     * current state.
     *
     * @param intervalStartYear Start year of the visualized interval.
     * @param intervalEndYear End year of the visualized interval.
     * @param seriesTabName Name of the selected tab from the series panel.
     * @param indicatorIdent Identifier of the current indicator.
     * @param countryISOList List of ISO codes from the selected countries.
     */
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

    /**
     * Regular expression representing the history token format.
     */
    public static final RegExp REGEX =
        RegExp.compile("([0-9]*):([0-9]*)/([^/]*)/?([^/]*)/?((.{2},?)*)");

    private static final String COUNTRY_ISO_LIST_SEPARATOR = ",";

    private static final int REGEX_INTERVAL_START_YEAR = 1;
    private static final int REGEX_INTERVAL_END_YEAR = 2;
    private static final int REGEX_SERIES_TAB_NAME = 3;
    private static final int REGEX_INDICATOR_IDENT = 4;
    private static final int REGEX_COUNTRY_ISO_LIST = 5;

    /**
     * Create a {@code HistoryState} from a history token.
     * 
     * <pre>([0-9]*) : ([0-9]*) / ([^/]*) /? ([^/]*) /? ((.{2},?)*)</pre>
     *
     * <ol>
     *   <li>Interval start</li>
     *   <li>Interval end</li>
     *   <li>Series tab name</li>
     *   <li>Indicator identifier (optional)</li>
     *   <li>Comma-separated list of ISO codes (optional)</li>
     * </ol>
     *
     * Examples:
     *
     * <ul>
     *   <li>2010:2014/map:europe/ABC.DEF.1234/SE,DK,NO</li>
     *   <li>2010:2014/map:europe/ABC.DEF.1234</li>
     *   <li>2010:2014/map:europe</li>
     * </ul>
     *
     * @param historyToken History token.
     * @return Created {@code HistoryState} instance.
     *
     * @see HistoryState#REGEX
     */
    public static HistoryState fromHistoryToken(String historyToken) {
        MatchResult result = REGEX.exec(historyToken);

        if (result == null || result.getGroupCount() <= 1) {
            return new HistoryState();
        }

        /*
         * Interval
         */
        Integer intervalStartYear =
            Integer.valueOf(result.getGroup(REGEX_INTERVAL_START_YEAR));
        Integer intervalEndYear =
            Integer.valueOf(result.getGroup(REGEX_INTERVAL_END_YEAR));

        /*
         * Tab name
         */
        String seriesTabName = result.getGroup(REGEX_SERIES_TAB_NAME);
        if (seriesTabName != null) {
            seriesTabName = seriesTabName.trim();

            if (seriesTabName.isEmpty()) {
                seriesTabName = null;
            }
        }

        /*
         * Indicator
         */
        String indicatorIdent = result.getGroup(REGEX_INDICATOR_IDENT);
        if (indicatorIdent != null) {
            indicatorIdent = indicatorIdent.trim();

            if (indicatorIdent.isEmpty()) {
                indicatorIdent = null;
            }
        }

        /*
         * Country ISO list
         */
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

    /**
     * Whether this {@code HistoryState} is missing information.
     *
     * @return Whether the state is missing information.
     */
    public boolean isEmpty() {
        return intervalStartYear == null || intervalEndYear == null ||
            seriesTabName == null;
    }

    /**
     * Generate a history token with the information in this
     * {@code HistoryState}.
     *
     * @return History token.
     */
    public String getHistoryToken() {
        if (isEmpty()) {
            return null;
        }

        /*
         * Interval and tab name
         */
        String historyToken =
            intervalStartYear + ":" + intervalEndYear +
            "/" + seriesTabName;

        if (indicatorIdent != null) {
            /*
             * Indicator
             */
            historyToken += "/" + indicatorIdent;

            /*
             * Country ISO list
             */
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

    /**
     * Get start year of the visualized interval.
     *
     * @return Interval start year.
     */
    public Integer getIntervalStartYear() {
        return intervalStartYear;
    }

    /**
     * Get end year of the visualized interval.
     *
     * @return Interval end year.
     */
    public Integer getIntervalEndYear() {
        return intervalEndYear;
    }

    /**
     * Set start and end of the visualized interval.
     *
     * @param startYear Start year of the interval.
     * @param endYear End year of the interval.
     */
    public void setInterval(Integer startYear, Integer endYear) {
        this.intervalStartYear = startYear;
        this.intervalEndYear = endYear;
    }

    /**
     * Get the name of the selected series tab.
     *
     * @return Selected tab name.
     */
    public String getSeriesTabName() {
        return seriesTabName;
    }

    /**
     * Set the name of the selected series tab.
     *
     * @param seriesTabName Tab name.
     */
    public void setSeriesTabName(String seriesTabName) {
        this.seriesTabName = seriesTabName;
    }

    /**
     * Get identifier of the selected indicator.
     *
     * @return Indicator identifier.
     */
    public String getIndicatorIdent() {
        return indicatorIdent;
    }

    /**
     * Set the identifier of the selected indicator.
     *
     * @param indicatorIdent Indicator identifier.
     */
    public void setIndicatorIdent(String indicatorIdent) {
        if (this.indicatorIdent != null &&
                this.indicatorIdent.equals(indicatorIdent)) {
            return;
        }

        this.indicatorIdent = indicatorIdent;
        invalidateData();
    }

    /**
     * Get the list of ISO codes corresponding to selected countries.
     *
     * @return List of ISO codes.
     */
    public List<String> getCountryISOList() {
        return countryISOList;
    }

    /**
     * Set the list of ISO codes corresponding to selected countries.
     *
     * @param countryISOList List of ISO codes.
     */
    public void setCountryISOList(List<String> countryISOList) {
        if (countryISOList == null) {
            countryISOList = new ArrayList<String>();
        }

        if (this.countryISOList != null) {
            // Remove duplicates
            Set<String> countryISOSet = new HashSet<String>(countryISOList);

            countryISOList.clear();
            countryISOList.addAll(countryISOSet);

            // Get ISO codes that are not in the new list
            List<String> toRemoveCountryISOList =
                new ArrayList<String>(this.countryISOList);
            toRemoveCountryISOList.removeAll(countryISOList);

            // Return if there are no differences between the lists
            if (this.countryISOList.size() == countryISOList.size() &&
                    toRemoveCountryISOList.isEmpty()) {
                return;
            }
        }

        this.countryISOList = countryISOList;
        invalidateData();
    }

    /**
     * Replace the information in this {@code HistoryState} with information
     * from a different {@code HistoryState}.
     *
     * @param state History state with new values.
     */
    public void replace(HistoryState state) {
        setInterval(state.getIntervalStartYear(), state.getIntervalEndYear());
        setSeriesTabName(state.getSeriesTabName());
        setIndicatorIdent(state.getIndicatorIdent());
        setCountryISOList(state.getCountryISOList());
    }

    /*
     * The following methods relate to {@code HistoryStateData} retrieval.
     */

    /**
     * Invalidate the current {@code HistoryStateData}.
     */
    private void invalidateData() {
        this.loadingData = false;
        this.data = null;
    }

    /**
     * Set the current {@code HistoryStateData}.
     *
     * @param data Up-to-date state data.
     */
    private void setData(HistoryStateData data) {
        this.loadingData = false;
        this.data = data;
    }

    /**
     * Request an up-to-date {@code HistoryStateData}.
     *
     * @param callback {@code Callback} called when ready.
     */
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

        /*
         * Make RPC request
         */
        WBIExplorationService.getStateData(
            this, new ClientRequest.Listener<HistoryStateData>() {
                @Override
                public void onSuccess(HistoryStateData data) {
                    setData(data);

                    // Call callbacks
                    for (Callback<HistoryStateData, Void> callback :
                            dataCallbacks) {

                        callback.onSuccess(data);
                    }

                    dataCallbacks.clear();
                }

                @Override
                public void onFailure(ClientRequest.Error error) {
                    invalidateData();

                    // Call callbacks
                    for (Callback<HistoryStateData, Void> callback :
                            dataCallbacks) {

                        callback.onFailure(null);
                    }

                    dataCallbacks.clear();
                }
            });
    }

    /*
     * {@code Serializable} implementation
     */

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
