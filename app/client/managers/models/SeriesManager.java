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
 * {@link Manager} in charge of fetching and providing
 * {@link Series} as needed.
 */
public class SeriesManager
    implements
        Manager,
        IntervalManager.Listener,
        IndicatorManager.Listener,
        CountryManager.Listener {

    /**
     * Interface for views that can be attached to a {@link SeriesManager}
     * in order to display a list of {@link Series} wrapped in {@link Row}
     * objects.
     */
    public static interface View extends Manager.View<SeriesManager> {}

    public static class Row {
        private SeriesManager manager;
        private Series series;

        private boolean selected;
        private boolean visible;

        public Row(
                SeriesManager manager, Series series,
                boolean selected, boolean visible) {

            this.manager = manager;
            this.series = series;

            this.selected = selected;
            this.visible = visible;
        }

        public Row(SeriesManager manager, Series series, boolean selected) {
            this(manager, series, selected, true);
        }

        public Row(SeriesManager manager, Series series) {
            this(manager, series, false);
        }

        public Series getSeries() {
            return series;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            manager.change(this);
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
            manager.change(this);
        }
    }

    public static class Ordering {
        public static enum Direction {
            ASC,
            DESC
        }

        public static int BY_COUNTRY_NAME = 0;

        private int by;
        private Direction direction;

        public Ordering(int by, Direction direction) {
            this.by = by;
            this.direction = direction;
        }

        public Ordering() {
            this(BY_COUNTRY_NAME, Direction.ASC);
        }

        public void setBy(int by) {
            this.by = by;
        }

        public int getBy() {
            return by;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public Direction getDirection() {
            return direction;
        }

        public boolean isAscending() {
            return direction == Direction.ASC;
        }

        public boolean isDescending() {
            return direction == Direction.DESC;
        }

        public static class Comparator implements java.util.Comparator<Row> {
            private Ordering ordering;

            public Comparator(Ordering ordering) {
                this.ordering = ordering;
            }

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

        public Comparator createComparator() {
            return new Comparator(this);
        }
    }

    public static interface Serializer {
        String serialize(SortedSet<Integer> years, List<Row> list);
    }

    public static interface Filter {
        boolean matches(Row row);
    }

    /**
     * Interface for {@link SeriesManager} listeners that listen to 
     * changes in the displayed {@link Row} objects.
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

    private static final int QUERY_DELAY = 100;
    private ClientRequest.Listener<List<Series>> queryRequestListener;
    private Timer queryTimer;

    private Ordering ordering = new Ordering();

    private List<Row> rows = new ArrayList<Row>();
    private Map<Country, Row> rowsByCountry = new HashMap<Country, Row>();
    private SortedSet<Integer> years = new TreeSet<Integer>();

    private IntervalManager intervalManager;
    private IndicatorManager indicatorManager;
    private CountryManager countryManager;

    public SeriesManager() {
        queryRequestListener = new ClientRequest.Listener<List<Series>>() {
            @Override
            public void onSuccess(List<Series> series) {
                load(series);
            }

            @Override
            public void onFailure(ClientRequest.Error error) {
            }
        };

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

    public String serialize(Serializer serializer) {
        return serializer.serialize(years, rows);
    }

    private void scheduleQuery() {
        queryTimer.cancel();
        queryTimer.schedule(QUERY_DELAY);
    }

    public void connect(IntervalManager intervalManager) {
        assert this.intervalManager == null;

        this.intervalManager = intervalManager;
        this.intervalManager.addListener(this);
    }

    public void disconnectIntervalManager() {
        assert this.intervalManager != null;

        this.intervalManager.removeListener(this);
        this.intervalManager = null;
    }

    public IntervalManager getCurrentIntervalManager() {
        return intervalManager;
    }

    @Override
    public void onSelect(IntervalManager.Option option) {
        scheduleQuery();
    }

    public void connect(IndicatorManager indicatorManager) {
        assert this.indicatorManager == null;

        this.indicatorManager = indicatorManager;
        this.indicatorManager.addListener(this);
    }

    public void disconnectIndicatorManager() {
        assert this.indicatorManager != null;

        this.indicatorManager.removeListener(this);
        this.indicatorManager = null;
    }

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

    public void connect(CountryManager countryManager) {
        assert this.countryManager == null;

        this.countryManager = countryManager;
        this.countryManager.addListener(this);
    }

    public void disconnectCountryManager() {
        assert this.countryManager != null;

        this.countryManager.removeListener(this);
        this.countryManager = null;
    }

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

    public List<Row> getRows() {
        return rows;
    }

    public SortedSet<Integer> getYears() {
        return years;
    }

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

    private void update() {
        for (Listener listener : listeners) {
            listener.onUpdate(rows, years, ordering);
        }
    }

    private void change(Row row) {
        for (Listener listener : listeners) {
            listener.onChange(row);
        }
    }

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

    public void setOrdering(Ordering ordering) {
        this.ordering = ordering;

        Collections.sort(rows, ordering.createComparator());
        update();
    }

    public void setFilter(Filter filter) {
        for (Row row : rows) {
            boolean visible = filter.matches(row);

            if (visible != row.isVisible()) {
                row.setVisible(visible);
            }
        }
    }
}
