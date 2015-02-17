package client.ui.series;

import java.util.List;
import java.util.SortedSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Composite;

import models.Region;

import client.managers.SeriesManager;

public abstract class SeriesView extends Composite
    implements SeriesManager.View, SeriesManager.Listener {

    private SeriesManager manager;

    public SeriesView() {
    }

    @Override
    public void onUpdate(
            List<SeriesManager.Row> rows, List<Region> regions,
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

    @Override
    public SeriesManager getCurrentManager() {
        return manager;
    }
}
