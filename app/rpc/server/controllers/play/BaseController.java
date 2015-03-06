package rpc.server.controllers.play;

import play.mvc.Controller;

import rpc.server.call.DefaultCallRequestServerSerializer;
import rpc.server.call.DefaultCallResponseServerSerializer;
import rpc.shared.call.CallRequest;
import rpc.shared.call.CallResponse;

public class BaseController extends Controller {
    protected static CallRequest.ServerSerializer requestSerializer =
        new DefaultCallRequestServerSerializer();
    protected static CallResponse.ServerSerializer responseSerializer =
        new DefaultCallResponseServerSerializer();
}
