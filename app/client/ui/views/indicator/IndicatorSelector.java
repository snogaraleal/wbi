package client.ui.views.indicator;

import java.util.Arrays;
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

import models.Indicator;

import client.managers.models.IndicatorManager;
import client.ui.components.MaterialItem;
import client.ui.components.MaterialSearch;

public class IndicatorSelector extends Composite
    implements IndicatorManager.View, IndicatorManager.Listener {

    public static class Item extends MaterialItem {
        private IndicatorSelector selector;
        private Indicator indicator;

        public Item(IndicatorSelector selector, Indicator indicator) {
            super();

            this.selector = selector;

            setIndicator(indicator);
            setAnimationEnabled(true);
        }

        public void setIndicator(Indicator indicator) {
            this.indicator = indicator;

            setTitleText(indicator.getName());
            setSubtitleText(indicator.getSource().getName());
            setLoading(indicator.isLoading());

            if (indicator.isReady()) {
                setIconVisible(false);
            } else {
                setIconVisible(true);
                setIcon(Icon.DOWNLOAD);
            }
        }

        @Override
        public void onClick(ClickEvent event) {
            super.onClick(event);
            IndicatorManager manager = selector.getCurrentManager();
            manager.select(indicator);
        }
    }

    interface IndicatorSelectorUiBinder
        extends UiBinder<Widget, IndicatorSelector> {}
    private static IndicatorSelectorUiBinder uiBinder =
        GWT.create(IndicatorSelectorUiBinder.class);

    private Map<Indicator, Item> map = new HashMap<Indicator, Item>();

    private IndicatorManager manager;

    private static final int SEARCH_INPUT_DELAY = 300;

    private String searchInputText;
    private Timer searchInputTimer;

    @UiField
    MaterialSearch search;

    @UiField
    FlowPanel panel;

    public IndicatorSelector() {
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

                    Indicator selectedIndicator =
                        manager.getSelectedIndicator();

                    if (selectedIndicator != null) {
                        onSearch(
                            Arrays.asList(selectedIndicator),
                            selectedIndicator);
                    }
                } else {
                    searchInputTimer.schedule(SEARCH_INPUT_DELAY);
                }
            }
        });
    }

    @Override
    public void onSearch(
            List<Indicator> indicators,
            Indicator selectedIndicator) {

        panel.clear();
        map.clear();

        for (Indicator indicator : indicators) {
            Item item = new Item(this, indicator);

            panel.add(item);
            map.put(indicator, item);

            if (indicator.equals(selectedIndicator) && indicator.isReady()) {
                item.setActive(true);
            }
        }
    }

    @Override
    public void onChange(Indicator indicator) {
        Item item = map.get(indicator);
        if (item != null) {
            item.setIndicator(indicator);
        }
    }

    @Override
    public void onSelect(Indicator indicator) {
        if (searchInputText == null || searchInputText.isEmpty()) {
            onSearch(Arrays.asList(indicator), indicator);
        } else {
            for (Item item : map.values()) {
                item.setActive(false);
            }

            Item item = map.get(indicator);
            if (item != null) {
                item.setActive(true);
            }
        }
    }

    @Override
    public void onAttach(IndicatorManager manager) {
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
    public IndicatorManager getCurrentManager() {
        return manager;
    }
}
