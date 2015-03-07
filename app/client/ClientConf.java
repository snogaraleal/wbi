package client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Document;

import rpc.client.Client;
import rpc.client.data.JSONSerializer;
import rpc.shared.data.Serializer;
import rpc.shared.data.SerializerException;
import rpc.shared.data.Type;

public class ClientConf {
    public static String HEAD_ATTR = "data-conf";

    public static class Setting {
        public static String INDEX_URL = "index";
        public static String ASSETS_URL = "assets";

        public static String RPC_SERVICE_HTTP_URL = "http";
        public static String RPC_SERVICE_WS_URL = "ws";
    }

    public static Serializer defaultSerializer =
        new JSONSerializer(GlobalSerializableFactoryProvider.get());

    @SuppressWarnings("unchecked")
    private static Map<String, Object> loadClientConf() {
        Map<String, Object> clientConf;

        String json = Document.get().getHead().getAttribute(HEAD_ATTR);

        try {
            Type type = Type.get(
                HashMap.class,
                Type.get(String.class),
                Type.get(Object.class));

            clientConf = (HashMap<String, Object>)
                defaultSerializer.deserialize(json, type);

        } catch (SerializerException exception) {
            clientConf = new HashMap<String, Object>();
        }

        return clientConf;
    }

    private static Map<String, Object> clientConf;

    private static Map<String, Object> getClientConf() {
        if (clientConf == null) {
            clientConf = loadClientConf();
        }

        return clientConf;
    }

    public static String getString(String name) {
        return (String) getClientConf().get(name);
    }

    public static boolean getBoolean(String name) {
        return (Boolean) getClientConf().get(name);
    }

    public static int getInteger(String name) {
        return (Integer) getClientConf().get(name);
    }

    public static double getFloat(String name) {
        return (Float) getClientConf().get(name);
    }

    public static double getDouble(String name) {
        return (Double) getClientConf().get(name);
    }

    public static String asset(String url) {
        return getString(Setting.ASSETS_URL) + url;
    }

    public static void configureRPC() {
        Client.setConfiguration(
            new Client.Configuration(
                defaultSerializer,
                getString(Setting.RPC_SERVICE_HTTP_URL),
                getString(Setting.RPC_SERVICE_WS_URL)));
    }
}
