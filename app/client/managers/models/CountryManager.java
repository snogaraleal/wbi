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

public class CountryManager implements Manager {
    public static interface View extends Manager.View<CountryManager> {
    }

    public static interface Listener {
        void onSearch(
            List<Country> countries,
            List<Country> selectedCountries);
        void onAdd(Country country);
        void onRemove(Country country);
        void onClear(List<Country> selectedCountries);
    }

    private List<Listener> listeners = new ArrayList<Listener>();

    private ClientRequest.Listener<List<Country>> searchRequestListener;

    private String lastQuery;
    private ClientRequest<List<Country>> lastSearchRequest;
    private List<Country> lastSearchCountries;

    private List<Country> selectedCountries = new ArrayList<Country>();

    public CountryManager() {
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

    public List<Country> getSelectedCountries() {
        return selectedCountries;
    }

    public void addListener(Listener listener) {
        if (lastSearchCountries != null) {
            listener.onSearch(lastSearchCountries, selectedCountries);
        }

        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public void clearSearch() {
        lastQuery = null;
    }

    public void search(String query) {
        if (query.equals(lastQuery)) {
            return;
        }

        if (lastSearchRequest != null) {
            lastSearchRequest.cancel();
        }

        lastQuery = query;
        lastSearchRequest = WBIExplorationService.queryCountryList(
            query, searchRequestListener);
    }

    public void add(Country country) {
        if (!selectedCountries.contains(country)) {
            selectedCountries.add(country);

            for (Listener listener : listeners) {
                listener.onAdd(country);
            }
        }
    }

    public void remove(Country country) {
        if (selectedCountries.contains(country)) {
            selectedCountries.remove(country);

            for (Listener listener : listeners) {
                listener.onRemove(country);
            }
        }
    } 

    public void toggle(Country country) {
        if (selectedCountries.contains(country)) {
            remove(country);
        } else {
            add(country);
        }
    }

    public void clear() {
        for (Listener listener : listeners) {
            listener.onClear(selectedCountries);
        }

        selectedCountries.clear();
    }
}
