package client.managers.history;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;

import client.managers.Manager;

public class HistoryManager implements Manager, ValueChangeHandler<String> {
    public static interface Listener {
        void onAdd(HistoryManager historyManager);
        void onRemove();
        void onChange(HistoryState state);
    }

    public static abstract class BaseHistory implements Listener {
        protected HistoryManager historyManager;

        @Override
        public void onAdd(HistoryManager historyManager) {
            assert this.historyManager == null;

            this.historyManager = historyManager;
        }

        @Override
        public void onRemove() {
            assert this.historyManager != null;

            this.historyManager = null;
        }

        public HistoryManager getCurrentHistoryManager() {
            return historyManager;
        }
    }

    private List<Listener> listeners = new ArrayList<Listener>();

    public HistoryManager() {
        History.addValueChangeHandler(this);
    }

    public void setState(HistoryState state) {
        History.newItem(state.getHistoryToken());
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
        listener.onAdd(this);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
        listener.onRemove();
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        HistoryState state = new HistoryState(event.getValue());

        History.replaceItem(state.getHistoryToken(), false);

        for (Listener listener : listeners) {
            listener.onChange(state);
        }
    }
}
