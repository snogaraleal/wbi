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

package client.dashboard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import rpc.client.ClientRequest;

import client.history.CountryHistory;
import client.history.IndicatorHistory;
import client.history.IntervalHistory;
import client.history.SeriesPanelHistory;
import client.managers.history.HistoryManager;
import client.managers.models.CountryManager;
import client.managers.models.IndicatorManager;
import client.managers.models.IntervalManager;
import client.managers.models.SeriesManager;
import client.serializers.CSVSeriesSerializer;
import client.serializers.JSONSeriesSerializer;
import client.serializers.XMLSeriesSerializer;
import client.ui.GlobalLoadingIndicator;
import client.ui.SeriesSerializerDialogBox;
import client.ui.components.MaterialButton;
import client.ui.components.VectorMap;
import client.ui.coordinators.SimpleCoordinator;
import client.ui.coordinators.TabCoordinator;
import client.ui.views.country.CountrySelector;
import client.ui.views.indicator.IndicatorSelector;
import client.ui.views.interval.IntervalSwitch;
import client.ui.views.series.ChartSeriesView;
import client.ui.views.series.MapSeriesView;
import client.ui.views.series.SeriesView;
import client.ui.views.series.TableSeriesView;

/**
 * Main viewport widget.
 */
public class Dashboard extends Composite {
    public interface DashboardUiBinder extends UiBinder<Widget, Dashboard> {}
    private static DashboardUiBinder uiBinder =
        GWT.create(DashboardUiBinder.class);

    /**
     * Root widget containing all other widgets.
     */
    @UiField
    public FlowPanel viewport;

    /**
     * Tab panel for indicators (left panel).
     */
    @UiField
    public TabLayoutPanel indicatorPanel;

    /**
     * Tab panel for countries (right panel).
     */
    @UiField
    public TabLayoutPanel countryPanel;

    /**
     * Tab panel for series (center panel).
     */
    @UiField
    public TabLayoutPanel seriesPanel;

    /**
     * Floating panel.
     */
    @UiField
    public FlowPanel overlay;

    /**
     * Interval selector (part of {@code overlay}).
     */
    @UiField
    public IntervalSwitch seriesInterval;

    /**
     * Serializers buttons (part of {@code overlay}).
     */
    @UiField
    public FlowPanel seriesAnchors;

    // History manager
    private HistoryManager historyManager = new HistoryManager();

    // Model managers
    private IntervalManager intervalManager = new IntervalManager();
    private IndicatorManager indicatorManager = new IndicatorManager();
    private CountryManager countryManager = new CountryManager();
    private SeriesManager seriesManager = new SeriesManager();

    /**
     * Initialize viewport.
     */
    public Dashboard() {
        initWidget(uiBinder.createAndBindUi(this));

        setupLoadingIndicator();

        initIntervalComponents();
        initIndicatorComponents();
        initCountryComponents();
        initSeriesComponents();
    }

    /**
     * Add {@code GlobalLoadingIndicator} to the viewport.
     */
    private void setupLoadingIndicator() {
        viewport.add(GlobalLoadingIndicator.get());

        /*
         * Change the visibility of the loading indicator when RPC requests
         * are sent and received.
         */
        ClientRequest.addGlobalListener(new ClientRequest.GlobalListener() {
            @Override
            public void onSend(ClientRequest<?> clientRequest) {
                GlobalLoadingIndicator.start();
            }

            @Override
            public void onFinish(ClientRequest<?> clientRequest) {
                GlobalLoadingIndicator.finish();
            }
        });
    }

    /**
     * Connect views and history to the main {@code IntervalManager}.
     */
    private void initIntervalComponents() {
        SimpleCoordinator<IntervalManager> intervalCoordinator =
            new SimpleCoordinator<IntervalManager>(intervalManager);

        intervalCoordinator.setView(seriesInterval);

        IntervalHistory intervalHistory = new IntervalHistory();
        intervalHistory.connect(intervalManager);

        historyManager.addListener(intervalHistory);

        // Select the last interval option by default
        if (intervalManager.getSelectedOption() == null) {
            intervalManager.select(
                IntervalManager.OPTIONS[IntervalManager.OPTIONS.length - 1]);
        }
    }

