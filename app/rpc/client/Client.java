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

package rpc.client;

import java.util.ArrayList;
import java.util.List;

import rpc.client.call.DefaultCallRequestClientSerializer;
import rpc.client.call.DefaultCallResponseClientSerializer;
import rpc.shared.call.CallRequest;
import rpc.shared.call.CallResponse;
import rpc.shared.call.InvalidPayload;
import rpc.shared.data.Serializer;
import rpc.shared.data.SerializerException;
import rpc.shared.data.Type;

/**
 * RPC client.
 *
 * This is the highest-level class of the client-side package.
 *
 * Derived classes provide an appropriate implementation of the
 * {@link #send(ClientRequest)} and {@link #cancel(ClientRequest)} methods.
 *
 * @see {@link HTTPClient} and {@link WebSocketClient}.
 */
public abstract class Client {
    /**
     * Client configuration.
     */
    public static class Configuration {
        /**
         * Global {@link Serializer}.
         */
        private Serializer serializer;

        /**
         * HTTP endpoint.
         */
        private String httpURL;

        /**
         * WebSocket endpoint.
         */
        private String wsURL;

        /**
         * Initialize {@link Client.Configuration}.
         *
         * @param serializer Global {@link Serializer}.
         * @param httpURL HTTP endpoint.
         * @param wsURL WebSocket endpoint.
         */
        public Configuration(
                Serializer serializer,
                String httpURL, String wsURL) {

            this.serializer = serializer;
            this.httpURL = httpURL;
            this.wsURL = wsURL;
        }

        /**
         * Get global serializer.
         *
         * @return Global serializer.
         */
        public Serializer getSerializer() {
            return serializer;
        }

        /**
         * Get HTTP endpoint.
         *
         * @return HTTP endpoint.
         */
        public String httpURL() {
            return httpURL;
        }

        /**
         * Get WebSocket endpoint.
         *
         * @return WebSocket endpoint.
         */
        public String wsURL() {
            return wsURL;
        }
    }

    /**
     * Current client configuration.
     */
    private static Configuration configuration;

    /**
     * Set {@link Client.Configuration}.
     *
     * @param config Client configuration.
     */
    public static void setConfiguration(Configuration config) {
        configuration = config;
    }

    /**
     * Load {@link Client} class.
     *
     * @return New {@code Client} object.
     */
    private static Client load() {
        if (WebSocketClient.isSupported()) {
            return new WebSocketClient(
                configuration.getSerializer(),
                configuration.wsURL());
        } else {
            return new HTTPClient(
                configuration.getSerializer(),
                configuration.httpURL());
        }
    }

    private static Client client;

    /**
     * Get the single {@link Client} instance.
     *
     * @return {@code Client} instance.
     */
    public static Client get() {
        if (client == null) {
            client = load();
        }

        return client;
    }

    protected static CallRequest.ClientSerializer requestSerializer =
        new DefaultCallRequestClientSerializer();
    protected static CallResponse.ClientSerializer responseSerializer =
        new DefaultCallResponseClientSerializer();

    protected Serializer serializer;

    /**
     * Initialize {@code Client}.
     */
    public Client() {}

    /**
     * Initialize {@code Client} with the specified object serializer.
     *
     * @param serializer {@link Serializer}.
     */
    public Client(Serializer serializer) {
        this.serializer = serializer;
    }

    /**
     * Send the specified {@link ClientRequest}.
     *
     * @param clientRequest RPC request.
     */
    public abstract void send(ClientRequest<?> clientRequest);

    /**
     * Cancel the specified {@link ClientRequest}.
     *
     * @param clientRequest RPC request.
     */
    public abstract void cancel(ClientRequest<?> clientRequest);

    /**
     * Build a low-level serializable {@link CallRequest} from a high-level
     * {@link ClientRequest}.
     *
     * @param clientRequest RPC request.
     * @return Serializable {@link CallRequest}.
     * @throws SerializerException
     */
    protected CallRequest buildCallRequest(ClientRequest<?> clientRequest)
        throws SerializerException {

        List<String> argumentPayloadList = new ArrayList<String>();

        for (Object argument : clientRequest.getArguments()) {
            argumentPayloadList.add(serializer.serialize(argument));
        }

        return new CallRequest(
            clientRequest.getClassName(),
            clientRequest.getMethodName(),
            argumentPayloadList);
    }

    /**
     * Serialize {@link CallRequest} to be sent.
     *
     * @param callRequest {@code CallRequest} to serialize.
     * @return Serialized request.
     */
    protected String toRequestMessage(CallRequest callRequest) {
        return requestSerializer.serialize(callRequest);
    }

    /**
     * Deserialize received {@link CallResponse}.
     *
     * @param message Serialized response.
     * @return Deserialized {@code CallResponse}.
     * @throws InvalidPayload
     */
    protected CallResponse fromResponseMessage(String message)
        throws InvalidPayload {

        return responseSerializer.deserialize(message);
    }

    /**
     * Deserialize {@link Serializable} object.
     *
     * @param payload Serialized contents.
     * @param expected Expected object type.
     * @return Deserialized {@code Serializable} object.
     * @throws SerializerException
     */
    protected Object deserialize(String payload, Type expected)
        throws SerializerException {

        return serializer.deserialize(payload, expected);
    }
}