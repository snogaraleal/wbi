package client.ui.coordinator;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import client.managers.Manager;

public class TabCoordinator<T extends Manager> extends SimpleCoordinator<T>
    implements SelectionHandler<Integer> {

    private TabLayoutPanel panel;

    private Map<Widget, Manager.View<T>> viewsByWidget =
        new HashMap<Widget, Manager.View<T>>();

    public TabCoordinator(T manager, TabLayoutPanel panel) {
        super(manager);
        this.panel = panel;
        this.panel.addSelectionHandler(this);
    }

    public void addTab(String title, Manager.View<T> view) {
        Widget widget = view.getWidget();
        viewsByWidget.put(widget, view);
        panel.add(widget, title);
    }

    public Manager.View<T> getView(int index) {
        return viewsByWidget.get(panel.getWidget(index));
    }

    @Override
    public void onSelection(SelectionEvent<Integer> event) { 
        Widget widget = panel.getWidget(event.getSelectedItem());
        Manager.View<T> view = viewsByWidget.get(widget);
        setView(view);
    }
}
