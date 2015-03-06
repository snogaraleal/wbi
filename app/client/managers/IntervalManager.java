package client.managers;

import java.util.ArrayList;
import java.util.List;

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
        select(OPTIONS[OPTIONS.length - 1]);
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
        selectedOption = option;

        for (Listener listener : listeners) {
            listener.onSelect(option);
        }
    }

    public Option getSelectedOption() {
        return selectedOption;
    }
}
