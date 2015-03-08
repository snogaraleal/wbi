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
import client.ui.components.VectorMap;

public class MapSeriesView extends SeriesView {
    interface MapSeriesViewUiBinder
        extends UiBinder<Widget, MapSeriesView> {}
    private static MapSeriesViewUiBinder uiBinder =
        GWT.create(MapSeriesViewUiBinder.class);

    @UiField
    VectorMap vectorMap;

    public MapSeriesView() {
        super();
        initWidget(uiBinder.createAndBindUi(this));
    }

    public MapSeriesView(VectorMap.Visual visual) {
        this();
        vectorMap.setVisual(visual);
    }

    @Override
    public void onUpdate(
            List<SeriesManager.Row> rows,
            SortedSet<Integer> years,
            SeriesManager.Ordering ordering) {

        super.onUpdate(rows, years, ordering);

        Map<String, Double> data = new HashMap<String, Double>();

        for (SeriesManager.Row row : rows) {
            Series series = row.getSeries();
            Country country = series.getCountry();

            if (country != null) {
                data.put(country.getISO(), series.getAverage());
            }
        }

        vectorMap.setData(data);
    }

    @Override
    public void onChange(SeriesManager.Row row) {
        super.onChange(row);
    }
}
