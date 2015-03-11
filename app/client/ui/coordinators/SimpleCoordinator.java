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

import client.managers.Manager;

/**
 * Coordinator for attaching a single {@link Manager.View} to a
 * {@link Manager} detaching any currently attached view.
 *
 * @param <T> Manager class.
 */
public class SimpleCoordinator<T extends Manager> {
    /**
     * {@code Manager} to attach views to.
     */
    private T manager;

    /**
     * Currently attached {@code Manager.View}.
     */
    private Manager.View<T> currentView;

    /**
     * Initialize {@code SimpleCoordinator}.
     *
     * @param manager {@code Manager} to attach views to.
     */
    public SimpleCoordinator(T manager) {
        this.manager = manager;
    }

    /**
     * Get {@code Manager} views are attached to.
     *
     * @return {@code Manager}.
     */
    public T getManager() {
        return manager;
    }

    /**
     * Set the currently attached {@code Manager.View}.
     *
     * @param view View to attach.
     */
    public void setView(Manager.View<T> view) {
        if (currentView != null) {
            currentView.onDetach();
        }

        currentView = view;

        view.onAttach(manager);
    }

    /**
     * Get currently attached {@code Manager.View}.
     *
     * @return Currently attached view.
     */
    public Manager.View<T> getCurrentView() {
        return currentView;
    }
}
