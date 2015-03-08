package client.history;

import java.util.List;

import models.Indicator;

import client.managers.history.HistoryManager;
import client.managers.history.HistoryState;
import client.managers.models.IndicatorManager;

public class IndicatorHistory extends HistoryManager.BaseHistory
    implements IndicatorManager.Listener {

    private IndicatorManager manager;

    public IndicatorHistory() {
    }

    public void connect(IndicatorManager manager) {
        assert this.manager == null;

        this.manager = manager;
        this.manager.addListener(this);
    }

    public void disconnect() {
        assert this.manager != null;

        this.manager.removeListener(this);
        this.manager = null;
    }

    @Override
    public void onChange(HistoryState state) {
    }

    @Override
    public void onSearch(
            List<Indicator> indicators,
            Indicator selectedIndicator) {
    }

    @Override
    public void onChange(Indicator indicator) {
    }

    @Override
    public void onSelect(Indicator indicator) {
    }
}
