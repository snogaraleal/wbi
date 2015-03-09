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
