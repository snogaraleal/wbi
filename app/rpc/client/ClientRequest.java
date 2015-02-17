package rpc.client;

import java.util.List;
import java.util.ArrayList;

import rpc.shared.data.Type;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ClientRequest {
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
        void onSuccess(ClientRequest request, T object);
        void onFailure(ClientRequest request, Error error);
    }

    private String className;
    private String methodName;

    private Object[] arguments;
    private Type expected;

    private List<Listener> listeners = new ArrayList<Listener>();

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

    public ClientRequest setArguments(Object... arguments) {
        this.arguments = arguments;
        return this;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public ClientRequest setExpected(Type expected) {
        this.expected = expected;
        return this;
    }

    public Type getExpected() {
        return expected;
    }

    public ClientRequest addListener(Listener listener) {
        listeners.add(listener);
        return this;
    }

    public ClientRequest send(Client client) {
        client.send(this);
        return this;
    }

    public ClientRequest send() {
        return send(Client.get());
    }

    public ClientRequest cancel(Client client) {
        client.cancel(this);
        return this;
    }

    public ClientRequest cancel() {
        return cancel(Client.get());
    }

    public void finish(Object object) {
        for (Listener listener : listeners) {
            listener.onSuccess(this, object);
        }
    }

    public void finish(Error error) {
        for (Listener listener : listeners) {
            listener.onFailure(this, error);
        }
    }
}
