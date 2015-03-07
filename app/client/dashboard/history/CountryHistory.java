package client.dashboard.history;

import java.util.List;

import models.Country;

import client.managers.CountryManager;
import client.managers.HistoryManager;

public class CountryHistory extends HistoryManager.BaseHistory
    implements CountryManager.Listener {

    private CountryManager manager;

    public CountryHistory() {
    }

    public void connect(CountryManager manager) {
        assert this.manager == null;

        this.manager = manager;
        this.manager.addListener(this);
    }

    public void disconnect() {
        assert this.manager != null;

        this.manager.removeListener(this);
        this.manager = null;
    }

    @Override
    public void onChange(HistoryManager.State state) {
    }

    @Override
    public void onSearch(
            List<Country> countries,
            List<Country> selectedCountries) {
    }

    @Override
    public void onAdd(Country country) {
    }

    @Override
    public void onRemove(Country country) {
    }

    @Override
    public void onClear(List<Country> selectedCountries) {
    }
}
