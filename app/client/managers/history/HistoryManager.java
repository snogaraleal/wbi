package client.managers.history;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;

import client.managers.Manager;

/**
 * {@code Manager} in charge of keeping track of history changes.
 */
public class HistoryManager implements Manager, ValueChangeHandler<String> {
    /**
     * Interface for history listeners.
     */
    public static interface Listener {
        /**
         * Handle connection to {@code HistoryManager}.
         * @param historyManager Manager connected to.
         */
        void onAdd(HistoryManager historyManager);

        /**
         * Handle disconnection from {@code HistoryManager}.
         */
        void onRemove();

        /**
         * Handle change of {@code HistoryState}.
         * @param state Current state.
         */
        void onChange(HistoryState state);
    }

    /**
     * Base class for {@code HistoryManager.Listener} implementors.
     */
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

        /**
         * Get the {@code HistoryManager} this listener is currently
         * listening to.
         * @return Current {@code HistoryManager}.
         */
        public HistoryManager getCurrentHistoryManager() {
            return historyManager;
        }
    }

    /**
     * {@code Listener} objects listening to changes in this manager.
     */
    private List<Listener> listeners = new ArrayList<Listener>();

    /**
     * Current history state.
     */
    private HistoryState currentState;

    /**
     * Initialize {@code HistoryManager}.
     */
    public HistoryManager() {
        History.addValueChangeHandler(this);
        currentState = HistoryState.fromHistoryToken(History.getToken());
    }

    /**
     * Change the current {@code HistoryState} creating a new history entry.
     * @param state New history state.
     */
    public void setState(HistoryState state) {
        currentState = state;
        History.newItem(state.getHistoryToken(), false);
    }

    /**
     * Get the current {@code HistoryState}.
     * @return Current history state.
     */
    public HistoryState getCurrentState() {
        return currentState;
    }

    /**
     * Attach {@code Listener}.
     * @param listener Listener to attach.
     */
    public void addListener(Listener listener) {
        listeners.add(listener);
        listener.onAdd(this);
        listener.onChange(currentState);
    }

    /**
     * Detach {@code Listener}.
     * @param listener Listener to detach.
     */
    public void removeListener(Listener listener) {
        listeners.remove(listener);
        listener.onRemove();
    }

    /**
     * Handle browser history changes.
     */
    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        HistoryState state = HistoryState.fromHistoryToken(event.getValue());

        if (state != null && !state.isEmpty()) {
            // Update the current {@code HistoryState}
            currentState.replace(state);

            // Re-generate the current history token
            History.replaceItem(currentState.getHistoryToken(), false);

            // Call listeners
            for (Listener listener : listeners) {
                listener.onChange(currentState);
            }
        }
    }
}
