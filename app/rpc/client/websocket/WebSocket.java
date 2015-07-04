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

/**
 * Abstract class defining a {@code WebSocket}.
 */
public abstract class WebSocket {
    public static boolean ENABLED = false;

    /**
     * {@link WebSocket} event listener.
     */
    public static interface Listener {
        /**
         * Handle open {@link WebSocket}.
         *
         * @param socket Open {@code WebSocket}.
         */
        void onOpen(WebSocket socket);

        /**
         * Handle incoming {@link WebSocket} message.
         *
         * @param socket {@code WebSocket}.
         * @param message Incoming message.
         */
        void onMessage(WebSocket socket, String message);

        /**
         * Handle {@link WebSocket} error.
         *
         * @param socket {@code WebSocket}.
         * @param reason Error description.
         */
        void onError(WebSocket socket, String reason);

        /**
         * Handle closed {@link WebSocket}.
         *
         * @param socket {@code WebSocket}.
         * @param reason Reason for closing {@code WebSocket}.
         * @param clean Whether closing was clean.
         */
        void onClose(WebSocket socket, String reason, boolean clean);
    }

    /**
     * Registered {@link WebSocket.Listener} objects.
     */
    private List<Listener> listeners = new ArrayList<Listener>();

    /**
     * Initialize {@code WebSocket}.
     */
    public WebSocket() {}

    /**
     * Register {@link WebSocket.Listener}.
     *
     * @param listener Listener.
     */
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Create {@code WebSocket} instance.
     *
     * @param url {@code WebSocket} URL.
     * @return New {@code WebSocket} instance.
     */
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

    /**
     * Inform that the {@code WebSocket} is open.
     */
    protected void onOpen() {
        for (Listener listener : listeners) {
            listener.onOpen(this);
        }
    }

    /**
     * Inform that there is an incoming {@code WebSocket} message.
     */
    protected void onMessage(String message) {
        for (Listener listener : listeners) {
            listener.onMessage(this, message);
        }
    }

    /**
     * Inform that there was an error.
     */
    protected void onError(String reason) {
        for (Listener listener : listeners) {
            listener.onError(this, reason);
        }
    }

    /**
     * Inform that the {@code WebSocket} is closed.
     */
    protected void onClose(String reason, boolean clean) {
        for (Listener listener : listeners) {
            listener.onClose(this, reason, clean);
        }
    }

    /**
     * Get whether {@link WebSocket} is supported.
     *
     * @return Whether {@code WebSocket} is supported.
     */
    public static boolean isSupported() {
        return ENABLED &&
            (TrueWebSocket.isSupported() || FlashWebSocket.isSupported());
    }
}