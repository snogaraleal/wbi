package rpc.client;

public class ClientRequestFactory {
    private String className;

    public ClientRequestFactory(String className) {
        this.className = className;
    }

    public ClientRequest call(String methodName) {
        return new ClientRequest(className, methodName);
    }
}
