package client.ui.series;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiBinder;

public class TableSeriesView extends SeriesView {
    interface TableSeriesViewUiBinder
        extends UiBinder<Widget, TableSeriesView> {}
    private static TableSeriesViewUiBinder uiBinder =
        GWT.create(TableSeriesViewUiBinder.class);

    public TableSeriesView() {
        super();
        initWidget(uiBinder.createAndBindUi(this));
    }
}
