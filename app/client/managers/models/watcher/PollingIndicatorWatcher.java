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

public class PollingIndicatorWatcher extends IndicatorWatcher {
    private static int DEFAULT_INTERVAL = 2000;

    private int interval;
    private ClientRequest.Listener<List<Indicator>> pollRequestListener;
    private Timer timer;

    public PollingIndicatorWatcher(int interval) {
        super();

        this.interval = interval;

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
