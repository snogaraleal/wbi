package client.managers.models.watcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Indicator;

public abstract class IndicatorWatcher {
    public interface Listener {
        public void onChange(Indicator indicator);
    }

    protected List<Listener> listeners = new ArrayList<Listener>();
    protected Map<Indicator, List<Indicator.Status>> watched =
        new HashMap<Indicator, List<Indicator.Status>>();

    public IndicatorWatcher() {
    }

    public void watch(Indicator indicator) {
        if (!watched.containsKey(indicator)) {
            watched.put(indicator, null);
        }
    }

    public void unwatch(Indicator indicator) {
        if (watched.containsKey(indicator)) {
            watched.remove(indicator);
        }
    }

    public void watchUntilStatusEquals(
            Indicator indicator,
            Indicator.Status status) {

        List<Indicator.Status> watchedStatusList = watched.get(indicator);

        if (watchedStatusList == null) {
            watchedStatusList = new ArrayList<Indicator.Status>();
            watchedStatusList.add(status);
            watched.put(indicator, watchedStatusList);
        } else {
            if (!watchedStatusList.contains(status)) {
                watchedStatusList.add(status);
            }
        }
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public abstract void start();
    public abstract void stop();

    private void autoUnwatch(Indicator indicator) {
        List<Indicator.Status> watchedStatusList = watched.get(indicator);

        if (watchedStatusList != null) {
            watchedStatusList.remove(indicator.getStatus());

            if (watchedStatusList.isEmpty()) {
                watched.remove(indicator);
            }
        }
    }

    protected void change(Indicator indicator) {
        for (Listener listener : listeners) {
            listener.onChange(indicator);
            autoUnwatch(indicator);
        }
    }

    public static IndicatorWatcher create() {
        if (LiveIndicatorWatcher.isSupported()) {
            return new LiveIndicatorWatcher();
        } else {
            return new PollingIndicatorWatcher();
        }
    }
}
