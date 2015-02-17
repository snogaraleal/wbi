package controllers.rpc;

import rpc.server.controllers.play.BaseHTTPController;

public class HTTPController extends BaseHTTPController {
    public static play.mvc.Result call() {
        return BaseHTTPController.call();
    }
}
