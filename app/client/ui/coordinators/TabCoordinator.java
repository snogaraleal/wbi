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

package client.ui.coordinators;

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

    private Map<String, Integer> indexesByName =
        new HashMap<String, Integer>();
    private Map<Integer, String> namesByIndex =
        new HashMap<Integer, String>();

    public TabCoordinator(T manager, TabLayoutPanel panel) {
        super(manager);
        this.panel = panel;
        this.panel.addSelectionHandler(this);
    }

    public void addTab(String name, String title, Manager.View<T> view) {
        Widget widget = view.getWidget();
        viewsByWidget.put(widget, view);
        panel.add(widget, title);

        int index = panel.getWidgetIndex(widget);
        indexesByName.put(name, index);
        namesByIndex.put(index, name);
    }

    public Manager.View<T> getView(int index) {
        return viewsByWidget.get(panel.getWidget(index));
    }

    public TabLayoutPanel getPanel() {
        return panel;
    }

    public Integer getTabIndex(String name) {
        return indexesByName.get(name);
    }

    public String getTabName(int index) {
        return namesByIndex.get(index);
    }

    public void selectTab(String name) {
        Integer tabIndex = getTabIndex(name);
        if (tabIndex != null) {
            panel.selectTab(tabIndex);
        }
    }

    @Override
    public void onSelection(SelectionEvent<Integer> event) { 
        Widget widget = panel.getWidget(event.getSelectedItem());
        Manager.View<T> view = viewsByWidget.get(widget);
        setView(view);
    }
}
