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

package client.managers;

import com.google.gwt.user.client.ui.Widget;

/**
 * Interface for a class to which a {@link View} can be attached.
 */
public interface Manager {
    /**
     * View displaying information provided by a {@link Manager} (use a
     * class from {@link client.ui.coordinators} to connect managers and
     * views).
     *
     * @param <T> {@code Manager} class.
     */
    public static interface View<T extends Manager> {
        /**
         * Handle attach.
         *
         * @param manager Attached {@code Manager}.
         */
        void onAttach(T manager);

        /**
         * Handle detach.
         */
        void onDetach();

        /**
         * Get the {@code Widget} underlying this view.
         *
         * @return View widget.
         */
        Widget getWidget();

        /**
         * Get the {@link Manager} this {@link View} is currently attached to.
         *
         * @return Current manager.
         */
        T getCurrentManager();
    }
}
