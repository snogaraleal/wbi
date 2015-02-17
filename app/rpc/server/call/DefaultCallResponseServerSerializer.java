package rpc.server.call;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

import rpc.shared.call.CallResponse;

public class DefaultCallResponseServerSerializer
    implements CallResponse.ServerSerializer {

    public DefaultCallResponseServerSerializer() {
    }

    @Override
    public String serialize(CallResponse response) {
        JsonArray jsonArray = new JsonArray();

        assert CallResponse.Message.POSITION_TOKEN == 0;
        jsonArray.add(new JsonPrimitive(response.getToken()));

        assert CallResponse.Message.POSITION_SUCCESS == 1;
        jsonArray.add(new JsonPrimitive(response.isSuccess()));

        assert CallResponse.Message.POSITION_RETURN_VALUE == 2;
        jsonArray.add(new JsonPrimitive(response.getPayload()));

        return jsonArray.toString();
    }
}
