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

package client.managers.models;

import java.util.ArrayList;
import java.util.List;

import client.managers.Manager;

/**
 * {@link Manager} in charge of the {@link IntervalManager.Option} selection.
 */
public class IntervalManager implements Manager {
    /**
     * Interface for views that can be attached to an {@link IntervalManager}
     * in order to change the selected {@link IntervalManager.Option}.
     */
    public static interface View extends Manager.View<IntervalManager> {}

    /**
     * Interval of time between two years.
     */
    public static class Option {
        /**
         * Interval start year.
         */
        private int startYear;

        /**
         * Interval end year.
         */
        private int endYear;

        /**
         * Initialize {@code Option}.
         *
         * @param startYear Interval start year.
         * @param endYear Interval end year.
         */
        public Option(int startYear, int endYear) {
            this.startYear = startYear;
            this.endYear = endYear;
        }

        /**
         * Get interval start year.
         *
         * @return Start year.
         */
        public int getStartYear() {
            return startYear;
        }

        /**
         * Get interval end year.
         *
         * @return End year.
         */
        public int getEndYear() {
            return endYear;
        }

        @Override
        public String toString() {
            return startYear + " " + endYear;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null) {
                return false;
            }

            if (object == this) {
                return true;
            }

            if (!(object instanceof Option)) {
                return false;
            }

            if (this.getClass() != object.getClass()) {
                return false;
            }

            Option option = (Option) object;

            return this.getStartYear() == option.getStartYear() &&
                this.getEndYear() == option.getEndYear();
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 37 * hash + (startYear ^ (startYear >>> 32));
            hash = 37 * hash + (endYear ^ (endYear >>> 32));
            return hash;
        }
    }

    /**
     * Interface for listeners that can be attached to an
     * {@link IntervalManager} in order to listen to  changes in the current
     * {@link IntervalManager.Option} selection.
     */
    public static interface Listener {
        /**
         * Handle change of {@link Option}.
         *
         * @param option {@code Option} that was just selected.
         */
        void onSelect(Option option);
    }

    /**
     * {@code Listener} objects listening to changes in this manager.
     */
    private List<Listener> listeners = new ArrayList<Listener>();

    /**
     * Available list of {@code Option} objects.
     */
    public static final Option[] OPTIONS = new Option[] {
        new Option(1985, 1989),
        new Option(1990, 1994),
        new Option(1995, 1999),
        new Option(2000, 2004),
        new Option(2005, 2009),
        new Option(2010, 2014)
    };

    /**
     * Currently selected {@code Option}.
     */
    private Option selectedOption;

    public IntervalManager() {}

    /**
     * Get the currently selected {@code Option}.
     *
     * @return Selected option.
     */
    public Option getSelectedOption() {
        return selectedOption;
    }

    /**
     * Attach {@code Listener}.
     *
     * @param listener Listener to attach.
     */
    public void addListener(Listener listener) {
        if (selectedOption != null) {
            listener.onSelect(selectedOption);
        }

        listeners.add(listener);
    }

    /**
     * Detach {@code Listener}.
     *
     * @param listener Listener to detach.
     */
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    /**
     * Select an {@code Option}.
     *
     * @param option Option to select.
     */
    public void select(Option option) {
        if (option.equals(selectedOption)) {
            return;
        }

        selectedOption = option;

        for (Listener listener : listeners) {
            listener.onSelect(option);
        }
    }
}
