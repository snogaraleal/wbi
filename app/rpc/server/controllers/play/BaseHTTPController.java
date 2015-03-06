package rpc.server.controllers.play;

import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

import rpc.server.GlobalHandler;
import rpc.shared.call.CallRequest;
import rpc.shared.call.CallResponse;
import rpc.shared.call.InvalidPayload;

public class BaseHTTPController extends BaseController {
    public static Result call() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest();
        }

        String body = json.toString();

        CallRequest request;
        
        try {
            request = requestSerializer.deserialize(body);
        } catch (InvalidPayload exception) {
            return badRequest(exception.toString());
        }

        CallResponse response = GlobalHandler.handle(request);

        String payload = responseSerializer.serialize(response);

        response().setContentType("application/json");

        if (response.isSuccess()) {
            return ok(payload);
        } else {
            return badRequest(payload);
        }
    }
}
