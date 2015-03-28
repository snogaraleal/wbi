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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import models.Country;
import models.Series;

import client.managers.models.SeriesManager;
import client.ui.components.Chart;

/**
 * {@link SeriesView} displaying a {@link Chart}.
 */
public class ChartSeriesView extends SeriesView {
    public interface ChartSeriesViewUiBinder
        extends UiBinder<Widget, ChartSeriesView> {}
    private static ChartSeriesViewUiBinder uiBinder =
        GWT.create(ChartSeriesViewUiBinder.class);

    /**
     * {@code Chart.Series} by {@code SeriesManager.Row}.
     */
    private Map<SeriesManager.Row, Chart.Series> map =
        new HashMap<SeriesManager.Row, Chart.Series>();

    /**
     * {@code Label} displayed when there is no data.
     */
    @UiField
    public Label label;

    /**
     * {@code Chart} displaying series data.
     */
    @UiField
    public Chart chart;

    /**
     * Time required after data changes to redraw the chart.
     */
    private static final int REDRAW_DELAY = 20;

    /**
     * Timer for redrawing the chart.
     */
    private Timer redrawTimer;

    /**
     * Initialize {@code ChartSeriesView}.
     */
    public ChartSeriesView() {
        super();
        initWidget(uiBinder.createAndBindUi(this));

        redrawTimer = new Timer() {
            @Override
            public void run() {
                if (map.isEmpty()) {
                    label.setVisible(true);
                    chart.setVisible(false);
                } else {
                    label.setVisible(false);
                    chart.setVisible(true);

                    chart.setSeries(map.values());
                }
            }
        };
    }

    /**
     * Schedule a redraw.
     */
    private void scheduleRedraw() {
        redrawTimer.cancel();
        redrawTimer.schedule(REDRAW_DELAY);
    }

    @Override
    public void onUpdate(
            List<SeriesManager.Row> rows,
            SortedSet<Integer> years,
            SeriesManager.Ordering ordering) {

        super.onUpdate(rows, years, ordering);

        map.clear();

        for (SeriesManager.Row row : rows) {
            if (!row.isSelected()) {
                continue;
            }

            Series series = row.getSeries();
            Country country = series.getCountry();

            if (country != null) {
                Chart.Series chartSeries =
                    new Chart.Series(country.getName(), series.getPointsMap());

                map.put(row, chartSeries);
            }
        }

        scheduleRedraw();
    }

    @Override
    public void onChange(SeriesManager.Row row) {
        super.onChange(row);

        if (row.isSelected()) {
            if (!map.containsKey(row)) {
                Series series = row.getSeries();

                Chart.Series chartSeries = new Chart.Series(
                    series.getCountry().getName(), series.getPointsMap());

                map.put(row, chartSeries);
            }
        } else {
            if (map.containsKey(row)) {
                map.remove(row);
            }
        }

        scheduleRedraw();
    }
}
