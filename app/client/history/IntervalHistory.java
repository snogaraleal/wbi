package client.history;

import client.managers.history.HistoryManager;
import client.managers.history.HistoryState;
import client.managers.models.IntervalManager;

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
    public void onChange(HistoryState state) {
        Integer startYear = state.getIntervalStartYear();
        Integer endYear = state.getIntervalEndYear();

        if (startYear != null && endYear != null) {
            manager.select(new IntervalManager.Option(startYear, endYear));
        }
    }

    @Override
    public void onSelect(IntervalManager.Option option) {
        HistoryState state = historyManager.getCurrentState();
        state.setInterval(option.getStartYear(), option.getEndYear());
        historyManager.setState(state);
    }
}
