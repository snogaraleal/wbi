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

package client.managers.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.gwt.user.client.Timer;

import rpc.client.ClientRequest;

import models.Country;
import models.Indicator;
import models.Point;
import models.Series;

import client.managers.Manager;
import client.services.WBIExplorationService;

/**
 * {@link Manager} in charge of fetching and providing rows of
 * {@link Series} as needed.
 *
 * @see Manager
 */
public class SeriesManager
    implements
        Manager,
        IntervalManager.Listener,
        IndicatorManager.Listener,
        CountryManager.Listener {

    /**
     * Interface for views that interact with a {@link SeriesManager}.
     */
    public static interface View extends Manager.View<SeriesManager> {}

    /**
     * Wrapper around {@link Series} providing selection and visibility.
     */
    public static class Row {
        /**
         * {@code SeriesManager} that created this {@code Row}.
         */
        private SeriesManager manager;

        /**
         * Wrapped {@code Series} object.
         */
        private Series series;

        /**
         * Whether this row is selected.
         */
        private boolean selected;

        /**
         * Whether this row is visible.
         */
        private boolean visible;

        /**
         * Initialize {@code Row}.
         *
         * @param manager {@code SeriesManager} used to create this row.
         * @param series Wrapped {@code Series} object.
         * @param selected Whether this row is selected.
         * @param visible Whether this row is visible.
         */
        public Row(
                SeriesManager manager, Series series,
                boolean selected, boolean visible) {

            this.manager = manager;
            this.series = series;

            this.selected = selected;
            this.visible = visible;
        }

        /**
         * Initialize {@code Row}.
         *
         * @param manager {@code SeriesManager} used to create this row.
         * @param series Wrapped {@code Series} object.
         * @param selected Whether this row is selected.
         */
        public Row(SeriesManager manager, Series series, boolean selected) {
            this(manager, series, selected, true);
        }

        /**
         * Initialize {@code Row}.
         *
         * @param manager {@code SeriesManager} used to create this row.
         * @param series Wrapped {@code Series} object.
         */
        public Row(SeriesManager manager, Series series) {
            this(manager, series, false);
        }

        /**
         * Get wrapped {@code Series} object.
         *
         * @return Wrapped series.
         */
        public Series getSeries() {
            return series;
        }

        /**
         * Get whether this row is selected.
         *
         * @return Whether this row is selected.
         */
        public boolean isSelected() {
            return selected;
        }

        /**
         * Set whether this row is selected.
         *
         * @param selected Whether this row is selected.
         */
        public void setSelected(boolean selected) {
            this.selected = selected;
            manager.change(this);
        }

        /**
         * Get whether this row is visible.
         *
         * @return Whether this row is visible.
         */
        public boolean isVisible() {
            return visible;
        }

        /**
         * Set whether this row is visible.
         *
         * @param visible Whether this row is visible.
         */
        public void setVisible(boolean visible) {
            this.visible = visible;
            manager.change(this);
        }
    }

    /**
     * Object representing the ordering criteria of a list of
     * {@link Row} objects.
     */
    public static class Ordering {
        /**
         * Whether the order is ascending or descending.
         */
        public static enum Direction {
            /**
             * Ascending {@code Direction}.
             */
            ASC,

            /**
             * Descending {@code Direction}.
             */
            DESC
        }

        /**
         * Order by country name.
         */
        public static int BY_COUNTRY_NAME = 0;

        /**
         * Year by which to order or {@link Ordering#BY_COUNTRY_NAME}.
         */
        private int by;

        /**
         * Order direction.
         */
        private Direction direction;

        /**
         * Initialize {@code Ordering}.
         *
         * @param by Year by which to order or {@link Ordering#BY_COUNTRY_NAME}.
         * @param direction Ordering {@link Direction}.
         */
        public Ordering(int by, Direction direction) {
            this.by = by;
            this.direction = direction;
        }

        /**
         * Initialize {@code Ordering} with default values.
         */
        public Ordering() {
            this(BY_COUNTRY_NAME, Direction.ASC);
        }

        /**
         * Set year by which to order.
         *
         * @param by Year by which to order or {@link Ordering#BY_COUNTRY_NAME}.
         */
        public void setBy(int by) {
            this.by = by;
        }

        /**
         * Get year by which to order.
         *
         * @return Year by which to order or {@link Ordering#BY_COUNTRY_NAME}.
         */
        public int getBy() {
            return by;
        }

        /**
         * Set current {@link Direction}.
         *
         * @param direction Ordering direction.
         */
        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        /**
         * Get current {@link Direction}.
         *
         * @return Ordering direction.
         */
        public Direction getDirection() {
            return direction;
        }

        /**
         * Get whether the current {@link Direction} is ascending.
         *
         * @return Whether the current direction is ascending.
         */
        public boolean isAscending() {
            return direction == Direction.ASC;
        }

        /**
         * Get whether the current {@link Direction} is descending.
         *
         * @return Whether the current direction is descending.
         */
        public boolean isDescending() {
            return direction == Direction.DESC;
        }

        /**
         * {@code java.util.Comparator} of {@link Row} objects based on the
         * criteria defined by an {@link Ordering} object.
         */
        public static class Comparator implements java.util.Comparator<Row> {
            /**
             * Current ordering criteria.
             */
            private Ordering ordering;

            /**
             * Initialize {@code Comparator}.
             *
             * @param ordering {@link Ordering} instance.
             */
            public Comparator(Ordering ordering) {
                this.ordering = ordering;
            }

            /**
             * Compare country names from rows.
             *
             * @param a {@code Row} to compare.
             * @param b {@code Row} to compare.
             * @return Comparation result.
             */
            private int compareCountryName(Row a, Row b) {
                Country countryA = a.getSeries().getCountry();
                Country countryB = b.getSeries().getCountry();

                if (countryA == null && countryB == null) {
                    return 0;
                }

                if (countryA == null) {
                    return 1;
                }

                if (countryB == null) {
                    return -1;
                }

                String nameA = countryA.getName();
                String nameB = countryB.getName();

                return nameA.compareTo(nameB);
            }

            /**
             * Compare {@code Point} values corresponding to the year
             * specified in the current {@code Ordering}.
             *
             * @param a {@code Row} to compare.
             * @param b {@code Row} to compare.
             * @return Comparation result.
             */
            private int compareYear(Row a, Row b) {
                int year = ordering.getBy();

                Double pointA = a.getSeries().getPointValue(year);
                Double pointB = b.getSeries().getPointValue(year);

                if (pointA == null && pointB == null) {
                    return 0;
                }

                if (pointA == null) {
                    return -1;
                }

                if (pointB == null) {
                    return 1;
                }

                return pointA.compareTo(pointB);
            }

            /**
             * Compare {@code Row} objects.
             */
            @Override
            public int compare(Row a, Row b) {
                int value;

                if (ordering.getBy() == BY_COUNTRY_NAME) {
                    value = compareCountryName(a, b);
                } else {
                    value = compareYear(a, b);
                }

                switch (ordering.getDirection()) {
                    case ASC:
                        return value;
                    case DESC:
                        return -value;
                }

                return value;
            }
        }

        /**
         * Create {@link Comparator} with the criteria defined by this
         * {@link Ordering} object.
         *
         * @return Created comparator.
         */
        public Comparator createComparator() {
            return new Comparator(this);
        }
    }

    /**
     * Interface for serializers of {@link Row} lists.
     */
    public static interface Serializer {
        /**
         * Serialize a list of {@link Row} to string.
         *
         * @param years List of years the rows contain information about.
         * @param list List of rows.
         * @return Serialized value.
         */
        String serialize(SortedSet<Integer> years, List<Row> list);
    }

    /**
     * Interface for filters of {@link Row} lists.
     */
    public static interface Filter {
        /**
         * Get whether a {@link Row} matches this filter.
         *
         * @param row Row to match.
         * @return Whether the row matches.
         */
        boolean matches(Row row);
    }

    /**
     * Interface for listeners of changes in {@link Row} objects.
     */
    public static interface Listener {
        /**
         * Handle new list of {@link Row} objects.
         *
         * @param rows New list of rows.
         * @param years List of years the rows contain information about.
         * @param ordering Current {@link Ordering}.
         */
        void onUpdate(
            List<Row> rows,
            SortedSet<Integer> years,
            Ordering ordering);

        /**
         * Handle change in {@link Row} object.
         *
         * @param row Changed row.
         */
        void onChange(Row row);
    }

    /**
     * {@code Listener} objects listening to changes in this manager.
     */
    private List<Listener> listeners = new ArrayList<Listener>();

    /**
     * Delay before querying.
     */
    private static final int QUERY_DELAY = 100;

    /**
     * {@code ClientRequest.Listener} for query requests.
     */
    private ClientRequest.Listener<List<Series>> queryRequestListener;

    /**
     * Timer for scheduling query requests.
     */
    private Timer queryTimer;

    /**
     * Current ordering criteria.
     */
    private Ordering ordering = new Ordering();

    /**
     * List of {@code Row} objects from last query.
     */
    private List<Row> rows = new ArrayList<Row>();

    /**
     * {@code Row} objects by {@code Country}.
     */
    private Map<Country, Row> rowsByCountry = new HashMap<Country, Row>();

    /**
     * Set of years that the current list of rows contains information about.
     */
    private SortedSet<Integer> years = new TreeSet<Integer>();

    /**
     * Attached {@code IntervalManager} providing an
     * {@code IntervalManager.Option}.
     */
    private IntervalManager intervalManager;

    /**
     * Attached {@code IndicatorManager} providing an {@code Indicator}.
     */
    private IndicatorManager indicatorManager;

    /**
     * Attached {@code CountryManager} providing a {@code Country}.
     */
    private CountryManager countryManager;

    /**
     * Initialize {@code SeriesManager}.
     */
    public SeriesManager() {
        /*
         * Initialize {@code ClientRequest.Listener} for queries.
         */
        queryRequestListener = new ClientRequest.Listener<List<Series>>() {
            @Override
            public void onSuccess(List<Series> series) {
                load(series);
            }

            @Override
            public void onFailure(ClientRequest.Error error) {
            }
        };

        /*
         * Initialize timer for queries.
         */
        queryTimer = new Timer() {
            @Override
            public void run() {
                Indicator indicator =
                    getCurrentIndicatorManager().getSelectedIndicator();
                IntervalManager.Option option =
                    getCurrentIntervalManager().getSelectedOption();

                if (indicator != null && option != null) {
                    WBIExplorationService.querySeriesList(
                        indicator.getId(),
                        option.getStartYear(), option.getEndYear(),
                        queryRequestListener);
                }
            }
        };
    }

    /**
     * Serialize rows provided by this {@link SeriesManager}.
     *
     * @param serializer {@link Serializer} implementing serialization.
     * @return Serialized contents.
     */
    public String serialize(Serializer serializer) {
        return serializer.serialize(years, rows);
    }

    /**
     * Schedule a series query.
     */
    private void scheduleQuery() {
        queryTimer.cancel();
        queryTimer.schedule(QUERY_DELAY);
    }

    /**
     * Attach an {@link IntervalManager}.
     *
     * @param manager Manager to attach.
     */
    public void connect(IntervalManager intervalManager) {
        assert this.intervalManager == null;

        this.intervalManager = intervalManager;
        this.intervalManager.addListener(this);
    }

    /**
     * Detach the currently attached {@link IntervalManager}.
     */
    public void disconnectIntervalManager() {
        assert this.intervalManager != null;

        this.intervalManager.removeListener(this);
        this.intervalManager = null;
    }

    /**
     * Get the currently attached {@link IntervalManager}.
     *
     * @return Currently attached interval manager.
     */
    public IntervalManager getCurrentIntervalManager() {
        return intervalManager;
    }

    @Override
    public void onSelect(IntervalManager.Option option) {
        scheduleQuery();
    }

    /**
     * Attach an {@link IndicatorManager}.
     *
     * @param manager Manager to attach.
     */
    public void connect(IndicatorManager indicatorManager) {
        assert this.indicatorManager == null;

        this.indicatorManager = indicatorManager;
        this.indicatorManager.addListener(this);
    }

    /**
     * Detach the currently attached {@link IndicatorManager}.
     */
    public void disconnectIndicatorManager() {
        assert this.indicatorManager != null;

        this.indicatorManager.removeListener(this);
        this.indicatorManager = null;
    }

    /**
     * Get the currently attached {@link IndicatorManager}.
     *
     * @return Currently attached indicator manager.
     */
    public IndicatorManager getCurrentIndicatorManager() {
        return indicatorManager;
    }

    @Override
    public void onSearch(
            List<Indicator> indicators,
            Indicator selectedIndicator) {
    }

    @Override
    public void onChange(Indicator indicator) {
    }

    @Override
    public void onSelect(final Indicator indicator) {
        scheduleQuery();
    }

    /**
     * Attach a {@link CountryManager}.
     *
     * @param manager Manager to attach.
     */
    public void connect(CountryManager countryManager) {
        assert this.countryManager == null;

        this.countryManager = countryManager;
        this.countryManager.addListener(this);
    }

    /**
     * Detach the currently attached {@link CountryManager}.
     */
    public void disconnectCountryManager() {
        assert this.countryManager != null;

        this.countryManager.removeListener(this);
        this.countryManager = null;
    }

    /**
     * Get the currently attached {@link CountryManager}.
     *
     * @return Currently attached country manager.
     */
    public CountryManager getCurrentCountryManager() {
        return countryManager;
    }

    @Override
    public void onSearch(
            List<Country> countries,
            List<Country> selectedCountries) {
    }

    @Override
    public void onAdd(Country country) {
        Row row = rowsByCountry.get(country);
        if (row != null) {
            row.setSelected(true);
        }
    }

    @Override
    public void onRemove(Country country) {
        Row row = rowsByCountry.get(country);
        if (row != null) {
            row.setSelected(false);
        }
    }

    @Override
    public void onClear(List<Country> selectedCountries) {
        for (Country country : selectedCountries) {
            Row row = rowsByCountry.get(country);
            if (row != null) {
                row.setSelected(false);
            }
        }
    }

    /**
     * Get list of {@link Row} objects from last query.
     *
     * @return List of row objects.
     */
    public List<Row> getRows() {
        return rows;
    }

    /**
     * Get the set of years that the current list of rows contains
     * information about.
     *
     * @return List of years.
     */
    public SortedSet<Integer> getYears() {
        return years;
    }

    /**
     * Get the current {@link Ordering}.
     *
     * @return Current ordering.
     */
    public Ordering getOrdering() {
        return ordering;
    }

    /**
     * Attach {@code Listener}.
     *
     * @param listener Listener to attach.
     */
    public void addListener(Listener listener) {
        listener.onUpdate(rows, years, ordering);
        listeners.add(listener);
    }

    /**
     * Detach {@code Listener}.
     *
     * @param listener Listener to detach.
     */
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    /**
     * Call {@link Listener#onUpdate} on all listeners.
     */
    private void update() {
        for (Listener listener : listeners) {
            listener.onUpdate(rows, years, ordering);
        }
    }

    /**
     * Call {@link Listener#onChange} on all listeners.
     *
     * @param row {@link Row} changed.
     */
    private void change(Row row) {
        for (Listener listener : listeners) {
            listener.onChange(row);
        }
    }

    /**
     * Load a list of {@link Series} objects.
     *
     * @param seriesList List of series.
     */
    private void load(List<Series> seriesList) {
        rows.clear();
        rowsByCountry.clear();
        years.clear();

        List<Country> selectedCountries =
            countryManager.getSelectedCountries();

        for (Series series : seriesList) {
            for (Point point : series.getPoints()) {
                years.add(point.getYear());
            }

            Country country = series.getCountry();

            Row row = new Row(
                this, series, selectedCountries.contains(country));

            rows.add(row);
            rowsByCountry.put(country, row);
        }

        Collections.sort(rows, ordering.createComparator());
        update();
    }

    /**
     * Set the current {@link Ordering}.
     *
     * @param ordering Ordering criteria.
     */
    public void setOrdering(Ordering ordering) {
        this.ordering = ordering;

        Collections.sort(rows, ordering.createComparator());
        update();
    }

    /**
     * Set the current {@link Filter}.
     *
     * @param filter {@code Filter} implementer.
     */
    public void setFilter(Filter filter) {
        for (Row row : rows) {
            boolean visible = filter.matches(row);

            if (visible != row.isVisible()) {
                row.setVisible(visible);
            }
        }
    }
}
