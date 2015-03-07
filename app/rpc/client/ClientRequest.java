package rpc.client;

import java.util.ArrayList;
import java.util.List;

import rpc.shared.data.Type;

public class ClientRequest<T> {
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
        for (Listener<T> listener : listeners) {
            listener.onSuccess(object);
        }
    }

    public void finish(Error error) {
        for (Listener<T> listener : listeners) {
            listener.onFailure(error);
        }
    }
}
