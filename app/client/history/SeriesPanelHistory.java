package client.history;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import client.managers.HistoryManager;

public class SeriesPanelHistory extends HistoryManager.BaseHistory
    implements SelectionHandler<Integer> {

    private TabLayoutPanel panel;
    private HandlerRegistration handlerRegistration;

    public SeriesPanelHistory() {
    }

    public void connect(TabLayoutPanel panel) {
        assert this.panel == null;

        this.panel = panel;
        this.handlerRegistration = panel.addSelectionHandler(this);
    }

    public void disconnect() {
        assert this.panel != null;

        this.handlerRegistration.removeHandler();
        this.panel = null;
    }

    @Override
    public void onChange(HistoryManager.State state) {
    }

    @Override
    public void onSelection(SelectionEvent<Integer> event) {
    }
}
