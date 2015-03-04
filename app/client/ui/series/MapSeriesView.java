package client.ui.series;

import java.util.List;
import java.util.SortedSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import client.managers.SeriesManager;

import client.ui.components.VectorMap;

public class MapSeriesView extends SeriesView {
    interface MapSeriesViewUiBinder
        extends UiBinder<Widget, MapSeriesView> {}
    private static MapSeriesViewUiBinder uiBinder =
        GWT.create(MapSeriesViewUiBinder.class);

    @UiField
    VectorMap vectorMap;

    private VectorMap.Visual visual;

    public MapSeriesView() {
        super();
        initWidget(uiBinder.createAndBindUi(this));
    }

    public MapSeriesView(VectorMap.Visual visual) {
        this();
        this.visual = visual;
    }

    @Override
    public void onUpdate(
            List<SeriesManager.Row> rows,
            SortedSet<Integer> years,
            SeriesManager.Ordering ordering) {

        super.onUpdate(rows, years, ordering);

        vectorMap.setVisual(visual);
    }

    @Override
    public void onChange(SeriesManager.Row row) {
        super.onChange(row);
    }
}
