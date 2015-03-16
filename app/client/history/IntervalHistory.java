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

package client.history;

import client.managers.history.HistoryManager;
import client.managers.history.HistoryState;
import client.managers.models.IntervalManager;

/**
 * {@link HistoryManager.Listener} in charge of updating an
 * {@link IntervalManager} depending on the current {@link HistoryState}.
 */
public class IntervalHistory extends HistoryManager.BaseHistory
    implements IntervalManager.Listener {

    /**
     * Currently attached {@code IntervalManager}.
     */
    private IntervalManager manager;

    public IntervalHistory() {}

    /**
     * Attach an {@link IntervalManager}.
     *
     * @param manager Manager to attach.
     */
    public void connect(IntervalManager manager) {
        assert this.manager == null;

        this.manager = manager;
        this.manager.addListener(this);

        HistoryManager.get().addListener(this);
    }

    /**
     * Detach the currently attached {@link IntervalManager}.
     */
    public void disconnect() {
        assert this.manager != null;

        this.manager.removeListener(this);
        this.manager = null;

        HistoryManager.get().removeListener(this);
    }

    /**
     * Handle a {@code HistoryState} change.
     */
    @Override
    public void onChange(HistoryState state) {
        Integer startYear = state.getIntervalStartYear();
        Integer endYear = state.getIntervalEndYear();

        /*
         * Update the current {@code IntervalManager.Option} when the current
         * {@code HistoryState} specifies an interval.
         */
        if (startYear != null && endYear != null) {
            manager.select(new IntervalManager.Option(startYear, endYear));
        }
    }

    /**
     * Handle selected {@code IntervalManager.Option}.
     */
    @Override
    public void onSelect(IntervalManager.Option option) {
        HistoryState state = historyManager.getCurrentState();

        /*
         * Update the current {@code HistoryState} with the interval of the
         * selected {@code IntervalManager.Option}.
         */
        state.setInterval(option.getStartYear(), option.getEndYear());
        historyManager.setState(state);
    }
}
