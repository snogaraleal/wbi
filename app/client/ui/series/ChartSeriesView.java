package client.ui.series;

import java.util.List;
import java.util.SortedSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

import client.managers.SeriesManager;

public class ChartSeriesView extends SeriesView {
    interface ChartSeriesViewUiBinder
        extends UiBinder<Widget, ChartSeriesView> {}
    private static ChartSeriesViewUiBinder uiBinder =
        GWT.create(ChartSeriesViewUiBinder.class);

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
    }

    @Override
    public void onChange(SeriesManager.Row row) {
        super.onChange(row);
    }
}
