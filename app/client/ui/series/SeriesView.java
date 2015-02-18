package client.ui.series;

import java.util.List;
import java.util.SortedSet;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import client.managers.SeriesManager;

public abstract class SeriesView extends Composite
    implements SeriesManager.View, SeriesManager.Listener {

    private SeriesManager manager;

    public SeriesView() {
    }

    @Override
    public void onUpdate(
            List<SeriesManager.Row> rows,
            SortedSet<Integer> years) {
    }

    @Override
    public void onChange(SeriesManager.Row row) {
    }

    @Override
    public void onOrderingChange(SeriesManager.Ordering ordering) {
    }

    @Override
    public void onAttach(SeriesManager manager) {
        manager.addListener(this);
        this.manager = manager;
        onUpdate(manager.getRows(), manager.getYears());
    }

    @Override
    public void onDetach(SeriesManager manager) {
        manager.removeListener(this);
        this.manager = null;
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    private static String CLASS_NAME_SCROLL = "series-scroll";
    private static int SCROLL_DELAY = 300;

    public void setScrollEnabled(final boolean enabled) {
        final Element element = (Element) getElement().getFirstChild();
        boolean scrollEnabled = element.hasClassName(CLASS_NAME_SCROLL);

        if ((scrollEnabled && !enabled) || (!scrollEnabled && enabled)) {
            if (enabled) {
                (new Timer() {
                    @Override
                    public void run() {
                        element.addClassName(CLASS_NAME_SCROLL);
                    }
                }).schedule(SCROLL_DELAY);
            } else {
                element.removeClassName(CLASS_NAME_SCROLL);
            }
        }
    }

    @Override
    public SeriesManager getCurrentManager() {
        return manager;
    }
}
