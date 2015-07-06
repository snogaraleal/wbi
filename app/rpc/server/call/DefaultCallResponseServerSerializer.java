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

package rpc.server.call;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

import rpc.shared.call.CallResponse;

/**
 * Default implementation of a {@link CallResponse.ServerSerializer}.
 */
public class DefaultCallResponseServerSerializer
    implements CallResponse.ServerSerializer {

    /**
     * Initialize {@code DefaultCallResponseServerSerializer}.
     */
    public DefaultCallResponseServerSerializer() {}

    @Override
    public String serialize(CallResponse response) {
        JsonArray jsonArray = new JsonArray();

        assert CallResponse.Message.POSITION_TOKEN == 0;
        jsonArray.add(new JsonPrimitive(response.getToken()));

        assert CallResponse.Message.POSITION_SUCCESS == 1;
        jsonArray.add(new JsonPrimitive(response.isSuccess()));

        assert CallResponse.Message.POSITION_RETURN_VALUE == 2;
        jsonArray.add(new JsonPrimitive(response.getPayload()));

        return jsonArray.toString();
    }
}