    /**
     * Connect views and history to the main {@code IndicatorManager}.
     */
    private void initIndicatorComponents() {
        TabCoordinator<IndicatorManager> indicatorTabCoordinator =
            new TabCoordinator<IndicatorManager>(
                indicatorManager, indicatorPanel);

        indicatorTabCoordinator.addTab(
            "search", "Indicators", new IndicatorSelector());

        IndicatorHistory indicatorHistory = new IndicatorHistory();
        indicatorHistory.connect(indicatorManager);

        historyManager.addListener(indicatorHistory);
    }

    /**
     * Connect views and history to the main {@code CountryManager}.
     */
    private void initCountryComponents() {
        TabCoordinator<CountryManager> countryTabCoordinator =
            new TabCoordinator<CountryManager>(countryManager, countryPanel);

        countryTabCoordinator.addTab(
            "search", "Countries", new CountrySelector());

        CountryHistory countryHistory = new CountryHistory();
        countryHistory.connect(countryManager);

        historyManager.addListener(countryHistory);
    }

    /**
     * Connect views and history to the main {@code IntervalManager}.
     */
    private void initSeriesComponents() {
        seriesManager.connect(intervalManager);
        seriesManager.connect(indicatorManager);
        seriesManager.connect(countryManager);

        final TabCoordinator<SeriesManager> seriesTabCoordinator =
            new TabCoordinator<SeriesManager>(seriesManager, seriesPanel);

        seriesTabCoordinator.addTab(
            "table", "Table", new TableSeriesView());
        seriesTabCoordinator.addTab(
            "chart", "Chart", new ChartSeriesView());
        seriesTabCoordinator.addTab(
            "map:world", "Map: World",
            new MapSeriesView(VectorMap.Visual.WORLD));
        seriesTabCoordinator.addTab(
            "map:europe", "Map: Europe",
            new MapSeriesView(VectorMap.Visual.EUROPE));

        /*
         * Serializers
         */

        addSeriesSerializer("CSV", new CSVSeriesSerializer());
        addSeriesSerializer("XML", new XMLSeriesSerializer());
        addSeriesSerializer("JSON", new JSONSeriesSerializer());

        /*
         * Tabs
         */

        SeriesPanelHistory seriesPanelHistory = new SeriesPanelHistory();
        seriesPanelHistory.connect(seriesTabCoordinator);

        historyManager.addListener(seriesPanelHistory);

        autoEnableScroll(seriesTabCoordinator, seriesPanel.getSelectedIndex());

        seriesPanel.addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) { 
                autoEnableScroll(seriesTabCoordinator, event.getSelectedItem());
            }
        });

        SelectionEvent.fire(seriesPanel, seriesPanel.getSelectedIndex());
    }

    /**
     * Add a {@code SeriesManager.Serializer} to the serializers menu.
     *
     * @param title Display name of the serializer.
     * @param serializer Serializer instance.
     */
    private void addSeriesSerializer(
            String title, SeriesManager.Serializer serializer) {

        MaterialButton button = new MaterialButton(title);

        button.setAnimationEnabled(true);

        button.addClickHandler(
            new SeriesSerializerDialogBox.OpenClickHandler(
                seriesManager, serializer));

        seriesAnchors.add(button);
    }

    /**
     * Class name added to {@code overlay} when scrollbars are enabled in
     * the series tab panel.
     */
    private static final String CLASS_NAME_OVERLAY_SCROLL = "overlay-scroll";

    /**
     * Enable or disable scrollbars depending on the current tab.
     *
     * @param seriesTabCoordinator {@code TabCoordinator} managing series tabs.
     * @param tabIndex Index of the current tab.
     */
    private void autoEnableScroll(
            TabCoordinator<SeriesManager> seriesTabCoordinator, int tabIndex) {

        Element overlayElement = overlay.getElement();

        if (tabIndex == 0) {
            overlayElement.addClassName(CLASS_NAME_OVERLAY_SCROLL);
        } else {
            overlayElement.removeClassName(CLASS_NAME_OVERLAY_SCROLL);
        }

        SeriesView view = (SeriesView) seriesTabCoordinator.getView(0);

        if (view != null) {
            if (tabIndex == 0) {
                view.setScrollEnabled(true);
            } else {
                view.setScrollEnabled(false);
            }
        }
    }
}
