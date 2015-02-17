package client.ui.series;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiBinder;

public class MapSeriesView extends SeriesView {
    interface MapSeriesViewUiBinder
        extends UiBinder<Widget, MapSeriesView> {}
    private static MapSeriesViewUiBinder uiBinder =
        GWT.create(MapSeriesViewUiBinder.class);

    public MapSeriesView() {
        super();
        initWidget(uiBinder.createAndBindUi(this));
    }
}
