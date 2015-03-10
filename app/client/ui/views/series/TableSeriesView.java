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

package client.ui.views.series;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;

import models.Country;

import client.managers.models.SeriesManager;

public class TableSeriesView extends SeriesView
    implements ColumnSortEvent.Handler {

    public static class CountryNameColumn
            extends TextColumn<SeriesManager.Row> {

        public static String HEADER = "Country";
        public static String NONE = "Unknown";

        public CountryNameColumn() {
            super();
            setSortable(true);
            setDefaultSortAscending(true);
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

        private static CountryNameColumn column;

        public static CountryNameColumn get() {
            if (column == null) {
                column = new CountryNameColumn();
            }
            return column;
        }
    }

    public static class YearColumn extends TextColumn<SeriesManager.Row> {
        private static final String NONE = "";
        private static final Double RES = 10.0;

        private int year;

        public YearColumn(int year) {
            super();
            this.year = year;
            setSortable(true);
        }

        public int getYear() {
            return year;
        }

        @Override
        public String getValue(SeriesManager.Row row) {
            Double point = row.getSeries().getPointValue(year);
            if (point == null) {
                return NONE;
            } else {
                return (Double)(((int)(point * RES)) / RES) + "";
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

    public interface TableSeriesViewUiBinder
        extends UiBinder<Widget, TableSeriesView> {}
    private static TableSeriesViewUiBinder uiBinder =
        GWT.create(TableSeriesViewUiBinder.class);

    @UiField
    public FlowPanel container;

    private Table table;
    private SelectionModel<SeriesManager.Row> selectionModel;

    public TableSeriesView() {
        super();
        initWidget(uiBinder.createAndBindUi(this));
    }

    private void updateColumnSortList(
            ColumnSortList columnSortList,
            SeriesManager.Ordering ordering) {

        Column<?, ?> column;
        int by = ordering.getBy();

        if (by == SeriesManager.Ordering.BY_COUNTRY_NAME) {
            column = CountryNameColumn.get();
        } else {
            column = YearColumn.get(by);
        }

        ColumnSortList.ColumnSortInfo columnSortInfo =
            new ColumnSortList.ColumnSortInfo(column, ordering.isAscending());

        columnSortList.clear();
        columnSortList.push(columnSortInfo);
    }

    @Override
    public void onColumnSort(ColumnSortEvent event) {
        SeriesManager.Ordering.Direction direction;
        if (event.isSortAscending()) {
            direction = SeriesManager.Ordering.Direction.ASC;
        } else {
            direction = SeriesManager.Ordering.Direction.DESC;
        }

        Column<?, ?> column = event.getColumn();

        if (column instanceof CountryNameColumn) {
            manager.setOrdering(new SeriesManager.Ordering(
                SeriesManager.Ordering.BY_COUNTRY_NAME, direction));
        }

        if (column instanceof YearColumn) {
            manager.setOrdering(new SeriesManager.Ordering(
                ((YearColumn) column).getYear(), direction));
        }
    }

    @Override
    public void onUpdate(
            List<SeriesManager.Row> rows,
            SortedSet<Integer> years,
            SeriesManager.Ordering ordering) {

        super.onUpdate(rows, years, ordering);

        table = new Table(rows.size());

        container.clear();
        container.add(table);

        table.addColumn(CountryNameColumn.get(), CountryNameColumn.HEADER);

        for (Integer year : years) {
            table.addColumn(YearColumn.get(year), year.toString());
        }

        table.addColumnSortHandler(this);

        updateColumnSortList(table.getColumnSortList(), ordering);

        ListDataProvider<SeriesManager.Row> provider =
            new ListDataProvider<SeriesManager.Row>();

        provider.addDataDisplay(table);
        provider.setList(rows);

        selectionModel = new MultiSelectionModel<SeriesManager.Row>();
        table.setSelectionModel(selectionModel, null);

        for (SeriesManager.Row row : rows) {
            selectionModel.setSelected(row, row.isSelected());
        }
    }

    @Override
    public void onChange(SeriesManager.Row row) {
        super.onChange(row);

        selectionModel.setSelected(row, row.isSelected());
    }
}
