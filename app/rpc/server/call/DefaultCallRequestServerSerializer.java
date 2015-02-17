package rpc.server.call;

import java.util.List;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import rpc.shared.call.CallRequest;
import rpc.shared.call.InvalidPayload;

public class DefaultCallRequestServerSerializer
    implements CallRequest.ServerSerializer {

    private static JsonParser parser = new JsonParser();

    public DefaultCallRequestServerSerializer() {
    }

    @Override
    public CallRequest deserialize(String payload) throws InvalidPayload {
        JsonElement jsonElement = parser.parse(payload);

        JsonArray asJsonArray = jsonElement.getAsJsonArray();

        if (asJsonArray.size() != CallRequest.Message.SIZE) {
            throw new InvalidPayload();
        }

        String className = asJsonArray.get(
            CallRequest.Message.POSITION_CLASS_NAME).getAsString();
        String methodName = asJsonArray.get(
            CallRequest.Message.POSITION_METHOD_NAME).getAsString();
        String token = asJsonArray.get(
            CallRequest.Message.POSITION_TOKEN).getAsString();

        JsonArray argumentsJsonArray = asJsonArray.get(
            CallRequest.Message.POSITION_ARGUMENTS).getAsJsonArray();
        int argumentsJsonArraySize = argumentsJsonArray.size();

        List<String> arguments = new ArrayList<String>();

        for (int i = 0; i < argumentsJsonArraySize; i++) {
            arguments.add(argumentsJsonArray.get(i).getAsString());
        }

        return new CallRequest(className, methodName, arguments, token);
    }
}
