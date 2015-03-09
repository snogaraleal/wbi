package client.history;

import client.managers.history.HistoryManager;
import client.managers.history.HistoryState;
import client.managers.models.IntervalManager;

/**
 * {@code HistoryManager.BaseHistory} in charge of updating an
 * {@code IntervalManager} depending on the current {@code HistoryState}.
 */
public class IntervalHistory extends HistoryManager.BaseHistory
    implements IntervalManager.Listener {

    /**
     * Currently attached {@code IntervalManager}.
     */
    private IntervalManager manager;

    public IntervalHistory() {}

    /**
     * Attach an {@code IntervalManager}.
     * @param manager Manager to attach.
     */
    public void connect(IntervalManager manager) {
        assert this.manager == null;

        this.manager = manager;
        this.manager.addListener(this);
    }

    /**
     * Detach the currently attached {@code IntervalManager}.
     */
    public void disconnect() {
        assert this.manager != null;

        this.manager.removeListener(this);
        this.manager = null;
    }

    /**
     * Handle a {@code HistoryState} change.
     */
    @Override
    public void onChange(HistoryState state) {
        Integer startYear = state.getIntervalStartYear();
        Integer endYear = state.getIntervalEndYear();

        /*
         * Update the current {@code IntervalManager.Option} when the current
         * {@code HistoryState} specifies an interval.
         */
        if (startYear != null && endYear != null) {
            manager.select(new IntervalManager.Option(startYear, endYear));
        }
    }

    /**
     * Handle selected {@code IntervalManager.Option}.
     */
    @Override
    public void onSelect(IntervalManager.Option option) {
        HistoryState state = historyManager.getCurrentState();

        /*
         * Update the current {@code HistoryState} with the interval of the
         * selected {@code IntervalManager.Option}.
         */
        state.setInterval(option.getStartYear(), option.getEndYear());
        historyManager.setState(state);
    }
}
