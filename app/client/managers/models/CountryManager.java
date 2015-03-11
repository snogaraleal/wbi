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

import models.Country;

import client.managers.Manager;
import client.services.WBIExplorationService;

/**
 * {@link Manager} in charge of the current {@link Country} selection.
 *
 * @see Manager
 */
public class CountryManager implements Manager {
    /**
     * Interface for views that interact with a {@link CountryManager}.
     */
    public static interface View extends Manager.View<CountryManager> {}

    /**
     * Interface for {@link CountryManager} listeners.
     */
    public static interface Listener {
        /**
         * Handle search results.
         *
         * @param countries Search results.
         * @param selectedCountries Currently selected countries.
         */
        void onSearch(
            List<Country> countries,
            List<Country> selectedCountries);

        /**
         * Handle added {@link Country}.
         *
         * @param country Country that was just added.
         */
        void onAdd(Country country);

        /**
         * Handle removed {@link Country}.
         *
         * @param country Country that was just removed.
         */
        void onRemove(Country country);

        /**
         * Clear the current selection.
         *
         * @param selectedCountries Selection before clearing.
         */
        void onClear(List<Country> selectedCountries);
    }

    /**
     * {@code Listener} objects listening to changes in this manager.
     */
    private List<Listener> listeners = new ArrayList<Listener>();

    /**
     * {@code ClientRequest.Listener} for search requests.
     */
    private ClientRequest.Listener<List<Country>> searchRequestListener;

    /**
     * Last search query.
     */
    private String lastQuery;

    /**
     * Last search {@code ClientRequest}.
     */
    private ClientRequest<List<Country>> lastSearchRequest;

    /**
     * Last search results.
     */
    private List<Country> lastSearchCountries;

    /**
     * List of selected {@code Country} objects.
     */
    private List<Country> selectedCountries = new ArrayList<Country>();

    /**
     * Initialize {@code CountryManager}.
     */
    public CountryManager() {
        /*
         * Initialize {@code ClientRequest.Listener} for search.
         */
        searchRequestListener = new ClientRequest.Listener<List<Country>>() {
            @Override
            public void onSuccess(List<Country> list) {
                lastSearchCountries = list;

                for (Listener listener : listeners) {
                    listener.onSearch(list, selectedCountries);
                }
            }

            @Override
            public void onFailure(ClientRequest.Error error) {
            }
        };
    }

    /**
     * Get the list of selected {@link Country} objects.
     *
     * @return Selected countries.
     */
    public List<Country> getSelectedCountries() {
        return selectedCountries;
    }

    /**
     * Attach {@code Listener}.
     *
     * @param listener Listener to attach.
     */
    public void addListener(Listener listener) {
        if (lastSearchCountries != null) {
            listener.onSearch(lastSearchCountries, selectedCountries);
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
        lastSearchRequest = WBIExplorationService.queryCountryList(
            query, searchRequestListener);
    }

    /**
     * Add country to selection.
     *
     * @param country {@link Country} to add.
     */
    public void add(Country country) {
        if (!selectedCountries.contains(country)) {
            selectedCountries.add(country);

            for (Listener listener : listeners) {
                listener.onAdd(country);
            }
        }
    }

    /**
     * Remove country from selection.
     *
     * @param country {@link Country} to remove.
     */
    public void remove(Country country) {
        if (selectedCountries.contains(country)) {
            selectedCountries.remove(country);

            for (Listener listener : listeners) {
                listener.onRemove(country);
            }
        }
    } 

    /**
     * Toggle country selection.
     *
     * @param country {@link Country} to add or remove.
     */
    public void toggle(Country country) {
        if (selectedCountries.contains(country)) {
            remove(country);
        } else {
            add(country);
        }
    }

    /**
     * Clear the current selection.
     */
    public void clear() {
        for (Listener listener : listeners) {
            listener.onClear(selectedCountries);
        }

        selectedCountries.clear();
    }
}
