package client.managers;

import java.util.List;
import java.util.ArrayList;

import rpc.client.ClientRequest;

import models.Indicator;

import client.services.WBIExplorationService;
import client.services.WBIManagementService;
import client.managers.watcher.IndicatorWatcher;

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
    private ClientRequest lastSearchRequest;

    private Indicator selectedIndicator;

    private IndicatorWatcher watcher;

    public IndicatorManager() {
        selectedIndicator = null;

        watcher = IndicatorWatcher.create();
        watcher.addListener(new IndicatorWatcher.Listener() {
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
            public void onSuccess(
                    ClientRequest request,
                    List<Indicator> list) {

                for (Listener listener : listeners) {
                    listener.onSearch(list, selectedIndicator);
                }
            }

            public void onFailure(
                    ClientRequest request,
                    ClientRequest.Error error) {
            }
        };

        loadRequestListener = new ClientRequest.Listener<Indicator>() {
            public void onSuccess(
                    ClientRequest request,
                    Indicator indicator) {

                change(indicator);

                watcher.watchUntilStatusEquals(
                    indicator, Indicator.Status.READY);
            }

            public void onFailure(
                    ClientRequest request,
                    ClientRequest.Error error) {
            }
        };

        unloadRequestListener = new ClientRequest.Listener<Indicator>() {
            public void onSuccess(
                    ClientRequest request,
                    Indicator indicator) {

                change(indicator);

                watcher.watchUntilStatusEquals(
                    indicator, Indicator.Status.AVAILABLE);
            }

            public void onFailure(
                    ClientRequest request,
                    ClientRequest.Error error) {
            }
        };
    }

    public Indicator getSelectedIndicator() {
        return selectedIndicator;
    }

    public void addListener(Listener listener) {
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
        if (indicator != this.selectedIndicator) {
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
}
