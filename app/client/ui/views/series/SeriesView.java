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

package client.ui.views.series;

import java.util.List;
import java.util.SortedSet;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import client.managers.models.SeriesManager;

/**
 * Base view implementing {@link SeriesManager.View}.
 */
public abstract class SeriesView extends Composite
    implements SeriesManager.View, SeriesManager.Listener {

    /**
     * {@code SeriesManager} this view is currently attached to.
     */
    protected SeriesManager manager;

    /**
     * Initialize {@code SeriesView}.
     */
    public SeriesView() {}

    @Override
    public void onUpdate(
            List<SeriesManager.Row> rows,
            SortedSet<Integer> years,
            SeriesManager.Ordering ordering) {
    }

    @Override
    public void onChange(SeriesManager.Row row) {
    }

    @Override
    public void onAttach(SeriesManager manager) {
        assert this.manager == null;

        this.manager = manager;
        this.manager.addListener(this);
    }

    @Override
    public void onDetach() {
        assert this.manager != null;

        this.manager.removeListener(this);
        this.manager = null;
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    @Override
    public SeriesManager getCurrentManager() {
        return manager;
    }

    /**
     * Class name added to this widget when scrollbars are enabled.
     */
    private static final String CLASS_NAME_SCROLL = "scroll";

    /**
     * Delay before enabling scrollbars.
     */
    private static final int SCROLL_DELAY = 300;

    /**
     * Set whether scrollbars are enabled.
     *
     * @param enabled Whether scrollbars are enabled.
     */
    public void setScrollEnabled(final boolean enabled) {
        final Element element = getElement();
        boolean scrollEnabled = element.hasClassName(CLASS_NAME_SCROLL);

        if ((scrollEnabled && !enabled) || (!scrollEnabled && enabled)) {
            if (enabled) {
                (new Timer() {
                    @Override
                    public void run() {
                        element.addClassName(CLASS_NAME_SCROLL);
                    }
                }).schedule(SCROLL_DELAY);
            } else {
                element.removeClassName(CLASS_NAME_SCROLL);
            }
        }
    }
}
