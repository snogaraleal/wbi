package client.history;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;

import client.managers.history.HistoryManager;
import client.managers.history.HistoryState;
import client.ui.coordinators.TabCoordinator;

public class SeriesPanelHistory extends HistoryManager.BaseHistory
    implements SelectionHandler<Integer> {

    private TabCoordinator<?> coordinator;
    private HandlerRegistration handlerRegistration;

    public SeriesPanelHistory() {
    }

    public void connect(TabCoordinator<?> coordinator) {
        assert this.coordinator == null;

        this.coordinator = coordinator;
        this.handlerRegistration =
            coordinator.getPanel().addSelectionHandler(this);
    }

    public void disconnect() {
        assert this.coordinator != null;

        this.handlerRegistration.removeHandler();
        this.coordinator = null;
    }

    @Override
    public void onChange(HistoryState state) {
        coordinator.selectTab(state.getSeriesTabName());
    }

    @Override
    public void onSelection(SelectionEvent<Integer> event) {
        String tabName = coordinator.getTabName(event.getSelectedItem());
        if (tabName != null) {
            HistoryState state = historyManager.getCurrentState();
            state.setSeriesTabName(tabName);
            historyManager.setState(state);
        }
    }
}
