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

/**
 * RPC response.
 *
 * <ol>
 *   <li>Produced and serialized by the server.</li>
 *   <li>Sent from server to client.</li>
 *   <li>Deserialized and handled by the client.</li>
 * </ol>
 */
public class CallResponse {
    /**
     * Server-side serialization of a {@link CallResponse}.
     */
    public static interface ServerSerializer {
        /**
         * Serialize the specified {@code CallResponse}.
         *
         * @param request {@code CallResponse} to serialize.
         * @return Response payload.
         */
        String serialize(CallResponse request);
    }

    /**
     * Client-side deserialization of a {@link CallResponse}.
     */
    public static interface ClientSerializer {
        /**
         * Deserialize the specified payload.
         *
         * @param payload Response payload.
         * @return Deserialized {@code CallResponse}.
         * @throws InvalidPayload
         */
        CallResponse deserialize(String payload) throws InvalidPayload;
    }

    /**
     * Description of a response message.
     */
    public static class Message {
        /*
         * Size of the response array.
         */
        public static int SIZE = 3;

        /*
         * Positions of values in response array.
         */
        public static int POSITION_TOKEN = 0;
        public static int POSITION_SUCCESS = 1;
        public static int POSITION_RETURN_VALUE = 2;
    }

    private String token;
    private boolean success;
    private String payload;

    /**
     * Initialize {@code CallResponse}.
     *
     * @param token Identifier to be included in the response.
     * @param success Whether the call was successful.
     * @param payload Result payload.
     */
    public CallResponse(String token, boolean success, String payload) {
        this.token = token;
        this.success = success;
        this.payload = payload;
    }

    /**
     * Get response identifier.
     *
     * @return Response identifier.
     */
    public String getToken() {
        return token;
    }

    /**
     * Get whether the call was successful.
     *
     * @return Whether the call was successful.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Get result payload.
     *
     * @return Result payload.
     */
    public String getPayload() {
        return payload;
    }
}