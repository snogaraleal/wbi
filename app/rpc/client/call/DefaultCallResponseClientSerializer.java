package rpc.client.call;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import rpc.shared.call.CallResponse;
import rpc.shared.call.InvalidPayload;

public class DefaultCallResponseClientSerializer
    implements CallResponse.ClientSerializer {

    public DefaultCallResponseClientSerializer() {
    }

    @Override
    public CallResponse deserialize(String payload) throws InvalidPayload {
        JSONArray array = JSONParser.parseStrict(payload).isArray();

        if (array.size() != CallResponse.Message.SIZE) {
            throw new InvalidPayload();
        }

        JSONString token = array.get(
            CallResponse.Message.POSITION_TOKEN).isString();
        JSONBoolean success = array.get(
            CallResponse.Message.POSITION_SUCCESS).isBoolean();
        JSONString returnValue = array.get(
            CallResponse.Message.POSITION_RETURN_VALUE).isString();

        return new CallResponse(
            token.stringValue(),
            success.booleanValue(),
            returnValue.stringValue());
    }
}
