package rpc.client.websocket;

public class TrueWebSocket extends WebSocket {
    private String url;

    public TrueWebSocket(String url) {
        super();
        this.url = url;

        init();
        bind();
    }

    private native void init() /*-{
        var url = this.@rpc.client.websocket.TrueWebSocket::url;
        this.socket = new WebSocket(url);
    }-*/;

    private native void bind() /*-{
        var that = this;

        that.socket.onopen = function (event) {
            that.@rpc.client.websocket.TrueWebSocket::onOpen()();
        };

        that.socket.onmessage = function (event) {
            that.@rpc.client.websocket.TrueWebSocket::onMessage(
                Ljava/lang/String;)(event.data);
        };

        that.socket.onerror = function (event) {
            that.@rpc.client.websocket.TrueWebSocket::onError(
                Ljava/lang/String;)(event.toString());
        };

        that.socket.onclose = function (event) {
            that.@rpc.client.websocket.TrueWebSocket::onClose(
                Ljava/lang/String;Z)(event.reason, event.wasClean);
        };
    }-*/;

    @Override
    public native void send(String message) /*-{
        this.socket.send(message);
    }-*/;

    @Override
    public native void close() /*-{
        this.socket.close();
    }-*/;

    public native static boolean isSupported() /*-{
        return 'WebSocket' in $wnd;
    }-*/;
}
