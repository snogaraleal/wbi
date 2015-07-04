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

package rpc.client;

import java.util.ArrayList;
import java.util.List;

import rpc.shared.data.Type;

/**
 * RPC request.
 *
 * <ol>
 *   <li>Create a {@code ClientRequest} with a remote class and method</li>
 *   <li>Specify method arguments with {@link #setArguments(Object...)}</li>
 *   <li>Set the expected return type with {@link #setExpected(Type)}</li>
 *   <li>Attach listeners with {@link #addListener(Listener)}</li>
 *   <li>Send the request with {@link #send()} or {@link #send(Client)}</li>
 * </ol>
 *
 * The request can be cancelled with {@link #cancel()} or
 * {@link #cancel(Client)}.
 *
 * @param <T> Expected return type.
 */
public class ClientRequest<T> {
    /**
     * Global request listener.
     *
     * Useful for debugging, general information or global loading UI
     * indicators.
     */
    public static interface GlobalListener {
        /**
         * Handle sent {@link ClientRequest}.
         *
         * @param clientRequest {@code ClientRequest}.
         */
        void onSend(ClientRequest<?> clientRequest);

        /**
         * Handle completed {@link ClientRequest}.
         *
         * @param clientRequest {@code ClientRequest}.
         */
        void onFinish(ClientRequest<?> clientRequest);
    }

    /**
     * Registered {@link GlobalListener}.
     */
    private static List<GlobalListener> globalListeners =
        new ArrayList<GlobalListener>();

    /**
     * Register {@link GlobalListener}.
     *
     * @param listener Listener to register.
     */
    public static void addGlobalListener(GlobalListener listener) {
        globalListeners.add(listener);
    }

    /**
     * Unregister {@link GlobalListener}.
     *
     * @param listener Listener to unregister.
     */
    public static void removeGlobalListener(GlobalListener listener) {
        globalListeners.remove(listener);
    }

    /**
     * Error during request.
     */
    @SuppressWarnings("serial")
    public static class Error extends Exception {
        /**
         * Initialize {@link Error}.
         *
         * @param caught {@code Throwable} to wrap.
         */
        public Error(Throwable caught) {
            super(caught);
        }

        /**
         * Initialize {@link Error}.
         *
         * @param reason Error message.
         */
        public Error(String reason) {
            super(reason);
        }
    }

    /**
     * Request listener.
     *
     * @param <T>
     */
    public static interface Listener<T> {
        /**
         * Handle request result.
         *
         * @param object Returned result.
         */
        void onSuccess(T object);

        /**
         * Handle request error.
         *
         * @param error Error.
         */
        void onFailure(Error error);
    }

    private String className;
    private String methodName;

    private Object[] arguments;
    private Type expected;

    /**
     * Registered request listeners.
     */
    private List<Listener<T>> listeners = new ArrayList<Listener<T>>();

    /**
     * Initialize {@code ClientRequest}.
     *
     * @param className Remote service class.
     * @param methodName Method name.
     */
    public ClientRequest(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    /**
     * Get remote service class name.
     *
     * @return Service class name.
     */
    public String getClassName() {
        return className;
    }

    /**
     * Get remote service method name.
     *
     * @return Service method name.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Set arguments for the remote method.
     *
     * @param arguments Arguments.
     * @return {@code ClientRequest}.
     */
    public ClientRequest<T> setArguments(Object... arguments) {
        this.arguments = arguments;
        return this;
    }

    /**
     * Get arguments for remote method.
     *
     * @return Arguments.
     */
    public Object[] getArguments() {
        return arguments;
    }

    /**
     * Set expected return type.
     *
     * @param expected Expected return type.
     * @return {@code ClientRequest}.
     */
    public ClientRequest<T> setExpected(Type expected) {
        this.expected = expected;
        return this;
    }

    /**
     * Get expected return type.
     *
     * @return Expected return type.
     */
    public Type getExpected() {
        return expected;
    }

    /**
     * Add request listener.
     *
     * @param listener Request listener.
     * @return {@code ClientRequest}.
     */
    public ClientRequest<T> addListener(Listener<T> listener) {
        listeners.add(listener);
        return this;
    }

    /**
     * Send request using the specified {@link Client}.
     *
     * @param client {@code Client}.
     * @return {@code ClientRequest}.
     */
    public ClientRequest<T> send(Client client) {
        for (GlobalListener listener : globalListeners) {
            listener.onSend(this);
        }

        client.send(this);
        return this;
    }

    /**
     * Send request using the default {@link Client}.
     *
     * @return {@code ClientRequest}.
     */
    public ClientRequest<T> send() {
        return send(Client.get());
    }

    /**
     * Cancel request using the specified {@link Client}.
     *
     * @param client {@code Client}.
     * @return {@code ClientRequest}.
     */
    public ClientRequest<T> cancel(Client client) {
        client.cancel(this);
        return this;
    }

    /**
     * Cancel request using the default {@link Client}.
     *
     * @return {@code ClientRequest}.
     */
    public ClientRequest<T> cancel() {
        return cancel(Client.get());
    }

    /**
     * Inform that this {@code ClientRequest} is completed and the specified
     * object is the return value of the remote service method.
     *
     * @param object Returned object.
     */
    public void finish(T object) {
        for (GlobalListener listener : globalListeners) {
            listener.onFinish(this);
        }

        for (Listener<T> listener : listeners) {
            listener.onSuccess(object);
        }
    }

    /**
     * Inform that this {@code ClientRequest} is completed and an error
     * occurred when processing the request remotely.
     *
     * @param error Remote error.
     */
    public void finish(Error error) {
        for (GlobalListener listener : globalListeners) {
            listener.onFinish(this);
        }

        for (Listener<T> listener : listeners) {
            listener.onFailure(error);
        }
    }
}