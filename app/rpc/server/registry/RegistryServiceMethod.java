package rpc.server.registry;

import java.util.List;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import rpc.shared.data.Type;
import rpc.server.data.Reflect;
import rpc.server.invoke.Invokable;

public class RegistryServiceMethod implements Invokable {
    private RegistryService registryService;

    private String serviceMethodName;
    private Method serviceMethod;

    private List<Type> argumentTypeList;

    public RegistryServiceMethod(
            RegistryService registryService,
            String serviceMethodName) {

        this.registryService = registryService;
        this.serviceMethodName = serviceMethodName;
    }

    public RegistryService getRegistryService() {
        return registryService;
    }

    public String getServiceMethodName() {
        return serviceMethodName;
    }

    private Method loadServiceMethod() throws RegistryException {
        Class<?> serviceClass = registryService.getServiceClass();

        for (Method method : serviceClass.getDeclaredMethods()) {
            if (method.getName().equals(serviceMethodName)) {
                if (Modifier.isStatic(method.getModifiers())) {
                    return method;
                } else {
                    throw new RegistryException(
                        this,
                        RegistryException.Reason.SERVICE_METHOD_NOT_STATIC);
                }
            }
        }

        throw new RegistryException(
            this, RegistryException.Reason.SERVICE_METHOD_NOT_FOUND);
    }

    public Method getServiceMethod() throws RegistryException {
        if (serviceMethod == null) {
            serviceMethod = loadServiceMethod();
        }

        return serviceMethod;
    }

    @Override
    public List<Type> getArgumentTypeList() throws Exception {
        if (argumentTypeList == null) {
            argumentTypeList = Reflect.getArgumentTypeList(getServiceMethod());
        }

        return argumentTypeList;
    }

    @Override
    public Object invoke(Object... arguments) throws Exception {
        return getServiceMethod().invoke(null, arguments);
    }
}
