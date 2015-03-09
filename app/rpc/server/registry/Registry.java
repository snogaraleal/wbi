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
