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

public class IntervalManager implements Manager {
    public static interface View extends Manager.View<IntervalManager> {
    }

    public static class Option {
        private int startYear;
        private int endYear;

        public Option(int startYear, int endYear) {
            this.startYear = startYear;
            this.endYear = endYear;
        }

        public int getStartYear() {
            return startYear;
        }

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

    public static interface Listener {
        void onSelect(Option option);
    }

    private List<Listener> listeners = new ArrayList<Listener>();

    public static Option[] OPTIONS = new Option[] {
        new Option(1985, 1989),
        new Option(1990, 1994),
        new Option(1995, 1999),
        new Option(2000, 2004),
        new Option(2005, 2009),
        new Option(2010, 2014)
    };

    private Option selectedOption;

    public IntervalManager() {
    }

    public void addListener(Listener listener) {
        if (selectedOption != null) {
            listener.onSelect(selectedOption);
        }

        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public void select(Option option) {
        if (option.equals(selectedOption)) {
            return;
        }

        selectedOption = option;

        for (Listener listener : listeners) {
            listener.onSelect(option);
        }
    }

    public Option getSelectedOption() {
        return selectedOption;
    }
}
