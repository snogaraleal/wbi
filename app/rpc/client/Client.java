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

public abstract class Client {
    public static class Configuration {
        private Serializer serializer;
        private String httpURL;
        private String wsURL;

        public Configuration(
                Serializer serializer,
                String httpURL, String wsURL) {

            this.serializer = serializer;
            this.httpURL = httpURL;
            this.wsURL = wsURL;
        }

        public Serializer getSerializer() {
            return serializer;
        }

        public String httpURL() {
            return httpURL;
        }

        public String wsURL() {
            return wsURL;
        }
    }

    private static Configuration configuration;

    public static void setConfiguration(Configuration config) {
        configuration = config;
    }

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

    public Client() {
    }

    public Client(Serializer serializer) {
        this.serializer = serializer;
    }

    public abstract void send(ClientRequest<?> clientRequest);
    public abstract void cancel(ClientRequest<?> clientRequest);

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

    protected String toRequestMessage(CallRequest callRequest) {
        return requestSerializer.serialize(callRequest);
    }

    protected CallResponse fromResponseMessage(String message)
        throws InvalidPayload {

        return responseSerializer.deserialize(message);
    }

    protected Object deserialize(String payload, Type expected)
        throws SerializerException {

        return serializer.deserialize(payload, expected);
    }
}
