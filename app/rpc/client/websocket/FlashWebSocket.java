package rpc.client.websocket;

public class FlashWebSocket extends WebSocket {
    public FlashWebSocket(String url) {
        super();
    }

    @Override
    public void send(String message) {
    }

    @Override
    public void close() {
    }

    public static boolean isSupported() {
        return false;
    }
}
