package client.history;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Callback;

import models.Country;

import client.managers.history.HistoryManager;
import client.managers.history.HistoryState;
import client.managers.history.HistoryStateData;
import client.managers.models.CountryManager;

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
    public void onChange(HistoryState state) {
        List<String> countryISOList = state.getCountryISOList();

        if (countryISOList != null && !countryISOList.isEmpty()) {
            state.getData(new Callback<HistoryStateData, Void>() {
                @Override
                public void onSuccess(HistoryStateData data) {
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
            manager.clear();
        }
    }

    @Override
    public void onSearch(
            List<Country> countries,
            List<Country> selectedCountries) {
    }

    @Override
    public void onAdd(Country country) {
        HistoryState state = historyManager.getCurrentState();
        List<String> countryISOList = state.getCountryISOList();

        if (countryISOList == null) {
            countryISOList = new ArrayList<String>();
        } else {
            countryISOList = new ArrayList<String>(countryISOList);
        }

        String iso = country.getISO();

        if (!countryISOList.contains(iso)) {
            countryISOList.add(iso);

            state.setCountryISOList(countryISOList);
            historyManager.setState(state);
        }
    }

    @Override
    public void onRemove(Country country) {
        HistoryState state = historyManager.getCurrentState();
        List<String> countryISOList = state.getCountryISOList();

        if (countryISOList == null) {
            countryISOList = new ArrayList<String>();
        } else {
            countryISOList = new ArrayList<String>(countryISOList);
        }

        String iso = country.getISO();

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
