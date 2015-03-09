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

package rpc.shared.call;

public class CallResponse {
    public static interface ServerSerializer {
        String serialize(CallResponse request);
    }

    public static interface ClientSerializer {
        CallResponse deserialize(String payload) throws InvalidPayload;
    }

    public static class Message {
        public static int SIZE = 3;

        public static int POSITION_TOKEN = 0;
        public static int POSITION_SUCCESS = 1;
        public static int POSITION_RETURN_VALUE = 2;
    }

    private String token;
    private boolean success;
    private String payload;

    public CallResponse(String token, boolean success, String payload) {
        this.token = token;
        this.success = success;
        this.payload = payload;
    }

    public String getToken() {
        return token;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getPayload() {
        return payload;
    }
}
