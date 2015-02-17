package rpc.server.controllers.play;

import play.mvc.Controller;

import rpc.shared.call.CallRequest;
import rpc.shared.call.CallResponse;
import rpc.server.call.DefaultCallRequestServerSerializer;
import rpc.server.call.DefaultCallResponseServerSerializer;

public class BaseController extends Controller {
    protected static CallRequest.ServerSerializer requestSerializer =
        new DefaultCallRequestServerSerializer();
    protected static CallResponse.ServerSerializer responseSerializer =
        new DefaultCallResponseServerSerializer();
}
