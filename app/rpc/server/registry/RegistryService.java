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
