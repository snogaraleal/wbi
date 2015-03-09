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

package client.services;

import java.util.List;

import rpc.client.ClientRequest;
import rpc.shared.data.Type;

import models.Indicator;

public class WBIManagementService {
    public static final String CLASS_NAME = "services.WBIManagementService";

    public static ClientRequest<List<Indicator>> poll(
            List<Long> indicatorIds,
            ClientRequest.Listener<List<Indicator>> listener) {

        return new ClientRequest<List<Indicator>>(CLASS_NAME, "poll")
            .setArguments(indicatorIds)
            .setExpected(Type.get(List.class, Type.get(Indicator.class)))
            .addListener(listener)
            .send();
    }

    public static ClientRequest<Indicator> load(
            Long indicatorId, ClientRequest.Listener<Indicator> listener) {

        return new ClientRequest<Indicator>(CLASS_NAME, "load")
            .setArguments(indicatorId)
            .setExpected(Type.get(Indicator.class))
            .addListener(listener)
            .send();
    }

    public static ClientRequest<Indicator> unload(
            Long indicatorId, ClientRequest.Listener<Indicator> listener) {

        return new ClientRequest<Indicator>(CLASS_NAME, "unload")
            .setArguments(indicatorId)
            .setExpected(Type.get(Indicator.class))
            .addListener(listener)
            .send();
    }
}
