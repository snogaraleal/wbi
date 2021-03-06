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

package client.ui;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Loading indicator that is always visible.
 */
public class GlobalLoadingIndicator extends FlowPanel {
    /**
     * Class name with loading style.
     */
    public static final String CLASS_NAME_SPINNER = "spinner";

    /**
     * Initialize {@code GlobalLoadingIndicator}.
     */
    private GlobalLoadingIndicator() {
        super();
        getElement().addClassName(CLASS_NAME_SPINNER);
        setVisible(false);
    }

    /**
     * Number of loading items.
     */
    private static int count = 0;

    /**
     * Update indicator visibility according to the current number of
     * loading items.
     */
    private static void updateVisibility() {
        GlobalLoadingIndicator indicator = get();
        indicator.setVisible(count > 0);
    }

    /**
     * Start loading an item.
     */
    public static void start() {
        count++;
        updateVisibility();
    }

    /**
     * Finish loading an item.
     */
    public static void finish() {
        count--;
        updateVisibility();
    }

    /*
     * Singleton
     */

    private static GlobalLoadingIndicator indicator;

    public static GlobalLoadingIndicator get() {
        if (indicator == null) {
            indicator = new GlobalLoadingIndicator();
        }

        return indicator;
    }
}
