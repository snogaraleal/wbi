/*
 * WBI Indicator Explorer
 *
 * Copyright 2015 Sebastian Nogara <snogaraleal@gmail.com>
 *
 * This file is part of WBI.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package rpc.client.websocket;

/**
 * Native {@link WebSocket} implemented by the browser.
 */
public class TrueWebSocket extends WebSocket {
    private String url;

    /**
     * Initialize {@code TrueWebSocket}.
     *
     * @param url {@link WebSocket} URL.
     */
    public TrueWebSocket(String url) {
        super();
        this.url = url;

        init();
        bind();
    }

    /**
     * Initialize {@code WebSocket} object.
     */
    private native void init() /*-{
        var url = this.@rpc.client.websocket.TrueWebSocket::url;
        this.socket = new WebSocket(url);
    }-*/;

    /**
     * Bind event handlers.
     */
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

    /**
     * Get whether {@link TrueWebSocket} is supported by the browser.
     *
     * @return Whether {@code TrueWebSocket} is supported.
     */
    public native static boolean isSupported() /*-{
        return 'WebSocket' in $wnd;
    }-*/;
}