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

public class IndicatorManager implements Manager {
    public static interface View extends Manager.View<IndicatorManager> {
    }

    public static interface Listener {
        void onSearch(
            List<Indicator> indicators,
            Indicator selectedIndicator);
        void onChange(Indicator indicator);
        void onSelect(Indicator indicator);
    }

    private List<Listener> listeners = new ArrayList<Listener>();

    private ClientRequest.Listener<List<Indicator>> searchRequestListener;
    private ClientRequest.Listener<Indicator> loadRequestListener;
    private ClientRequest.Listener<Indicator> unloadRequestListener;

    private String lastQuery;
    private ClientRequest<List<Indicator>> lastSearchRequest;
    private List<Indicator> lastSearchIndicators;

    private Indicator selectedIndicator;

    private IndicatorWatcher watcher;

    public IndicatorManager() {
        selectedIndicator = null;

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

    public Indicator getSelectedIndicator() {
        return selectedIndicator;
    }

    public void addListener(Listener listener) {
        if (lastSearchIndicators != null) {
            listener.onSearch(lastSearchIndicators, selectedIndicator);
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
        lastSearchRequest = WBIExplorationService.queryIndicatorList(
            query, searchRequestListener);
    }

    private void change(Indicator indicator) {
        for (Listener listener : listeners) {
            listener.onChange(indicator);
        }
    }

    public void load(Indicator indicator) {
        indicator.setStatus(Indicator.Status.LOADING);
        change(indicator);
        WBIManagementService.load(indicator.getId(), loadRequestListener);
    }

    public void unload(Indicator indicator) {
        indicator.setStatus(Indicator.Status.LOADING);
        change(indicator);
        WBIManagementService.unload(indicator.getId(), unloadRequestListener);
    }

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
