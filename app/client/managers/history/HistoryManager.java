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

    private HistoryState currentState;

    public HistoryManager() {
        History.addValueChangeHandler(this);
        currentState = new HistoryState(History.getToken());
    }

    public void setState(HistoryState state) {
        currentState = state;
        History.newItem(state.getHistoryToken(), false);
    }

    public HistoryState getCurrentState() {
        return currentState;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
        listener.onAdd(this);
        listener.onChange(currentState);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
        listener.onRemove();
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        HistoryState state = new HistoryState(event.getValue());

        if (!state.isEmpty()) {
            currentState.replace(state);

            History.replaceItem(currentState.getHistoryToken(), false);

            for (Listener listener : listeners) {
                listener.onChange(currentState);
            }
        }
    }
}
