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

package client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Document;

import rpc.client.Client;
import rpc.client.data.JSONSerializer;
import rpc.shared.data.Serializer;
import rpc.shared.data.SerializerException;
import rpc.shared.data.Type;

/**
 * Global configuration provider.
 */
public class ClientConf {
    /**
     * Setting specified by the server.
     */
    public static enum Setting {
        /**
         * Application start page.
         */
        INDEX_URL("index"),

        /**
         * Static assets URL.
         */
        ASSETS_URL("assets"),

        /**
         * RPC HTTP dispatcher URL.
         */
        RPC_SERVICE_HTTP_URL("http"),

        /**
         * RPC WebSocket dispatcher URL.
         */
        RPC_SERVICE_WS_URL("ws");

        /**
         * Setting name.
         */
        private String name;

        /**
         * Initialize {@code Setting}.
         *
         * @param name Setting name.
         */
        private Setting(String name) {
            this.name = name;
        }

        /**
         * Get setting name.
         *
         * @return Setting name.
         */
        public String getName() {
            return name;
        }
    }

    /**
     * Group of settings.
     */
    public static class Configuration {
        private Map<Setting, Object> data;

        /**
         * Initialize {@code Configuration}.
         */
        public Configuration() {
            this.data = new HashMap<Setting, Object>();
        }

        /**
         * Initialize {@code Configuration} with initial data.
         * 
         * @param data Initial data.
         */
        private Configuration(Map<Setting, Object> data) {
            this.data = data;
        }

        /**
         * Set setting value.
         *
         * @param setting Setting to set value to.
         * @param value Setting value.
         */
        public void set(Setting setting, Object value) {
            data.put(setting, value);
        }

        /**
         * Get setting value.
         *
         * @param setting Setting to get value from.
         * @return Setting value.
         */
        public Object get(Setting setting) {
            return data.get(setting);
        }

        /**
         * Serialize configuration.
         *
         * @param serializer {@code Serializer} used to deserialize.
         * @return Serialized configuration.
         * @throws SerializerException
         */
        public String serialize(Serializer serializer)
                throws SerializerException {

            return serializer.serialize(data);
        }

        /**
         * Deserialize configuration.
         *
         * @param serializer {@code Serializer} used to serialize.
         * @param payload Serialized payload.
         * @return Deserialized {@code Configuration}.
         */
        @SuppressWarnings("unchecked")
        public static Configuration deserialize(
                Serializer serializer, String payload)
                        throws SerializerException {

            Type type = Type.get(
                HashMap.class,
                Type.get(Setting.class),
                Type.get(Object.class));

            Map<Setting, Object> data = (HashMap<Setting, Object>)
                defaultSerializer.deserialize(payload, type);

            return new Configuration(data);
        }
    }

    /**
     * Global RPC serializer.
     */
    public static Serializer defaultSerializer =
        new JSONSerializer(GlobalSerializableFactoryProvider.get());

    /**
     * Attribute in {@code <head>} containing serialized configuration.
     */
    public static String HEAD_ATTR = "data-conf";

    /**
     * Load client configuration from {@code <head>}.
     *
     * @return Deserialized settings.
     */
    private static Configuration loadConfiguration() {
        Configuration configuration;

        String payload = Document.get().getHead().getAttribute(HEAD_ATTR);

        try {
            configuration = Configuration.deserialize(
                defaultSerializer, payload);
        } catch (SerializerException e) {
            configuration = new Configuration();
        }

        return configuration;
    }

    /*
     * Configuration lazy loading.
     */

    private static Configuration configuration;

    private static Configuration getConfiguration() {
        if (configuration == null) {
            configuration = loadConfiguration();
        }

        return configuration;
    }

    private static Object getSetting(Setting setting) {
        return getConfiguration().get(setting);
    }

    /**
     * Get {@code String} setting.
     *
     * @param setting {@link Setting} to get.
     * @return Setting value.
     */
    public static String getString(Setting setting) {
        return (String) getSetting(setting);
    }

    /**
     * Get {@code Boolean} setting.
     *
     * @param setting {@link Setting} to get.
     * @return Setting value.
     */
    public static boolean getBoolean(Setting setting) {
        return (Boolean) getSetting(setting);
    }

    /**
     * Get {@code Integer} setting.
     *
     * @param setting {@link Setting} to get.
     * @return Setting value.
     */
    public static int getInteger(Setting setting) {
        return (Integer) getSetting(setting);
    }

    /**
     * Get {@code Float} setting.
     *
     * @param setting {@link Setting} to get.
     * @return Setting value.
     */
    public static double getFloat(Setting setting) {
        return (Float) getSetting(setting);
    }

    /**
     * Get {@code Double} setting.
     *
     * @param setting {@link Setting} to get.
     * @return Setting value.
     */
    public static double getDouble(Setting setting) {
        return (Double) getSetting(setting);
    }

    /**
     * Get full asset URL.
     *
     * @param path Asset path.
     * @return Full asset URL.
     */
    public static String asset(String path) {
        return getString(Setting.ASSETS_URL) + path;
    }

    /**
     * Configure RPC {@link Client}.
     */
    public static void configureRPC() {
        Client.setConfiguration(
            new Client.Configuration(
                defaultSerializer,
                getString(Setting.RPC_SERVICE_HTTP_URL),
                getString(Setting.RPC_SERVICE_WS_URL)));
    }
}
