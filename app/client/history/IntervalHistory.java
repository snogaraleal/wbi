package client.history;

import client.managers.HistoryManager;
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
        assert this.manager != null;

        this.manager.removeListener(this);
        this.manager = null;
    }

    @Override
    public void onChange(HistoryManager.State state) {
    }

    @Override
    public void onSelect(IntervalManager.Option option) {
    }
}
