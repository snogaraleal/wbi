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

import models.Country;
import models.Indicator;
import models.Series;

import client.managers.history.HistoryState;
import client.managers.history.HistoryStateData;

public class WBIExplorationService {
    public static final String CLASS_NAME = "services.WBIExplorationService";

    public static ClientRequest<List<Indicator>> queryIndicatorList(
            String query, ClientRequest.Listener<List<Indicator>> listener) {

        return new ClientRequest<List<Indicator>>(
                CLASS_NAME, "queryIndicatorList")
            .setArguments(query)
            .setExpected(Type.get(List.class, Type.get(Indicator.class)))
            .addListener(listener)
            .send();
    }

    public static ClientRequest<List<Country>> queryCountryList(
            String query, ClientRequest.Listener<List<Country>> listener) {

        return new ClientRequest<List<Country>>(CLASS_NAME, "queryCountryList")
            .setArguments(query)
            .setExpected(Type.get(List.class, Type.get(Country.class)))
            .addListener(listener)
            .send();
    }

    public static ClientRequest<List<Series>> querySeriesList(
            Long indicatorId, Integer startYear, Integer endYear,
            ClientRequest.Listener<List<Series>> listener) {

        return new ClientRequest<List<Series>>(CLASS_NAME, "querySeriesList")
            .setArguments(indicatorId, startYear, endYear)
            .setExpected(Type.get(List.class, Type.get(Series.class)))
            .addListener(listener)
            .send();
    }

    public static ClientRequest<HistoryStateData> getStateData(
            HistoryState state,
            ClientRequest.Listener<HistoryStateData> listener) {

        return new ClientRequest<HistoryStateData>(
                CLASS_NAME, "getStateData")
            .setArguments(state)
            .setExpected(Type.get(HistoryStateData.class))
            .addListener(listener)
            .send();
    }
}
