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
 * Adobe Flash {@link WebSocket} emulation.
 */
public class FlashWebSocket extends WebSocket {
    /**
     * Initialize {@code FlashWebSocket}.
     *
     * @param url {@link WebSocket} URL.
     */
    public FlashWebSocket(String url) {
        super();
    }

    @Override
    public void send(String message) {}

    @Override
    public void close() {}

    /**
     * Get whether {@link FlashWebSocket} is supported by the browser.
     *
     * @return Whether {@code FlashWebSocket} is supported.
     */
    public static boolean isSupported() {
        return false;
    }
}