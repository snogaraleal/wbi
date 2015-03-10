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

package client.managers.models;

import java.util.ArrayList;
import java.util.List;

import rpc.client.ClientRequest;

import models.Indicator;

import client.managers.Manager;
import client.managers.models.watcher.IndicatorWatcher;
import client.services.WBIExplorationService;
import client.services.WBIManagementService;

/**
 * {@link Manager} in charge of the {@link Indicator} selection.
 */
public class IndicatorManager implements Manager {
    /**
     * Interface for views that can be attached to an {@link IndicatorManager}
     * in order to change the selected {@link Indicator}.
     */
    public static interface View extends Manager.View<IndicatorManager> {}

    /**
     * Interface for listeners that can be attached to an
     * {@link IndicatorManager} in order to listen to search results and
     * changes in the current {@link Indicator} selection.
     */
    public static interface Listener {
        /**
         * Handle search results.
         *
         * @param indicators Search results.
         * @param selectedIndicator Currently selected indicator.
         */
        void onSearch(
            List<Indicator> indicators,
            Indicator selectedIndicator);

        /**
         * Handle change in {@link Indicator}.
         *
         * @param indicator Indicator that changed.
         */
        void onChange(Indicator indicator);

        /**
         * Handle change of {@link Indicator}.
         *
         * @param indicator Indicator that was just selected.
         */
        void onSelect(Indicator indicator);
    }

    /**
     * {@code Listener} objects listening to changes in this manager.
     */
    private List<Listener> listeners = new ArrayList<Listener>();

    /**
     * {@code ClientRequest.Listener} for search requests.
     */
    private ClientRequest.Listener<List<Indicator>> searchRequestListener;

    /**
     * {@code ClientRequest.Listener} for {@code Indicator} load requests.
     */
    private ClientRequest.Listener<Indicator> loadRequestListener;

    /**
     * {@code ClientRequest.Listener} for {@code Indicator} unload requests.
     */
    private ClientRequest.Listener<Indicator> unloadRequestListener;

    /**
     * Last search query.
     */
    private String lastQuery;

    /**
     * Last search {@code ClientRequest}.
     */
    private ClientRequest<List<Indicator>> lastSearchRequest;

    /**
     * Last search results.
     */
    private List<Indicator> lastSearchIndicators;

    /**
     * Currently selected {@code Indicator}.
     */
    private Indicator selectedIndicator;

    /**
     * {@code IndicatorWatcher} providing real-time changes in
     * {@code Indicator} objects.
     */
    private IndicatorWatcher watcher;

    /**
     * Initialize {@code IndicatorManager}.
     */
    public IndicatorManager() {
        selectedIndicator = null;

        /*
         * Create {@code IndicatorWatcher}.
         */
        watcher = IndicatorWatcher.create();
        watcher.addListener(new IndicatorWatcher.Listener() {
            @Override
            public void onChange(Indicator indicator) {
                change(indicator);

                if (selectedIndicator.equals(indicator)) {
                    if (!selectedIndicator.isReady() && indicator.isReady()) {
                        for (Listener listener : listeners) {
                            listener.onSelect(indicator);
                        }
                    }
                }
            }
        });
        watcher.start();

        /*
         * Initialize {@code ClientRequest.Listener} for search.
         */
        searchRequestListener = new ClientRequest.Listener<List<Indicator>>() {
            @Override
            public void onSuccess(List<Indicator> list) {
                lastSearchIndicators = list;

                for (Listener listener : listeners) {
                    listener.onSearch(list, selectedIndicator);
                }
            }

            @Override
            public void onFailure(ClientRequest.Error error) {
            }
        };

        /*
         * Initialize {@code ClientRequest.Listener} for
         * {@code Indicator} loading.
         */
        loadRequestListener = new ClientRequest.Listener<Indicator>() {
            @Override
            public void onSuccess(Indicator indicator) {
                change(indicator);

                watcher.watchUntilStatusEquals(
                    indicator, Indicator.Status.READY);
            }

            @Override
            public void onFailure(ClientRequest.Error error) {
            }
        };

        /*
         * Initialize {@code ClientRequest.Listener} for
         * {@code Indicator} unloading.
         */
        unloadRequestListener = new ClientRequest.Listener<Indicator>() {
            @Override
            public void onSuccess(Indicator indicator) {
                change(indicator);

                watcher.watchUntilStatusEquals(
                    indicator, Indicator.Status.AVAILABLE);
            }

            @Override
            public void onFailure(ClientRequest.Error error) {
            }
        };
    }

    /**
     * Get the currently selected {@code Indicator}.
     *
     * @return Selected indicator.
     */
    public Indicator getSelectedIndicator() {
        return selectedIndicator;
    }

    /**
     * Attach {@code Listener}.
     *
     * @param listener Listener to attach.
     */
    public void addListener(Listener listener) {
        if (lastSearchIndicators != null) {
            listener.onSearch(lastSearchIndicators, selectedIndicator);
        }

        listeners.add(listener);
    }

    /**
     * Detach {@code Listener}.
     *
     * @param listener Listener to detach.
     */
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    /**
     * Clear the current search.
     */
    public void clearSearch() {
        lastQuery = null;
    }

    /**
     * Perform search.
     *
     * @param query Search terms.
     */
    public void search(String query) {
        if (query.equals(lastQuery)) {
            return;
        }

        if (lastSearchRequest != null) {
            lastSearchRequest.cancel();
        }

        lastQuery = query;

        // Make RPC request
        lastSearchRequest = WBIExplorationService.queryIndicatorList(
            query, searchRequestListener);
    }

    /**
     * Exhibit changes in an {@code Indicator}.
     *
     * @param indicator Indicator changed.
     */
    private void change(Indicator indicator) {
        for (Listener listener : listeners) {
            listener.onChange(indicator);
        }
    }

    /**
     * Request an {@code Indicator} to be loaded.
     *
     * @param indicator {@code Indicator} to load.
     */
    public void load(Indicator indicator) {
        indicator.setStatus(Indicator.Status.LOADING);
        change(indicator);

        // Make RPC request
        WBIManagementService.load(indicator.getId(), loadRequestListener);
    }

    /**
     * Request an {@code Indicator} to be unloaded.
     *
     * @param indicator {@code Indicator} to unload.
     */
    public void unload(Indicator indicator) {
        indicator.setStatus(Indicator.Status.LOADING);
        change(indicator);

        // Make RPC request
        WBIManagementService.unload(indicator.getId(), unloadRequestListener);
    }

    /**
     * Select an {@code Indicator}.
     *
     * @param indicator Indicator to select.
     */
    public void select(Indicator indicator) {
        if (indicator.equals(selectedIndicator)) {
            return;
        }

        this.selectedIndicator = indicator;

        if (indicator.isReady()) {
            for (Listener listener : listeners) {
                listener.onSelect(indicator);
            }
        } else {
            load(indicator);
        }
    }
}
