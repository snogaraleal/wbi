package client.dashboard.history;

import client.managers.HistoryManager.Fragment;
import client.managers.HistoryManager;
import client.managers.IntervalManager.Option;
import client.managers.IntervalManager;

public class IntervalHistory extends HistoryManager.BaseHistory
    implements IntervalManager.Listener {

    private IntervalManager manager;

    public IntervalHistory() {
    }

    public void connect(IntervalManager manager) {
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
    public void onSelect(Option option) {
    }
}
