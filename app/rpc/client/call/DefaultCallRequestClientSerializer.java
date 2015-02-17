package rpc.client.call;

import java.util.List;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;

import rpc.shared.call.CallRequest;

public class DefaultCallRequestClientSerializer
    implements CallRequest.ClientSerializer {

    public DefaultCallRequestClientSerializer() {
    }

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
