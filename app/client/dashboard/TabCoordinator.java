package client.dashboard;

import java.util.Map;
import java.util.HashMap;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import client.managers.Manager;

@SuppressWarnings({"rawtypes", "unchecked"})
public class TabCoordinator implements SelectionHandler<Integer> {
    private TabLayoutPanel panel;
    private Manager manager;

    private Map<Widget, Manager.View> viewsByWidget =
        new HashMap<Widget, Manager.View>();
    private Manager.View currentView;

    public TabCoordinator(TabLayoutPanel panel, Manager manager) {
        this.panel = panel;
        this.manager = manager;

        this.panel.addSelectionHandler(this);
    }

    public void addTab(String title, Manager.View view) {
        Widget widget = view.getWidget();
        viewsByWidget.put(widget, view);
        panel.add(widget, title);
    }

    public Manager.View getView(int index) {
        return viewsByWidget.get(panel.getWidget(index));
    }

    @Override
    public void onSelection(SelectionEvent<Integer> event) { 
        Widget widget = panel.getWidget(event.getSelectedItem());
        Manager.View view = viewsByWidget.get(widget);

        if (currentView != null) {
            currentView.onDetach(manager);
        }

        currentView = view;
        currentView.onAttach(manager);
    }
}
