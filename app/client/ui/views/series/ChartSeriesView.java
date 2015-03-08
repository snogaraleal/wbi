package client.ui.views.series;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import models.Country;
import models.Series;

import client.managers.models.SeriesManager;
import client.ui.components.Chart;

public class ChartSeriesView extends SeriesView {
    interface ChartSeriesViewUiBinder
        extends UiBinder<Widget, ChartSeriesView> {}
    private static ChartSeriesViewUiBinder uiBinder =
        GWT.create(ChartSeriesViewUiBinder.class);

    private Map<SeriesManager.Row, Chart.Series> map =
        new HashMap<SeriesManager.Row, Chart.Series>();

    @UiField
    Chart chart;

    public ChartSeriesView() {
        super();
        initWidget(uiBinder.createAndBindUi(this));
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

        chart.setSeries(map.values());
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

        chart.setSeries(map.values());
    }
}
