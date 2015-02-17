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

import client.managers.IndicatorManager;
import client.managers.CountryManager;
import client.managers.SeriesManager;

import client.ui.indicator.IndicatorSelector;
import client.ui.country.CountrySelector;

import client.ui.series.TableSeriesView;
import client.ui.series.ChartSeriesView;
import client.ui.series.MapSeriesView;

import client.ui.components.MaterialButton;

import client.serializers.XMLSeriesSerializer;
import client.serializers.CSVSeriesSerializer;
import client.serializers.JSONSeriesSerializer;

public class Dashboard extends Composite {
    interface DashboardUiBinder extends UiBinder<Widget, Dashboard> {}
    private static DashboardUiBinder uiBinder =
        GWT.create(DashboardUiBinder.class);

    @UiField
    TabLayoutPanel indicatorPanel;

    @UiField
    TabLayoutPanel countryPanel;

    @UiField
    TabLayoutPanel seriesPanel;

    @UiField
    FlowPanel seriesAnchors;

    private IndicatorManager indicatorManager = new IndicatorManager();
    private CountryManager countryManager = new CountryManager();
    private SeriesManager seriesManager = new SeriesManager();

    public Dashboard() {
        initWidget(uiBinder.createAndBindUi(this));

        seriesManager.connect(indicatorManager);
        seriesManager.connect(countryManager);

        TabCoordinator indicatorTabCoordinator =
            new TabCoordinator(indicatorPanel, indicatorManager);
        TabCoordinator countryTabCoordinator =
            new TabCoordinator(countryPanel, countryManager);
        TabCoordinator seriesTabCoordinator =
            new TabCoordinator(seriesPanel, seriesManager);

        indicatorTabCoordinator.addTab("Indicators", new IndicatorSelector());
        countryTabCoordinator.addTab("Countries", new CountrySelector());

        seriesTabCoordinator.addTab("Table", new TableSeriesView());
        seriesTabCoordinator.addTab("Chart", new ChartSeriesView());
        seriesTabCoordinator.addTab("Map", new MapSeriesView());

        addSeriesSerializer("XML", new XMLSeriesSerializer());
        addSeriesSerializer("CSV", new CSVSeriesSerializer());
        addSeriesSerializer("JSON", new JSONSeriesSerializer());

        updateAnchorsScroll(seriesPanel.getSelectedIndex());
        seriesPanel.addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) { 
                updateAnchorsScroll(event.getSelectedItem());
            }
        });
    }

    private static String CLASS_NAME_ENABLE_SCROLL = "scroll";
    private static String CLASS_NAME_ANCHORS_SCROLL = "scroll";

    private void updateAnchorsScroll(int tabIndex) {
        Widget tab = seriesPanel.getWidget(tabIndex);
        Element seriesAnchorsElement = seriesAnchors.getElement();

        if (tab.getElement().hasClassName(CLASS_NAME_ENABLE_SCROLL)) {
            seriesAnchorsElement.addClassName(CLASS_NAME_ANCHORS_SCROLL);
        } else {
            seriesAnchorsElement.removeClassName(CLASS_NAME_ANCHORS_SCROLL);
        }
    }

    private void addSeriesSerializer(
            String title, SeriesManager.Serializer serializer) {
        seriesAnchors.add(new MaterialButton(title));
    }
}
