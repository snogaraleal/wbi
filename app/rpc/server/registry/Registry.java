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

import java.util.HashMap;
import java.util.Map;

/**
 * Registry of service classes.
 */
public class Registry {
    private Map<String, RegistryService> registryServices =
        new HashMap<String, RegistryService>();

    /**
     * Initialize {@code Registry}.
     */
    private Registry() {}

    /**
     * Get a {@link RegistryService} for the specified class name.
     *
     * @param name Service class name.
     * @return {@code RegistryService}.
     * @throws RegistryException
     */
    public RegistryService getService(String name) throws RegistryException {
        RegistryService registryService = registryServices.get(name);

        if (registryService == null) {
            registryService = new RegistryService(name);

            // Load service class
            registryService.getServiceClass();
        }

        registryServices.put(name, registryService);
        return registryService;
    }

    /**
     * Single instance.
     */
    private static Registry instance;

    /**
     * Get single instance of {@code Registry}.
     *
     * @return {@code Registry} instance.
     */
    public static Registry get() {
        if (instance == null) {
            instance = new Registry();
        }

        return instance;
    }

    /**
     * Obtain a {@link RegistryServiceMethod} for the specified
     * service class name and method name.
     *
     * @param className Service class name.
     * @param methodName Method name.
     * @return {@code RegistryServiceMethod}.
     * @throws RegistryException
     */
    public static RegistryServiceMethod get(
            String className, String methodName) throws RegistryException {

        return get().getService(className).getMethod(methodName);
    }
}