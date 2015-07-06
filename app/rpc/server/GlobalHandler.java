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

package rpc.server;

import rpc.server.invoke.Invoker;
import rpc.server.invoke.InvokerException;
import rpc.server.registry.Registry;
import rpc.server.registry.RegistryException;
import rpc.server.registry.RegistryServiceMethod;
import rpc.shared.call.CallRequest;
import rpc.shared.call.CallResponse;

/**
 * Shortcut class for handling requests.
 */
public class GlobalHandler {
    protected static Invoker defaultInvoker;

    /**
     * Set underlying invoker.
     *
     * @param invoker Invoker.
     */
    public static void setDefaultInvoker(Invoker invoker) {
        defaultInvoker = invoker;
    }

    /**
     * Handle the specified {@link CallRequest}.
     *
     * @param request {@code CallRequest} to handle.
     * @return {@link CallResponse}.
     */
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