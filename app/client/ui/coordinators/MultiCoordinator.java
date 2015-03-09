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

import java.util.HashSet;
import java.util.Set;

import client.managers.Manager;

public class MultiCoordinator<T extends Manager> {
    private T manager;
    private Set<Manager.View<T>> views = new HashSet<Manager.View<T>>();

    public MultiCoordinator(T manager) {
        this.manager = manager;
    }

    public T getManager() {
        return manager;
    }

    public void addView(Manager.View<T> view) {
        if (!views.contains(view)) {
            views.add(view);
            view.onAttach(manager);
        }
    }

    public void removeView(Manager.View<T> view) {
        if (views.contains(view)) {
            views.remove(view);
            view.onDetach();
        }
    }
}
