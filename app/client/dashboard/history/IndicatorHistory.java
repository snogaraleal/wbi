package client.dashboard.history;

import java.util.List;

import models.Indicator;

import client.managers.HistoryManager.Fragment;
import client.managers.HistoryManager;
import client.managers.IndicatorManager;

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
        manager.removeListener(this);
        manager = null;
    }

    @Override
    public void onChange(Fragment fragment) {
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
