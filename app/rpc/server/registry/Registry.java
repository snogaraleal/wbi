package rpc.server.registry;

import java.util.Map;
import java.util.HashMap;

public class Registry {
    private Map<String, RegistryService> registryServices =
        new HashMap<String, RegistryService>();

    private Registry() {
    }

    public RegistryService getService(String name) throws RegistryException {
        RegistryService registryService = registryServices.get(name);

        if (registryService == null) {
            registryService = new RegistryService(name);
            registryService.getServiceClass();
        }

        registryServices.put(name, registryService);
        return registryService;
    }

    private static Registry instance;

    public static Registry get() {
        if (instance == null) {
            instance = new Registry();
        }

        return instance;
    }

    public static RegistryServiceMethod get(
            String className, String methodName) throws RegistryException {

        return get().getService(className).getMethod(methodName);
    }
}
