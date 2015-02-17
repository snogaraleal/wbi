package rpc.server.registry;

import java.util.Map;
import java.util.HashMap;

import java.lang.reflect.Type;

import rpc.server.Service;

public class RegistryService {
    private String serviceClassName;
    private Class<?> serviceClass;

    private Map<String, RegistryServiceMethod> registryServiceMethods =
        new HashMap<String, RegistryServiceMethod>();

    public RegistryService(String serviceClassName) {
        this.serviceClassName = serviceClassName;
    }

    public String getServiceClassName() {
        return serviceClassName;
    }

    private Class<?> loadServiceClass() throws RegistryException {
        try {
            serviceClass = Class.forName(serviceClassName);

            for (Type interfaceType : serviceClass.getGenericInterfaces()) {
                if (interfaceType == Service.class) {
                    return serviceClass;
                }
            }

            throw new RegistryException(
                this, RegistryException.Reason.SERVICE_CLASS_NOT_ENABLED);

        } catch (ClassNotFoundException exception) {
            throw new RegistryException(
                this, RegistryException.Reason.SERVICE_CLASS_NOT_FOUND,
                exception);
        }
    }

    public Class<?> getServiceClass() throws RegistryException {
        if (serviceClass == null) {
            serviceClass = loadServiceClass();
        }

        return serviceClass;
    }

    public RegistryServiceMethod getMethod(String name)
        throws RegistryException {

        RegistryServiceMethod registryServiceMethod =
            registryServiceMethods.get(name);

        if (registryServiceMethod == null) {
            registryServiceMethod = new RegistryServiceMethod(this, name);
            registryServiceMethod.getServiceMethod();
        }

        registryServiceMethods.put(name, registryServiceMethod);
        return registryServiceMethod;
    }
}
