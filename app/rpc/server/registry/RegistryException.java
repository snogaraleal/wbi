package rpc.server.registry;

import rpc.server.Service;

@SuppressWarnings("serial")
public class RegistryException extends Exception {
    public static enum Reason {
        SERVICE_CLASS_NOT_FOUND,
        SERVICE_CLASS_NOT_ENABLED,
        SERVICE_METHOD_NOT_FOUND,
        SERVICE_METHOD_NOT_STATIC,
        SERVICE_METHOD_NOT_PUBLIC
    }

    public RegistryException(
            RegistryService registryService,
            Reason reason) {

        super(getMessage(registryService, null, reason));
    }

    public RegistryException(
            RegistryServiceMethod registryServiceMethod,
            Reason reason) {

        super(getMessage(
            registryServiceMethod.getRegistryService(),
            registryServiceMethod, reason));
    }

    public RegistryException(
            RegistryServiceMethod registryServiceMethod,
            Reason reason,
            Throwable caught) {

        super(getMessage(
            registryServiceMethod.getRegistryService(),
            registryServiceMethod, reason) + " " +
                "(" + caught.toString() + ")");
    }

    public RegistryException(
            RegistryService registryService,
            Reason reason,
            Throwable caught) {

        super(getMessage(registryService, null, reason) + " " +
                "(" + caught.toString() + ")");
    }

    private static String GENERIC_MESSAGE = "Registry exception";

    private static String getMessage(
            RegistryService registryService,
            RegistryServiceMethod registryServiceMethod,
            Reason reason) {

        if (reason == null) {
            return GENERIC_MESSAGE;
        }

        switch (reason) {
            case SERVICE_CLASS_NOT_FOUND:
                return
                    "Service class" + 
                    " '" + registryService.getServiceClassName() + "' " +
                    "not found";

            case SERVICE_CLASS_NOT_ENABLED:
                return
                    "Service class" +
                    " '" + registryService.getServiceClassName() + "' " +
                    "must be enabled by implementing " + Service.class;

            case SERVICE_METHOD_NOT_FOUND:
                return
                    "Method" +
                    " '" + registryServiceMethod.getServiceMethodName() + "' " +
                    "not found in service class" +
                    " '" + registryService.getServiceClassName() + "'";

            case SERVICE_METHOD_NOT_STATIC:
                return
                    "Method" +
                    " '" + registryServiceMethod.getServiceMethodName() + "' " +
                    "defined in service class" +
                    " '" + registryService.getServiceClassName() + "' " +
                    "must be static";

            case SERVICE_METHOD_NOT_PUBLIC:
                return
                    "Method" +
                    " '" + registryServiceMethod.getServiceMethodName() + "' " +
                    "defined in service class" +
                    " '" + registryService.getServiceClassName() + "' " +
                    "is not public";

            default:
                return GENERIC_MESSAGE;
        }
    }
}
