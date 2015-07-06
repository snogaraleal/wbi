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

package rpc.server.registry;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import rpc.server.data.Reflect;
import rpc.server.invoke.Invokable;
import rpc.shared.data.Type;

/**
 * Loaded method belonging to a {@link RegistryService}.
 */
public class RegistryServiceMethod implements Invokable {
    private RegistryService registryService;

    private String serviceMethodName;
    private Method serviceMethod;

    private List<Type> argumentTypeList;

    /**
     * Initialize {@code RegistryServiceMethod}.
     *
     * @param registryService {@link RegistryService}.
     * @param serviceMethodName Method name.
     */
    public RegistryServiceMethod(
            RegistryService registryService,
            String serviceMethodName) {

        this.registryService = registryService;
        this.serviceMethodName = serviceMethodName;
    }

    /**
     * Get service.
     *
     * @return Registry service.
     */
    public RegistryService getRegistryService() {
        return registryService;
    }

    /**
     * Get method name.
     *
     * @return Method name.
     */
    public String getServiceMethodName() {
        return serviceMethodName;
    }

    /**
     * Load service method via reflection.
     *
     * @return Service method.
     * @throws RegistryException
     */
    private Method loadServiceMethod() throws RegistryException {
        Class<?> serviceClass = registryService.getServiceClass();

        for (Method method : serviceClass.getDeclaredMethods()) {
            if (method.getName().equals(serviceMethodName)) {
                int modifiers = method.getModifiers();

                if (!Modifier.isStatic(modifiers)) {
                    throw new RegistryException(
                        this,
                        RegistryException.Reason.SERVICE_METHOD_NOT_STATIC);

                } else if (!Modifier.isPublic(modifiers)) {
                    throw new RegistryException(
                        this,
                        RegistryException.Reason.SERVICE_METHOD_NOT_PUBLIC);

                } else {
                    return method;
                }
            }
        }

        throw new RegistryException(
            this, RegistryException.Reason.SERVICE_METHOD_NOT_FOUND);
    }

    /**
     * Get service method.
     *
     * @return Service method.
     * @throws RegistryException
     */
    public Method getServiceMethod() throws RegistryException {
        if (serviceMethod == null) {
            serviceMethod = loadServiceMethod();
        }

        return serviceMethod;
    }

    /*
     * Implementation of {@code Invokable}.
     */

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