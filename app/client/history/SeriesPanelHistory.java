package client.history;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;

import client.managers.history.HistoryManager;
import client.managers.history.HistoryState;
import client.ui.coordinators.TabCoordinator;

/**
 * {@link HistoryManager.Listener} in charge of updating a
 * {@link TabCoordinator} according to the tab name specified in the
 * current {@link HistoryState}.
 */
public class SeriesPanelHistory extends HistoryManager.BaseHistory
    implements SelectionHandler<Integer> {

    /**
     * Currently attached {@code TabCoordinator}.
     */
    private TabCoordinator<?> coordinator;

    /**
     * {@code HandlerRegistration} obtained from registering a
     * {@code SelectionHandler} in the {@code TabLayoutPanel} underlying the
     * attached {@code TabCoordinator}.
     */
    private HandlerRegistration handlerRegistration;

    public SeriesPanelHistory() {}

    /**
     * Attach a {@link TabCoordinator}.
     *
     * @param coordinator Coordinator to attach.
     */
    public void connect(TabCoordinator<?> coordinator) {
        assert this.coordinator == null;

        this.coordinator = coordinator;
        this.handlerRegistration =
            coordinator.getPanel().addSelectionHandler(this);
    }

    /**
     * Detach the currently attached {@link TabCoordinator}.
     */
    public void disconnect() {
        assert this.coordinator != null;

        this.handlerRegistration.removeHandler();
        this.coordinator = null;
    }

    /**
     * Handle a {@code HistoryState} change.
     */
    @Override
    public void onChange(HistoryState state) {
        /*
         * Switch to the tab specified in the current {@code HistoryState}.
         */
        coordinator.selectTab(state.getSeriesTabName());
    }

    /**
     * Handle tab selection.
     */
    @Override
    public void onSelection(SelectionEvent<Integer> event) {
        String tabName = coordinator.getTabName(event.getSelectedItem());

        if (tabName != null) {
            HistoryState state = historyManager.getCurrentState();

            /*
             * Update the current {@code HistoryState} with the name of the
             * selected tab.
             */
            state.setSeriesTabName(tabName);
            historyManager.setState(state);
        }
    }
}
