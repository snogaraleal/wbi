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

import java.util.List;

import rpc.shared.UUID;

/**
 * RPC request. Serialized and then sent by the client,
 * deserialized and then dispatched by the server.
 */
public class CallRequest {
    /**
     * Server-side deserialization.
     */
    public static interface ServerSerializer {
        /**
         * Deserialize the specified payload.
         *
         * @param payload Request payload.
         * @return Deserialized {@code CallRequest}.
         * @throws InvalidPayload
         */
        CallRequest deserialize(String payload) throws InvalidPayload;
    }

    /**
     * Client-side serialization.
     */
    public static interface ClientSerializer {
        /**
         * Serialize the specified {@code CallRequest}.
         *
         * @param request {@code CallRequest} to serialize.
         * @return Request payload.
         */
        String serialize(CallRequest request);
    }

    /**
     * Description of a request message.
     */
    public static class Message {
        /*
         * Size of the request array.
         */
        public static int SIZE = 4;

        /*
         * Positions of values in request array.
         */
        public static int POSITION_CLASS_NAME = 0;
        public static int POSITION_METHOD_NAME = 1;
        public static int POSITION_TOKEN = 2;
        public static int POSITION_ARGUMENTS = 3;
    }

    private String className;
    private String methodName;
    private String token;
    private List<String> argumentPayloadList;

    /**
     * Initialize {@code CallRequest}.
     *
     * @param className Service class name.
     * @param methodName Service class method.
     * @param argumentPayloadList List of serialized arguments.
     * @param token Unique token for request identification.
     */
    public CallRequest(
            String className, String methodName,
            List<String> argumentPayloadList, String token) {

        this.className = className;
        this.methodName = methodName;
        this.argumentPayloadList = argumentPayloadList;
        this.token = token;
    }

    /**
     * Initialize {@code CallRequest}.
     *
     * @param className Service class name.
     * @param methodName Service class method.
     * @param argumentPayloadList List of serialized arguments.
     */
    public CallRequest(
            String className, String methodName,
            List<String> argumentPayloadList) {

        this(className, methodName, argumentPayloadList, generateToken());
    }

    /**
     * Get service class name.
     *
     * @return Service class name.
     */
    public String getClassName() {
        return className;
    }

    /**
     * Get service method name.
     *
     * @return Service method name.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Generate unique token for request identification.
     *
     * @return Request identification.
     */
    private static String generateToken() {
        return UUID.uuid();
    }

    /**
     * Get unique token for request identification.
     *
     * @return Request identification.
     */
    public String getToken() {
        return token;
    }

    /**
     * Get list of serialized arguments.
     *
     * @return List of serialized arguments.
     */
    public List<String> getArgumentPayloadList() {
        return argumentPayloadList;
    }
}
