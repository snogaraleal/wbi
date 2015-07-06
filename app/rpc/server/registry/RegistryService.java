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

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import rpc.server.Service;

/**
 * Service class in the {@link Registry}.
 */
public class RegistryService {
    private String serviceClassName;
    private Class<?> serviceClass;

    private Map<String, RegistryServiceMethod> registryServiceMethods =
        new HashMap<String, RegistryServiceMethod>();

    /**
     * Initialize {@code RegistryService}.
     *
     * @param serviceClassName Service class name.
     */
    public RegistryService(String serviceClassName) {
        this.serviceClassName = serviceClassName;
    }

    /**
     * Get service class name.
     *
     * @return Service class name.
     */
    public String getServiceClassName() {
        return serviceClassName;
    }

    /**
     * Load service class via reflection.
     *
     * @return Service class.
     * @throws RegistryException
     */
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

    /**
     * Get service class.
     *
     * @return Service class.
     * @throws RegistryException
     */
    public Class<?> getServiceClass() throws RegistryException {
        if (serviceClass == null) {
            serviceClass = loadServiceClass();
        }

        return serviceClass;
    }

    /**
     * Get method from service.
     *
     * @param name Method name.
     * @return {@link RegistryServiceMethod}.
     * @throws RegistryException
     */
    public RegistryServiceMethod getMethod(String name)
        throws RegistryException {

        RegistryServiceMethod registryServiceMethod =
            registryServiceMethods.get(name);

        if (registryServiceMethod == null) {
            registryServiceMethod = new RegistryServiceMethod(this, name);

            // Load method
            registryServiceMethod.getServiceMethod();
        }

        registryServiceMethods.put(name, registryServiceMethod);
        return registryServiceMethod;
    }
}