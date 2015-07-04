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

package rpc.client.call;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import rpc.shared.call.CallResponse;
import rpc.shared.call.InvalidPayload;

/**
 * Default implementation of a {@link CallResponse.ClientSerializer}.
 */
public class DefaultCallResponseClientSerializer
    implements CallResponse.ClientSerializer {

    /**
     * Initialize {@code DefaultCallResponseClientSerializer}.
     */
    public DefaultCallResponseClientSerializer() {}

    @Override
    public CallResponse deserialize(String payload) throws InvalidPayload {
        JSONArray array = JSONParser.parseStrict(payload).isArray();

        if (array.size() != CallResponse.Message.SIZE) {
            throw new InvalidPayload();
        }

        JSONString token = array.get(
            CallResponse.Message.POSITION_TOKEN).isString();
        JSONBoolean success = array.get(
            CallResponse.Message.POSITION_SUCCESS).isBoolean();
        JSONString returnValue = array.get(
            CallResponse.Message.POSITION_RETURN_VALUE).isString();

        return new CallResponse(
            token.stringValue(),
            success.booleanValue(),
            returnValue.stringValue());
    }
}