package rpc.client;

import java.util.List;
import java.util.ArrayList;

import rpc.shared.data.Type;
import rpc.shared.data.Serializer;
import rpc.shared.data.SerializerException;
import rpc.shared.call.CallRequest;
import rpc.shared.call.CallResponse;
import rpc.shared.call.InvalidPayload;
import rpc.client.call.DefaultCallRequestClientSerializer;
import rpc.client.call.DefaultCallResponseClientSerializer;

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

    public abstract void send(ClientRequest clientRequest);
    public abstract void cancel(ClientRequest clientRequest);

    protected CallRequest buildCallRequest(ClientRequest clientRequest)
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
