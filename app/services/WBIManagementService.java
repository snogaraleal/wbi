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

package services;

import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;

import rpc.server.Service;

import models.Indicator;

import data.tasks.IndicatorLoadTask;
import data.tasks.IndicatorUnloadTask;
import data.tasks.TaskUtils;

/**
 * Service for loading and unloading indicators.
 */
public class WBIManagementService implements Service {
    /**
     * Update the {@link Indicator.Status} of the specified {@link Indicator}.
     *
     * @param indicator Indicator to update.
     * @param status New indicator status.
     */
    private static void updateIndicatorStatus(
            Indicator indicator, Indicator.Status status) {

        SqlUpdate updateIndicator = Ebean.createSqlUpdate(
            "UPDATE indicator " +
            "SET status = :status " +
            "WHERE id = :id");

        updateIndicator.setParameter("status", status);
        updateIndicator.setParameter("id", indicator.getId());
        updateIndicator.execute();

        indicator.setStatus(status);
    }

    /**
     * Task that loads an {@link Indicator} and updates its status when
     * finished.
     */
    private static class IndicatorLoadAndUpdateTask
        extends IndicatorLoadTask {

        /**
         * Initialize task.
         *
         * @param indicator {@code Indicator} to load.
         */
        public IndicatorLoadAndUpdateTask(Indicator indicator) {
            super(indicator);
        }

        @Override
        public void run() {
            super.run();

            updateIndicatorStatus(indicator, Indicator.Status.READY);
        }
    }

    /**
     * Task that unloads an {@link Indicator} and updates its status when
     * finished.
     */
    private static class IndicatorUnloadAndUpdateTask 
        extends IndicatorUnloadTask {

        /**
         * Initialize task.
         *
         * @param indicator {@code Indicator} to unload.
         */
        public IndicatorUnloadAndUpdateTask(Indicator indicator) {
            super(indicator);
        }

        @Override
        public void run() {
            super.run();

            updateIndicatorStatus(indicator, Indicator.Status.AVAILABLE);
        }
    }

    /**
     * Get a list of {@link Indicator} objects for the specified list of IDs.
     *
     * @param indicatorIds List of {@code Indicator} IDs.
     * @return List of {@code Indicator} objects.
     */
    public static List<Indicator> poll(List<Long> indicatorIds) {
        if (indicatorIds.isEmpty()) {
            return null;
        }

        return Indicator.objects
            .where()
                .idIn(indicatorIds)
            .query()
            .fetch("source")
            .fetch("topics")
            .findList();
    }

    /**
     * Load the specified {@link Indicator}.
     *
     * @param indicatorId ID of the {@code Indicator} to load.
     * @return {@code Indicator} with status changed.
     */
    public static Indicator load(Long indicatorId) {
        Indicator indicator = Indicator.objects.byId(indicatorId);

        if (indicator.isAvailable()) {
            updateIndicatorStatus(indicator, Indicator.Status.LOADING);

            TaskUtils.runTask(new IndicatorLoadAndUpdateTask(indicator));
        }

        return indicator;
    }

    /**
     * Unload the specified {@link Indicator}.
     *
     * @param indicatorId ID of the {@code Indicator} to unload.
     * @return {@code Indicator} with status changed.
     */
    public static Indicator unload(Long indicatorId) {
        Indicator indicator = Indicator.objects.byId(indicatorId);

        if (indicator.isReady()) {
            updateIndicatorStatus(indicator, Indicator.Status.LOADING);

            TaskUtils.runTask(new IndicatorUnloadAndUpdateTask(indicator));
        }

        return indicator;
    }
}
