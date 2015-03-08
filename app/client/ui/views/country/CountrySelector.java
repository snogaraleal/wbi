package client.ui.views.country;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import models.Country;

import client.managers.models.CountryManager;
import client.ui.components.MaterialItem;
import client.ui.components.MaterialSearch;

public class CountrySelector extends Composite
    implements CountryManager.View, CountryManager.Listener {

    public static class Item extends MaterialItem {
        private CountrySelector selector;
        private Country country;

        public Item(CountrySelector selector, Country country) {
            super();

            this.selector = selector;

            setCountry(country);
            setAnimationEnabled(true);
        }

        public void setCountry(Country country) {
            this.country = country;

            setTitleText(country.getName());
            setSubtitleText(country.getRegion().getName());
        }

        @Override
        public void onClick(ClickEvent event) {
            super.onClick(event);
            CountryManager manager = selector.getCurrentManager();
            manager.toggle(country);
        }
    }

    interface CountrySelectorUiBinder
        extends UiBinder<Widget, CountrySelector> {}
    private static CountrySelectorUiBinder uiBinder =
        GWT.create(CountrySelectorUiBinder.class);

    private Map<Country, Item> map = new HashMap<Country, Item>();

    private CountryManager manager;

    private static int SEARCH_INPUT_DELAY = 300;

    private String searchInputText;
    private Timer searchInputTimer;

    @UiField
    MaterialSearch search;

    @UiField
    FlowPanel panel;

    public CountrySelector() {
        initWidget(uiBinder.createAndBindUi(this));

        searchInputTimer = new Timer() {
            @Override
            public void run() {
                if (searchInputText != null && !searchInputText.isEmpty()) {
                    manager.search(searchInputText);
                }
            }
        };

        search.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                searchInputText = search.getText().trim();
                searchInputTimer.cancel();

                if (searchInputText.isEmpty()) {
                    manager.clearSearch();

                    panel.clear();
                    map.clear();

                    List<Country> selectedCountries =
                        manager.getSelectedCountries();

                    onSearch(selectedCountries, selectedCountries);

                } else {
                    searchInputTimer.schedule(SEARCH_INPUT_DELAY);
                }
            }
        });
    }

    @Override
    public void onSearch(
            List<Country> countries,
            List<Country> selectedCountries) {

        panel.clear();
        map.clear();

        for (Country country : countries) {
            Item item = new Item(this, country);

            panel.add(item);
            map.put(country, item);

            if (selectedCountries.contains(country)) {
                item.setActive(true);
            }
        }
    }

    @Override
    public void onAdd(Country country) {
        Item item = map.get(country);
        if (item == null) {
            if (searchInputText == null || searchInputText.isEmpty()) {
                item = new Item(this, country);
                item.setActive(true);

                panel.add(item);
                map.put(country, item);
            }
        } else {
            item.setActive(true);
        }
    }

    @Override
    public void onRemove(Country country) {
        Item item = map.get(country);
        if (item != null) {
            item.setActive(false);
        }
    }

    @Override
    public void onClear(List<Country> selectedCountries) {
        search.setText("");
        searchInputText = null;
        manager.clearSearch();

        panel.clear();
        map.clear();
    }

    @Override
    public void onAttach(CountryManager manager) {
        assert this.manager == null;

        this.manager = manager;
        this.manager.addListener(this);
    }

    @Override
    public void onDetach() {
        assert this.manager != null;

        this.manager.removeListener(this);
        this.manager = null;
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    @Override
    public CountryManager getCurrentManager() {
        return manager;
    }
}
