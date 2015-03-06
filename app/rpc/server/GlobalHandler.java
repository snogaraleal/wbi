package rpc.server;

import rpc.server.invoke.Invoker;
import rpc.server.invoke.InvokerException;
import rpc.server.registry.Registry;
import rpc.server.registry.RegistryException;
import rpc.server.registry.RegistryServiceMethod;
import rpc.shared.call.CallRequest;
import rpc.shared.call.CallResponse;

public class GlobalHandler {
    protected static Invoker defaultInvoker;

    public static void setDefaultInvoker(Invoker invoker) {
        defaultInvoker = invoker;
    }

    public static CallResponse handle(CallRequest request) {
        boolean success;
        String payload;

        try {
            RegistryServiceMethod method = Registry.get(
                request.getClassName(), request.getMethodName());
            payload = defaultInvoker.invoke(
                method, request.getArgumentPayloadList());
            success = true;

        } catch (RegistryException exception) {
            payload = exception.toString();
            success = false;

        } catch (InvokerException exception) {
            payload = exception.toString();
            success = false;
        }

        return new CallResponse(request.getToken(), success, payload);
    }
}
