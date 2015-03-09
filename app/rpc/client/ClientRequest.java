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

public class ClientRequest<T> {
    public static interface GlobalListener {
        void onSend(ClientRequest<?> clientRequest);
        void onFinish(ClientRequest<?> clientRequest);
    }

    private static List<GlobalListener> globalListeners =
        new ArrayList<GlobalListener>();

    public static void addGlobalListener(GlobalListener listener) {
        globalListeners.add(listener);
    }

    public static void removeGlobalListener(GlobalListener listener) {
        globalListeners.remove(listener);
    }

    @SuppressWarnings("serial")
    public static class Error extends Exception {
        public Error(Throwable caught) {
            super(caught);
        }

        public Error(String reason) {
            super(reason);
        }
    }

    public static interface Listener<T> {
        void onSuccess(T object);
        void onFailure(Error error);
    }

    private String className;
    private String methodName;

    private Object[] arguments;
    private Type expected;

    private List<Listener<T>> listeners = new ArrayList<Listener<T>>();

    public ClientRequest(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public ClientRequest<T> setArguments(Object... arguments) {
        this.arguments = arguments;
        return this;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public ClientRequest<T> setExpected(Type expected) {
        this.expected = expected;
        return this;
    }

    public Type getExpected() {
        return expected;
    }

    public ClientRequest<T> addListener(Listener<T> listener) {
        listeners.add(listener);
        return this;
    }

    public ClientRequest<T> send(Client client) {
        for (GlobalListener listener : globalListeners) {
            listener.onSend(this);
        }

        client.send(this);
        return this;
    }

    public ClientRequest<T> send() {
        return send(Client.get());
    }

    public ClientRequest<T> cancel(Client client) {
        client.cancel(this);
        return this;
    }

    public ClientRequest<T> cancel() {
        return cancel(Client.get());
    }

    public void finish(T object) {
        for (GlobalListener listener : globalListeners) {
            listener.onFinish(this);
        }

        for (Listener<T> listener : listeners) {
            listener.onSuccess(object);
        }
    }

    public void finish(Error error) {
        for (GlobalListener listener : globalListeners) {
            listener.onFinish(this);
        }

        for (Listener<T> listener : listeners) {
            listener.onFailure(error);
        }
    }
}
