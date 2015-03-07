package client.managers;

import java.util.ArrayList;
import java.util.List;

public class HistoryManager implements Manager {
    public static class Fragment {
    }

    public static interface Listener {
        void onAdd(HistoryManager historyManager);
        void onRemove(HistoryManager historyManager);
        void onChange(Fragment fragment);
    }

    public static abstract class BaseHistory implements Listener {
        protected HistoryManager historyManager;

        @Override
        public void onAdd(HistoryManager historyManager) {
            this.historyManager = historyManager;
        }

        @Override
        public void onRemove(HistoryManager historyManager) {
            this.historyManager = null;
        }
    }

    private List<Listener> listeners = new ArrayList<Listener>();

    public HistoryManager() {
    }

    public void setFragment(Fragment fragment) {
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
        listener.onAdd(this);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
        listener.onRemove(this);
    }
}
