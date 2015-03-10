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
import java.util.List;

import com.google.gwt.user.client.Timer;

import rpc.client.ClientRequest;

import models.Indicator;

import client.services.WBIManagementService;

/**
 * Implementation of {@code IndicatorWatcher} that polls the server for
 * changes with the RPC mechanism.
 */
public class PollingIndicatorWatcher extends IndicatorWatcher {
    /**
     * Default value for {@code interval}.
     */
    public static final int DEFAULT_INTERVAL = 2000;

    /**
     * Time in milliseconds between each request.
     */
    private int interval;

    /**
     * {@code ClientRequest.Listener} handling RPC responses.
     */
    private ClientRequest.Listener<List<Indicator>> pollRequestListener;

    /**
     * Timer in charge of making requests periodically.
     */
    private Timer timer;

    /**
     * Initialize {@code PollingIndicatorWatcher}.
     *
     * @param interval Time in milliseconds between each request.
     */
    public PollingIndicatorWatcher(int interval) {
        super();

        this.interval = interval;

        /*
         * Initialize RPC handler.
         */
        pollRequestListener = new ClientRequest.Listener<List<Indicator>>() {
            @Override
            public void onSuccess(List<Indicator> indicators) {
                for (Indicator indicator : indicators) {
                    change(indicator);
                }
            }

            @Override
            public void onFailure(ClientRequest.Error error) {
            }
        };

        /*
         * Initialize timer.
         */
        timer = new Timer() {
            @Override
            public void run() {
                if (!watched.isEmpty()) {
                    List<Long> indicatorIds = new ArrayList<Long>();
                    for (Indicator indicator : watched.keySet()) {
                        indicatorIds.add(indicator.getId());
                    }

                    WBIManagementService.poll(
                        indicatorIds, pollRequestListener);
                }
            }
        };
    }

    /**
     * Initialize {@code PollingIndicatorWatcher} with the default interval.
     */
    public PollingIndicatorWatcher() {
        this(DEFAULT_INTERVAL);
    }

    @Override
    public void start() {
        timer.scheduleRepeating(interval);
    }

    @Override
    public void stop() {
        timer.cancel();
    }
}
