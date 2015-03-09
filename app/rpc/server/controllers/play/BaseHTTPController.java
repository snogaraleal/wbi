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

package rpc.server.controllers.play;

import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

import rpc.server.GlobalHandler;
import rpc.shared.call.CallRequest;
import rpc.shared.call.CallResponse;
import rpc.shared.call.InvalidPayload;

public class BaseHTTPController extends BaseController {
    public static Result call() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest();
        }

        String body = json.toString();

        CallRequest request;
        
        try {
            request = requestSerializer.deserialize(body);
        } catch (InvalidPayload exception) {
            return badRequest(exception.toString());
        }

        CallResponse response = GlobalHandler.handle(request);

        String payload = responseSerializer.serialize(response);

        response().setContentType("application/json");

        if (response.isSuccess()) {
            return ok(payload);
        } else {
            return badRequest(payload);
        }
    }
}
