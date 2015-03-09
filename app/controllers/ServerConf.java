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

package controllers;

import java.util.HashMap;
import java.util.Map;

import play.mvc.Http.Request;

import rpc.server.GlobalHandler;
import rpc.server.data.JSONSerializer;
import rpc.server.invoke.Invoker;
import rpc.shared.data.Serializer;

import client.ClientConf;
import client.GlobalSerializableFactoryProvider;

public class ServerConf {
    public static Serializer defaultSerializer =
        new JSONSerializer(GlobalSerializableFactoryProvider.get());

    public static void configureRPC() {
        GlobalHandler.setDefaultInvoker(new Invoker(defaultSerializer));
    }

    private static Map<String, Object> loadClientConf(Request request) {
        Map<String, Object> clientConf = new HashMap<String, Object>();

        clientConf.put(
            ClientConf.Setting.INDEX_URL,
            routes.Application.index().url());

        clientConf.put(
            ClientConf.Setting.ASSETS_URL,
            controllers.routes.Assets.at("").url());

        clientConf.put(
            ClientConf.Setting.RPC_SERVICE_HTTP_URL,
            controllers.rpc.routes.HTTPController.call().url());

        clientConf.put(
            ClientConf.Setting.RPC_SERVICE_WS_URL,
            controllers.rpc.routes.WebSocketController.socket().webSocketURL(
                request));

        return clientConf;
    }

    private static Map<String, Object> clientConf;

    public static Map<String, Object> getClientConf(Request request) {
        if (clientConf == null) {
            clientConf = loadClientConf(request);
        }

        return clientConf;
    }
}
