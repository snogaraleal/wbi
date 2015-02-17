package client.managers;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.gwt.user.client.Timer;

import rpc.client.ClientRequest;

import models.Indicator;
import models.Country;
import models.Series;
import models.Point;
import models.Region;

import client.services.WBIExplorationService;

public class SeriesManager
    implements Manager, IndicatorManager.Listener, CountryManager.Listener {

    public static interface View extends Manager.View<SeriesManager> {
    }

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

        public boolean getSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            manager.change(this);
        }

        public boolean getVisible() {
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

        public static class Comparator implements java.util.Comparator<Row> {
            private Ordering ordering;

            public Comparator(Ordering ordering) {
                this.ordering = ordering;
            }

            private int compareCountryName(Row a, Row b) {
                Country aCountry = a.getSeries().getCountry();
                Country bCountry = b.getSeries().getCountry();

                if (aCountry == null && bCountry == null) {
                    return 0;
                }

                if (aCountry == null) {
                    return 1;
                }

                if (bCountry == null) {
                    return -1;
                }

                String aName = aCountry.getName();
                String bName = bCountry.getName();

                return aName.compareTo(bName);
            }

            private int compareYear(Row a, Row b) {
                int year = ordering.getBy();

                Point aPoint = a.getSeries().getPointsMap().get(year);
                Point bPoint = b.getSeries().getPointsMap().get(year);

                if (aPoint == null && bPoint == null) {
                    return 0;
                }

                if (aPoint == null) {
                    return 1;
                }

                if (bPoint == null) {
                    return -1;
                }

                Double aValue = aPoint.getValue();
                Double bValue = bPoint.getValue();

                return aValue.compareTo(bValue);
            }

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
        String serialize(List<Row> list);
    }

    public static interface Filter {
        boolean matches(Row row);
    }

    public static interface Listener {
        void onUpdate(
            List<Row> row, List<Region> regions,
            SortedSet<Integer> years);
        void onChange(Row row);
        void onOrderingChange(Ordering ordering);
    }

    private List<Listener> listeners = new ArrayList<Listener>();

    private ClientRequest.Listener<List<Series>> queryRequestListener;

    private Ordering ordering = new Ordering();

    private List<Row> rows = new ArrayList<Row>();
    private Map<Country, Row> rowsByCountry = new HashMap<Country, Row>();
    private List<Region> regions = new ArrayList<Region>();
    private SortedSet<Integer> years = new TreeSet<Integer>();

    private IndicatorManager indicatorManager;
    private CountryManager countryManager;

    public SeriesManager() {
        queryRequestListener = new ClientRequest.Listener<List<Series>>() {
            public void onSuccess(
                    ClientRequest request,
                    List<Series> series) {

                load(series);
            }

            public void onFailure(
                    ClientRequest request,
                    ClientRequest.Error error) {
            }
        };
    }

    public void connect(IndicatorManager indicatorManager) {
        indicatorManager.addListener(this);
        this.indicatorManager = indicatorManager;
    }

    public void disconnect(IndicatorManager indicatorManager) {
        indicatorManager.removeListener(this);
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

    private static int QUERY_DELAY = 600;

    @Override
    public void onSelect(final Indicator indicator) {
        (new Timer() {
            @Override
            public void run() {
                WBIExplorationService.querySeriesList(
                    indicator.getId(), queryRequestListener);
            }
        }).schedule(QUERY_DELAY);
    }

    public void connect(CountryManager countryManager) {
        countryManager.addListener(this);
        this.countryManager = countryManager;
    }

    public void disconnect(CountryManager countryManager) {
        countryManager.removeListener(this);
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

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    private void update() {
        for (Listener listener : listeners) {
            listener.onUpdate(rows, regions, years);
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
        regions.clear();
        years.clear();

        List<Country> selectedCountries =
            countryManager.getSelectedCountries();

        for (Series series : seriesList) {
            Country country = series.getCountry();

            if (country != null) {
                Region region = country.getRegion();

                if (!regions.contains(region)) {
                    regions.add(region);
                }
            }

            for (Point point : series.getPoints()) {
                years.add(point.getYear());
            }

            boolean selected = selectedCountries.contains(country);

            Row row = new Row(this, series, selected);

            rows.add(row);
            rowsByCountry.put(country, row);
        }

        Collections.sort(rows, ordering.createComparator());
        update();
    }

    public void setOrdering(Ordering ordering) {
        this.ordering = ordering;

        for (Listener listener : listeners) {
            listener.onOrderingChange(ordering);
        }

        Collections.sort(rows, ordering.createComparator());
        update();
    }

    public void setFilter(Filter filter) {
        for (Row row : rows) {
            boolean visible = filter.matches(row);

            if (visible != row.getVisible()) {
                row.setVisible(visible);
            }
        }
    }
}
