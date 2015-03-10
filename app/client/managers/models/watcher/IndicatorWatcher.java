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

package client.managers.models.watcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Indicator;

/**
 * Real-time watcher for changes in {@link Indicator} objects.
 */
public abstract class IndicatorWatcher {
    /**
     * Interface for {@link IndicatorWatcher} listeners.
     */
    public interface Listener {
        /**
         * Handle {@code Indicator} change.
         *
         * @param indicator Updated indicator.
         */
        public void onChange(Indicator indicator);
    }

    /**
     * {@code Listener} objects listening to changes in this manager.
     */
    protected List<Listener> listeners = new ArrayList<Listener>();

    /**
     * {@code Indicator} objects being watched.
     */
    protected Map<Indicator, List<Indicator.Status>> watched =
        new HashMap<Indicator, List<Indicator.Status>>();

    protected IndicatorWatcher() {}

    /**
     * Start watching for changes in an {@code Indicator}.
     *
     * @param indicator Indicator to watch.
     */
    public void watch(Indicator indicator) {
        if (!watched.containsKey(indicator)) {
            watched.put(indicator, null);
        }
    }

    /**
     * Stop watching for changes in an {@code Indicator}.
     *
     * @param indicator Indicator to stop watching.
     */
    public void unwatch(Indicator indicator) {
        if (watched.containsKey(indicator)) {
            watched.remove(indicator);
        }
    }

    /**
     * Start watching an {@code Indicator} until its status equals to the
     * specified {@code Indicator.Status}.
     *
     * @param indicator Indicator to watch.
     * @param status Status required.
     */
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

    /**
     * Attach {@code Listener}.
     *
     * @param listener Listener to attach.
     */
    public void addListener(Listener listener) {
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
     * Start watching.
     */
    public abstract void start();

    /**
     * Stop watching.
     */
    public abstract void stop();

    /**
     * Stop watching an {@code Indicator} only if the required status is met.
     *
     * @param indicator Indicator to stop watching.
     */
    private void autoUnwatch(Indicator indicator) {
        List<Indicator.Status> watchedStatusList = watched.get(indicator);

        if (watchedStatusList != null) {
            watchedStatusList.remove(indicator.getStatus());

            if (watchedStatusList.isEmpty()) {
                watched.remove(indicator);
            }
        }
    }

    /**
     * Exhibit changes in an {@code Indicator}.
     *
     * @param indicator Indicator changed.
     */
    protected void change(Indicator indicator) {
        for (Listener listener : listeners) {
            listener.onChange(indicator);
            autoUnwatch(indicator);
        }
    }

    /**
     * Create a new instance of {@code IndicatorWatcher}.
     *
     * @return Instance created.
     */
    public static IndicatorWatcher create() {
        if (LiveIndicatorWatcher.isSupported()) {
            return new LiveIndicatorWatcher();
        } else {
            return new PollingIndicatorWatcher();
        }
    }
}
