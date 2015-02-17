package controllers.rpc;

import play.mvc.WebSocket;

import rpc.server.controllers.play.BaseWebSocketController;

public class WebSocketController extends BaseWebSocketController {
    public static WebSocket<String> socket() {
        return BaseWebSocketController.socket();
    }
}
