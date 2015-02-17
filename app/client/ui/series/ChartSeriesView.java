package client.ui.series;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiBinder;

public class ChartSeriesView extends SeriesView {
    interface ChartSeriesViewUiBinder
        extends UiBinder<Widget, ChartSeriesView> {}
    private static ChartSeriesViewUiBinder uiBinder =
        GWT.create(ChartSeriesViewUiBinder.class);

    public ChartSeriesView() {
        super();
        initWidget(uiBinder.createAndBindUi(this));
    }
}
