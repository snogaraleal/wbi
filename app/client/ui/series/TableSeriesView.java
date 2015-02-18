package client.ui.series;

import java.util.List;
import java.util.SortedSet;
import java.util.Map;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

import models.Country;
import models.Point;

import client.managers.SeriesManager;

public class TableSeriesView extends SeriesView {
    public static class NameColumn extends TextColumn<SeriesManager.Row> {
        public static String HEADER = "Country";
        public static String NONE = "Unknown";

        public NameColumn() {
            super();
            setSortable(true);
        }

        @Override
        public String getValue(SeriesManager.Row row) {
            Country country = row.getSeries().getCountry();
            if (country == null) {
                return NONE;
            } else {
                return country.getName();
            }
        }

        private static NameColumn column;

        public static NameColumn get() {
            if (column == null) {
                column = new NameColumn();
            }
            return column;
        }
    }

    public static class YearColumn extends TextColumn<SeriesManager.Row> {
        public static String NONE = "";
        public static Double RES = 100.0;

        private int year;

        public YearColumn(int year) {
            super();
            this.year = year;
            setSortable(true);
        }

        @Override
        public String getValue(SeriesManager.Row row) {
            Point point = row.getSeries().getPointsMap().get(year);
            if (point == null) {
                return NONE;
            } else {
                return ((Double)(((int)(point.getValue() * RES)) / RES)) + "";
            }
        }

        private static Map<Integer, YearColumn> columns =
            new HashMap<Integer, YearColumn>();

        public static YearColumn get(int year) {
            YearColumn column = columns.get(year);
            if (column == null) {
                column = new YearColumn(year);
                columns.put(year, column);
            }
            return column;
        }
    }

    public static class Table extends CellTable<SeriesManager.Row> {
        public Table() {
            super();
        }

        public Table(int pageSize) {
            super(pageSize);
        }
    }

    interface TableSeriesViewUiBinder
        extends UiBinder<Widget, TableSeriesView> {}
    private static TableSeriesViewUiBinder uiBinder =
        GWT.create(TableSeriesViewUiBinder.class);

    @UiField
    FlowPanel container;

    private Table table;

    public TableSeriesView() {
        super();
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void onUpdate(
            List<SeriesManager.Row> rows,
            SortedSet<Integer> years) {

        super.onUpdate(rows, years);

        table = new Table(rows.size());

        container.clear();
        container.add(table);

        table.addColumn(NameColumn.get(), NameColumn.HEADER);

        for (Integer year : years) {
            table.addColumn(YearColumn.get(year), year.toString());
        }

        ListDataProvider<SeriesManager.Row> provider =
            new ListDataProvider<SeriesManager.Row>();

        provider.addDataDisplay(table);
        provider.setList(rows);
    }

    @Override
    public void onChange(SeriesManager.Row row) {
        super.onChange(row);
    }

    @Override
    public void onOrderingChange(SeriesManager.Ordering ordering) {
        super.onOrderingChange(ordering);
    }
}
