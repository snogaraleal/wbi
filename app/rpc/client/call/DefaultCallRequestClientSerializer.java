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

import java.util.List;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;

import rpc.shared.call.CallRequest;

public class DefaultCallRequestClientSerializer
    implements CallRequest.ClientSerializer {

    public DefaultCallRequestClientSerializer() {}

    @Override
    public String serialize(CallRequest request) {
        List<String> arguments = request.getArgumentPayloadList();
        int argumentsSize = arguments.size();

        JSONArray argumentsArray = new JSONArray();

        for (int i = 0; i < argumentsSize; i++) {
            argumentsArray.set(i, new JSONString(arguments.get(i)));
        }

        JSONArray messageArray = new JSONArray();

        messageArray.set(
            CallRequest.Message.POSITION_CLASS_NAME,
            new JSONString(request.getClassName()));

        messageArray.set(
            CallRequest.Message.POSITION_METHOD_NAME,
            new JSONString(request.getMethodName()));

        messageArray.set(
            CallRequest.Message.POSITION_TOKEN,
            new JSONString(request.getToken()));

        messageArray.set(
            CallRequest.Message.POSITION_ARGUMENTS,
            argumentsArray);

        return messageArray.toString();
    }
}
