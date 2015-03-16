/*
 * WBI Indicator Explorer
 *
 * Copyright 2015 Sebastian Nogara <snogaraleal@gmail.com>
 *
 * This file is part of WBI.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package client.managers.history;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;

import client.managers.Manager;

/**
 * {@link Manager} that keeps track of the current {@link HistoryState}.
 */
public class HistoryManager implements Manager, ValueChangeHandler<String> {
    /**
     * Interface for {@link HistoryManager} listeners.
     */
    public static interface Listener {
        /**
         * Handle connection to {@link HistoryManager}.
         *
         * @param historyManager Manager connected to.
         */
        void onAdd(HistoryManager historyManager);

        /**
         * Handle disconnection from {@link HistoryManager}.
         */
        void onRemove();

        /**
         * Handle change of {@link HistoryState}.
         *
         * @param state Current state.
         */
        void onChange(HistoryState state);
    }

    /**
     * Base class for {@code HistoryManager.Listener} implementers.
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
         *
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
    private HistoryManager() {
        History.addValueChangeHandler(this);
        currentState = HistoryState.fromHistoryToken(History.getToken());
    }

    /**
     * Change the current {@link HistoryState}.
     *
     * @param state New history state.
     */
    public void setState(HistoryState state) {
        currentState = state;
        History.newItem(state.getHistoryToken(), false);
    }

    /**
     * Get the current {@link HistoryState}.
     *
     * @return Current history state.
     */
    public HistoryState getCurrentState() {
        return currentState;
    }

    /**
     * Attach {@code Listener}.
     *
     * @param listener Listener to attach.
     */
    public void addListener(Listener listener) {
        listeners.add(listener);
        listener.onAdd(this);
        listener.onChange(currentState);
    }

    /**
     * Detach {@code Listener}.
     *
     * @param listener Listener to detach.
     */
    public void removeListener(Listener listener) {
        listeners.remove(listener);
        listener.onRemove();
    }

    /**
     * Handle browser history state changes.
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

    /*
     * Singleton
     */

    private static HistoryManager manager;

    public static HistoryManager get() {
        if (manager == null) {
            manager = new HistoryManager();
        }

        return manager;
    }
}
