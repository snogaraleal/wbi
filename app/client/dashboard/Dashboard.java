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

import client.dashboard.history.CountryHistory;
import client.dashboard.history.IndicatorHistory;
import client.dashboard.history.IntervalHistory;
import client.dashboard.history.SeriesPanelHistory;
import client.managers.CountryManager;
import client.managers.HistoryManager;
import client.managers.IndicatorManager;
import client.managers.IntervalManager;
import client.managers.SeriesManager;
import client.serializers.CSVSeriesSerializer;
import client.serializers.JSONSeriesSerializer;
import client.serializers.XMLSeriesSerializer;
import client.ui.components.MaterialButton;
import client.ui.components.VectorMap;
import client.ui.coordinator.SimpleCoordinator;
import client.ui.coordinator.TabCoordinator;
import client.ui.country.CountrySelector;
import client.ui.indicator.IndicatorSelector;
import client.ui.interval.IntervalSwitch;
import client.ui.series.ChartSeriesView;
import client.ui.series.MapSeriesView;
import client.ui.series.SeriesView;
import client.ui.series.TableSeriesView;

public class Dashboard extends Composite {
    interface DashboardUiBinder extends UiBinder<Widget, Dashboard> {}
    private static DashboardUiBinder uiBinder =
        GWT.create(DashboardUiBinder.class);

    @UiField
    FlowPanel overlay;

    @UiField
    TabLayoutPanel indicatorPanel;

    @UiField
    TabLayoutPanel countryPanel;

    @UiField
    TabLayoutPanel seriesPanel;

    @UiField
    IntervalSwitch seriesInterval;

    @UiField
    FlowPanel seriesAnchors;

    private HistoryManager historyManager = new HistoryManager();

    private IntervalManager intervalManager = new IntervalManager();
    private IndicatorManager indicatorManager = new IndicatorManager();
    private CountryManager countryManager = new CountryManager();
    private SeriesManager seriesManager = new SeriesManager();

    public Dashboard() {
        initWidget(uiBinder.createAndBindUi(this));

        initIntervalComponents();
        initIndicatorComponents();
        initCountryComponents();
        initSeriesComponents();
    }

    private void initIntervalComponents() {
        SimpleCoordinator<IntervalManager> intervalCoordinator =
            new SimpleCoordinator<IntervalManager>(intervalManager);

        intervalCoordinator.setView(seriesInterval);

        IntervalHistory intervalHistory = new IntervalHistory();
        intervalHistory.connect(intervalManager);

        historyManager.addListener(intervalHistory);
    }

    private void initIndicatorComponents() {
        TabCoordinator<IndicatorManager> indicatorTabCoordinator =
            new TabCoordinator<IndicatorManager>(
                indicatorManager, indicatorPanel);

        indicatorTabCoordinator.addTab("Indicators", new IndicatorSelector());

        IndicatorHistory indicatorHistory = new IndicatorHistory();
        indicatorHistory.connect(indicatorManager);

        historyManager.addListener(indicatorHistory);
    }

    private void initCountryComponents() {
        TabCoordinator<CountryManager> countryTabCoordinator =
            new TabCoordinator<CountryManager>(countryManager, countryPanel);

        countryTabCoordinator.addTab("Countries", new CountrySelector());

        CountryHistory countryHistory = new CountryHistory();
        countryHistory.connect(countryManager);

        historyManager.addListener(countryHistory);
    }

    private void initSeriesComponents() {
        seriesManager.connect(intervalManager);
        seriesManager.connect(indicatorManager);
        seriesManager.connect(countryManager);

        final TabCoordinator<SeriesManager> seriesTabCoordinator =
            new TabCoordinator<SeriesManager>(seriesManager, seriesPanel);

        seriesTabCoordinator.addTab(
            "Table", new TableSeriesView());
        seriesTabCoordinator.addTab(
            "Chart", new ChartSeriesView());
        seriesTabCoordinator.addTab(
            "Map: World", new MapSeriesView(VectorMap.Visual.WORLD));
        seriesTabCoordinator.addTab(
            "Map: Europe", new MapSeriesView(VectorMap.Visual.EUROPE));

        // Serializers

        addSeriesSerializer("XML", new XMLSeriesSerializer());
        addSeriesSerializer("CSV", new CSVSeriesSerializer());
        addSeriesSerializer("JSON", new JSONSeriesSerializer());

        /*
         * Tabs
         */

        SeriesPanelHistory seriesPanelHistory = new SeriesPanelHistory();
        seriesPanelHistory.connect(seriesPanel);

        historyManager.addListener(seriesPanelHistory);

        autoEnableScroll(seriesTabCoordinator, seriesPanel.getSelectedIndex());

        seriesPanel.addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) { 
                autoEnableScroll(seriesTabCoordinator, event.getSelectedItem());
            }
        });
    }

    private void addSeriesSerializer(
            String title, SeriesManager.Serializer serializer) {

        MaterialButton button = new MaterialButton(title);
        button.setAnimationEnabled(true);
        seriesAnchors.add(button);
    }

    private static final String CLASS_NAME_OVERLAY_SCROLL = "overlay-scroll";

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
