package rpc.client.websocket;

import java.util.List;
import java.util.ArrayList;

public abstract class WebSocket {
    public static interface Listener {
        void onOpen(WebSocket socket);
        void onMessage(WebSocket socket, String message);
        void onError(WebSocket socket, String reason);
        void onClose(WebSocket socket, String reason, boolean clean);
    }

    private List<Listener> listeners = new ArrayList<Listener>();

    public WebSocket() {
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public static WebSocket create(String url) {
        if (TrueWebSocket.isSupported()) {
            return new TrueWebSocket(url);
        }

        if (FlashWebSocket.isSupported()) {
            return new FlashWebSocket(url);
        }

        return null;
    }

    public abstract void send(String message);
    public abstract void close();

    protected void onOpen() {
        for (Listener listener : listeners) {
            listener.onOpen(this);
        }
    }

    protected void onMessage(String message) {
        for (Listener listener : listeners) {
            listener.onMessage(this, message);
        }
    }

    protected void onError(String reason) {
        for (Listener listener : listeners) {
            listener.onError(this, reason);
        }
    }

    protected void onClose(String reason, boolean clean) {
        for (Listener listener : listeners) {
            listener.onClose(this, reason, clean);
        }
    }

    public static boolean isSupported() {
        return TrueWebSocket.isSupported() || FlashWebSocket.isSupported();
    }
}
