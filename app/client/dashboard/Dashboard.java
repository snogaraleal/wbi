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

import client.managers.IntervalManager;
import client.managers.IndicatorManager;
import client.managers.CountryManager;
import client.managers.SeriesManager;

import client.ui.interval.IntervalSwitch;
import client.ui.indicator.IndicatorSelector;
import client.ui.country.CountrySelector;

import client.ui.series.SeriesView;
import client.ui.series.TableSeriesView;
import client.ui.series.ChartSeriesView;
import client.ui.series.MapSeriesView;

import client.ui.components.MaterialButton;

import client.ui.coordinator.SimpleCoordinator;
import client.ui.coordinator.TabCoordinator;

import client.serializers.XMLSeriesSerializer;
import client.serializers.CSVSeriesSerializer;
import client.serializers.JSONSeriesSerializer;

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

    private IntervalManager intervalManager = new IntervalManager();
    private IndicatorManager indicatorManager = new IndicatorManager();
    private CountryManager countryManager = new CountryManager();
    private SeriesManager seriesManager = new SeriesManager();

    private SimpleCoordinator intervalCoordinator;
    private TabCoordinator indicatorTabCoordinator;
    private TabCoordinator countryTabCoordinator;
    private TabCoordinator seriesTabCoordinator;

    public Dashboard() {
        initWidget(uiBinder.createAndBindUi(this));

        seriesManager.connect(intervalManager);
        seriesManager.connect(indicatorManager);
        seriesManager.connect(countryManager);

        intervalCoordinator = new SimpleCoordinator(intervalManager);
        indicatorTabCoordinator =
            new TabCoordinator(indicatorManager, indicatorPanel);
        countryTabCoordinator =
            new TabCoordinator(countryManager, countryPanel);
        seriesTabCoordinator =
            new TabCoordinator(seriesManager, seriesPanel);

        intervalCoordinator.setView(seriesInterval);

        indicatorTabCoordinator.addTab("Indicators", new IndicatorSelector());
        countryTabCoordinator.addTab("Countries", new CountrySelector());

        seriesTabCoordinator.addTab("Table", new TableSeriesView());
        seriesTabCoordinator.addTab("Chart", new ChartSeriesView());
        seriesTabCoordinator.addTab("Map", new MapSeriesView());

        addSeriesSerializer("XML", new XMLSeriesSerializer());
        addSeriesSerializer("CSV", new CSVSeriesSerializer());
        addSeriesSerializer("JSON", new JSONSeriesSerializer());

        autoEnableScroll(seriesPanel.getSelectedIndex());
        seriesPanel.addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) { 
                autoEnableScroll(event.getSelectedItem());
            }
        });
    }

    private static String CLASS_NAME_OVERLAY_SCROLL = "overlay-scroll";

    private void autoEnableScroll(int tabIndex) {
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

    private void addSeriesSerializer(
            String title, SeriesManager.Serializer serializer) {

        MaterialButton button = new MaterialButton(title);
        button.setAnimationEnabled(true);
        seriesAnchors.add(button);
    }
}
