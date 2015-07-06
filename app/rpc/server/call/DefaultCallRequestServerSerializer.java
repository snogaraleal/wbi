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

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import rpc.shared.call.CallRequest;
import rpc.shared.call.InvalidPayload;

/**
 * Default implementation of a {@link CallRequest.ServerSerializer}.
 */
public class DefaultCallRequestServerSerializer
    implements CallRequest.ServerSerializer {

    private static JsonParser parser = new JsonParser();

    /**
     * Initialize {@code DefaultCallRequestServerSerializer}.
     */
    public DefaultCallRequestServerSerializer() {}

    @Override
    public CallRequest deserialize(String payload) throws InvalidPayload {
        JsonElement jsonElement = parser.parse(payload);

        JsonArray asJsonArray = jsonElement.getAsJsonArray();

        if (asJsonArray.size() != CallRequest.Message.SIZE) {
            throw new InvalidPayload();
        }

        String className = asJsonArray.get(
            CallRequest.Message.POSITION_CLASS_NAME).getAsString();
        String methodName = asJsonArray.get(
            CallRequest.Message.POSITION_METHOD_NAME).getAsString();
        String token = asJsonArray.get(
            CallRequest.Message.POSITION_TOKEN).getAsString();

        JsonArray argumentsJsonArray = asJsonArray.get(
            CallRequest.Message.POSITION_ARGUMENTS).getAsJsonArray();
        int argumentsJsonArraySize = argumentsJsonArray.size();

        List<String> arguments = new ArrayList<String>();

        for (int i = 0; i < argumentsJsonArraySize; i++) {
            arguments.add(argumentsJsonArray.get(i).getAsString());
        }

        return new CallRequest(className, methodName, arguments, token);
    }
}