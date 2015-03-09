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

import java.util.ArrayList;
import java.util.List;

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
