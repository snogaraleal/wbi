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

package client.history;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Callback;

import models.Country;

import client.managers.history.HistoryManager;
import client.managers.history.HistoryState;
import client.managers.history.HistoryStateData;
import client.managers.models.CountryManager;

/**
 * {@link HistoryManager.Listener} in charge of updating a
 * {@link CountryManager} depending on the current {@link HistoryState}.
 */
public class CountryHistory extends HistoryManager.BaseHistory
    implements CountryManager.Listener {

    /**
     * Currently attached {@code CountryManager}.
     */
    private CountryManager manager;

    public CountryHistory() {}

    /**
     * Attach a {@link CountryManager}.
     *
     * @param manager Manager to attach.
     */
    public void connect(CountryManager manager) {
        assert this.manager == null;

        this.manager = manager;
        this.manager.addListener(this);
    }

    /**
     * Detach the currently attached {@link CountryManager}.
     */
    public void disconnect() {
        assert this.manager != null;

        this.manager.removeListener(this);
        this.manager = null;
    }

    /**
     * Handle a {@code HistoryState} change.
     */
    @Override
    public void onChange(HistoryState state) {
        List<String> countryISOList = state.getCountryISOList();

        if (countryISOList != null && !countryISOList.isEmpty()) {
            /*
             * Request a {@code HistoryStateData} when the current
             * {@code HistoryState} specifies a list of countries.
             */
            state.getData(new Callback<HistoryStateData, Void>() {
                @Override
                public void onSuccess(HistoryStateData data) {
                    /*
                     * Get the list of {@code Country} objects and update
                     * the manager accordingly.
                     */
                    List<Country> countries = data.getCountries();

                    if (countries != null) {
                        manager.clear();

                        for (Country country : countries) {
                            manager.add(country);
                        }
                    }
                }

                @Override
                public void onFailure(Void reason) {
                }
            });
        } else {
            /*
             * Clear the current country selection when the current
             * {@code HistoryState} does not specify a list of countries.
             */
            manager.clear();
        }
    }

    @Override
    public void onSearch(
            List<Country> countries,
            List<Country> selectedCountries) {
    }

    /**
     * Handle added {@code Country}.
     */
    @Override
    public void onAdd(Country country) {
        HistoryState state = historyManager.getCurrentState();
        List<String> countryISOList =
            new ArrayList<String>(state.getCountryISOList());

        String iso = country.getISO();

        /*
         * Update the current {@code HistoryState} with the ISO code of the
         * added {@code Country}.
         */
        if (!countryISOList.contains(iso)) {
            countryISOList.add(iso);

            state.setCountryISOList(countryISOList);
            historyManager.setState(state);
        }
    }

    /**
     * Handle removed {@code Country}.
     */
    @Override
    public void onRemove(Country country) {
        HistoryState state = historyManager.getCurrentState();
        List<String> countryISOList =
            new ArrayList<String>(state.getCountryISOList());

        String iso = country.getISO();

        /*
         * Update the current {@code HistoryState} with the ISO code of the
         * removed {@code Country}.
         */
        if (countryISOList.contains(iso)) {
            countryISOList.remove(iso);

            state.setCountryISOList(countryISOList);
            historyManager.setState(state);
        }
    }

    @Override
    public void onClear(List<Country> selectedCountries) {
    }
}
