package controllers;

import java.util.Map;
import java.util.HashMap;

import play.mvc.Http.Request;

import rpc.shared.data.Serializer;
import rpc.server.data.JSONSerializer;
import rpc.server.invoke.Invoker;
import rpc.server.GlobalHandler;

import models.ModelSerializableFactoryProvider;
import client.ClientConf;

public class ServerConf {
    public static Serializer defaultSerializer =
        new JSONSerializer(ModelSerializableFactoryProvider.get());

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
